/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.clients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.github.kubesys.backend.clients.PostgresPoolClient.SQLObject.CompareCondition;
import io.github.kubesys.backend.clients.PostgresPoolClient.SQLObject.EqualCondition;
import io.github.kubesys.backend.clients.PostgresPoolClient.SQLObject.LikeCondition;
import io.github.kubesys.backend.models.auth.AuthBaseModel;
import io.github.kubesys.devfrk.spring.utils.ClassUtils;
import io.github.kubesys.devfrk.spring.utils.JSONUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

/**
 * @author  wuheng@iscas.ac.cn
 * @version 1.2.0
 * @since   2023/07/24
 * 
 */
@Component
public class PostgresPoolClient {

	private static Logger m_logger = Logger.getLogger(PostgresPoolClient.class);

	private static final Map<String, String> tables = new HashMap<>();

	static {
		Set<Class<?>> classes = ClassUtils.scan(new String[] {AuthBaseModel.class.getPackageName()});
		for (Class<?> clazz : classes) {
			Table table = clazz.getAnnotation(Table.class);
			if (table != null) {
				tables.put(clazz.getName(), table.name());
			}
		}
	}

	private static final Map<Integer, String> compares = new HashMap<>();

	static {
		compares.put(0, "=");
		compares.put(1, ">");
		compares.put(2, ">=");
		compares.put(3, "<");
		compares.put(4, "<=");
	}
	
	@PersistenceContext(unitName = "authEntityManager")
    private EntityManager authEntityManager;

    @PersistenceContext(unitName = "kubeEntityManager")
    private EntityManager kubeEntityManager;

    EntityManager getEntityManager(String cls) {
    	if (tables.containsKey(cls)) {
    		return authEntityManager;
    	}
    	return kubeEntityManager;
    }
    

	@Transactional
	public void createObject(String cls, JsonNode data) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		Object treeToValue = objectMapper.treeToValue(data, Class.forName(cls));
		getEntityManager(cls).persist(treeToValue);
	}

	@Transactional
	public void updateObject(String cls, JsonNode data) throws Exception {
		Object treeToValue = new ObjectMapper().treeToValue(data, Class.forName(cls));
		getEntityManager(cls).merge(treeToValue);
	}


	@Transactional
	public void removeObject(String cls, JsonNode data) throws Exception {
		Object treeToValue = new ObjectMapper().treeToValue(data, Class.forName(cls));
		getEntityManager(cls).remove(treeToValue);
	}

	@Transactional
	public JsonNode getObject(String cls, SQLObject data) throws Exception {
		return listObjects(cls, data, 1, 10).get("data").get(0);
	}


	@Transactional
	public long countObjects(String cls, SQLObject data) throws Exception {

		SQLBuilder builder = new SQLBuilder();

		// SELECT and Conditions
		builder.buildSelect(cls).buildConditions(data);
		return (long) execCountSql(cls, builder.getValue());

	}

	@Transactional
	public JsonNode listObjects(String cls, SQLObject data, int page, int number) throws Exception {

		SQLBuilder builder = new SQLBuilder();

		// SELECT and Conditions
		builder.buildSelect(cls).buildConditions(data);

		// COUNT
		long total = (long) execCountSql(cls, builder.getValue());

		// LIMIT
		builder.addLimit(page, number);

		List<Object> list = execQueySql(cls, builder.getValue(), buildItems(data));

		// RESULT
		ObjectNode result = new ObjectMapper().createObjectNode();
		result.put("count", total);
		result.set("data", JSONUtils.fromList(list));

		return result;
	}

	private StringBuilder buildItems(SQLObject data) {
		StringBuilder sb = new StringBuilder();
		sb.append(data.distinct ? " DISTINCT " : "");
		if (data.getTargets() != null) {
			for (String item : data.getTargets()) {
				sb.append(item).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		} else {
			sb.append("*");
		}
		return sb;
	}

	@SuppressWarnings("unchecked")
	private List<Object> execQueySql(String cls, String sql, StringBuilder sb) {
		String query = sql.replace("#TEMP#", sb.toString());
		m_logger.info("Query:" + query);
		return getEntityManager(cls).createNativeQuery(query).getResultList();
	}

	private long execCountSql(String cls, String sql) {
		String count = sql.replace("#TEMP#", "COUNT(*)");
		m_logger.info("Count:" + count);
		return (long) getEntityManager(cls).createNativeQuery(count).getSingleResult();
	}


	public static class SQLBuilder {

		protected StringBuilder sqlBuilder = new StringBuilder();

		public SQLBuilder buildSelect(String cls) {
			sqlBuilder.append("SELECT ").append("#TEMP#").append(" FROM ").append(tables.get(cls));
			return this;
		}

		public SQLBuilder addLimit(int page, int number) {
			sqlBuilder.append(" LIMIT ").append(number).append(" OFFSET ").append((page - 1) * number);
			return this;
		}

		public SQLBuilder buildConditions(SQLObject data) {

			boolean first = true;
			
			if (data.getEqualConds() != null) {
				for (EqualCondition sc : data.getEqualConds()) {
					sqlBuilder.append(first ? " WHERE " : " AND ").append(sc.getName()).append(" = '")
							.append(sc.getValue()).append("'");
					first = false;
				}
			}
			
			if (data.getLikeConds() != null) {
				for (LikeCondition sc : data.getLikeConds()) {
					sqlBuilder.append(first ? " WHERE " : " AND ").append(sc.getName()).append(" LIKE '%")
							.append(sc.getValue()).append("%'");
					first = false;
				}
			}

			if (data.getCompareConds() != null) {
				for (CompareCondition rc : data.getCompareConds()) {
					sqlBuilder.append(first ? " WHERE " : " AND ").append(rc.getName()).append(compares.get(rc.type))
							.append("'").append(rc.getValue()).append("'");
					first = false;
				}
			}

			return this;
		}

		public String getValue() {
			return sqlBuilder.toString();
		}

	}

	public static class SQLObjectBuilder {

		protected SQLObject sqlObj = new SQLObject();

		public SQLObjectBuilder addTarget(String str) {
			List<String> targets = sqlObj.getTargets() == null ? new ArrayList<>() : sqlObj.getTargets();
			targets.add(str);
			sqlObj.setTargets(targets);
			return this;
		}

		public SQLObjectBuilder addEqualCondition(String key, String val) {
			List<EqualCondition> lcs = sqlObj.getEqualConds() == null ? new ArrayList<>() : sqlObj.getEqualConds();
			lcs.add(new LikeCondition(key, val));
			sqlObj.setEqualConds(lcs);
			return this;
		}
		
		public SQLObjectBuilder addLikeCondition(String key, String val) {
			List<LikeCondition> lcs = sqlObj.getLikeConds() == null ? new ArrayList<>() : sqlObj.getLikeConds();
			lcs.add(new LikeCondition(key, val));
			sqlObj.setLikeConds(lcs);
			return this;
		}
		
		public SQLObjectBuilder addEqualCondition(String key, Object val) {
			List<CompareCondition> ccs = sqlObj.getCompareConds() == null ? new ArrayList<>() : sqlObj.getCompareConds();
			ccs.add(new CompareCondition(key, val, 0));
			sqlObj.setCompareConds(ccs);
			return this;
		}
		
		public SQLObjectBuilder addGreatThanCondition(String key, Object val) {
			List<CompareCondition> ccs = sqlObj.getCompareConds() == null ? new ArrayList<>() : sqlObj.getCompareConds();
			ccs.add(new CompareCondition(key, val, 1));
			sqlObj.setCompareConds(ccs);
			return this;
		}
		
		public SQLObjectBuilder addGreatThanAndEqualCondition(String key, Object val) {
			List<CompareCondition> ccs = sqlObj.getCompareConds() == null ? new ArrayList<>() : sqlObj.getCompareConds();
			ccs.add(new CompareCondition(key, val, 2));
			sqlObj.setCompareConds(ccs);
			return this;
		}
		
		public SQLObjectBuilder addLessThanCondition(String key, Object val) {
			List<CompareCondition> ccs = sqlObj.getCompareConds() == null ? new ArrayList<>() : sqlObj.getCompareConds();
			ccs.add(new CompareCondition(key, val, 3));
			sqlObj.setCompareConds(ccs);
			return this;
		}
		
		public SQLObjectBuilder addLessThanAndEqualCondition(String key, Object val) {
			List<CompareCondition> ccs = sqlObj.getCompareConds() == null ? new ArrayList<>() : sqlObj.getCompareConds();
			ccs.add(new CompareCondition(key, val, 4));
			sqlObj.setCompareConds(ccs);
			return this;
		}
		
		public SQLObject build() {
			return sqlObj;
		}
	}

	/**
	 * internal use
	 *
	 */
	public static class SQLObject {

		/**
		 * 对于 select a,b,c from table，targets表示a,b,c 如果为null，则表示*
		 * https://www.w3school.com.cn/sql/sql_syntax.asp
		 */
		private List<String> targets;

		/**
		 * SQL语法的where条件，只对String类型有效 如 select * from table where a = '%Value%'
		 * https://www.w3school.com.cn/sql/sql_syntax.asp
		 */
		private List<EqualCondition> equalConds;
		
		/**
		 * SQL语法的where条件，只对String类型有效 如 select * from table where a = '%Value%'
		 * https://www.w3school.com.cn/sql/sql_syntax.asp
		 */
		private List<LikeCondition> likeConds;
		

		/**
		 * SQL语法的where条件，只对String、long、int和boolan类型有效 比大小，如0是等于，1是大于，2是大于等于，3是小于，4是小于等于
		 * https://www.w3school.com.cn/sql/sql_syntax.asp
		 */
		private List<CompareCondition> compareConds;

		/**
		 * TODO 对结果升序和降序排列
		 */
		private SortCondition sortConds;

		/**
		 * 如来自 湖南 的考生有30万人，如果查湖南，则涉及湖南的 数据行数有30万行，但如果只需要1行，则设置为true 是否去重
		 * https://www.w3school.com.cn/sql/sql_syntax.asp
		 */
		private boolean distinct = false;

		public boolean isDistinct() {
			return distinct;
		}

		public void setDistinct(boolean distinct) {
			this.distinct = distinct;
		}

		public SortCondition getSortConds() {
			return sortConds;
		}

		public void setSortConds(SortCondition sortConds) {
			this.sortConds = sortConds;
		}

		public List<String> getTargets() {
			return targets;
		}

		public void setTargets(List<String> targets) {
			this.targets = targets;
		}

		public List<LikeCondition> getLikeConds() {
			return likeConds;
		}

		public void setLikeConds(List<LikeCondition> likeConds) {
			this.likeConds = likeConds;
		}

		public List<CompareCondition> getCompareConds() {
			return compareConds;
		}

		public void setCompareConds(List<CompareCondition> compareConds) {
			this.compareConds = compareConds;
		}

		
		public List<EqualCondition> getEqualConds() {
			return equalConds;
		}

		public void setEqualConds(List<EqualCondition> equalConds) {
			this.equalConds = equalConds;
		}


		static class EqualCondition {

			protected String name;

			protected Object value;

			public EqualCondition() {
				super();
			}

			public EqualCondition(String name, Object value) {
				super();
				this.name = name;
				this.value = value;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public Object getValue() {
				return value;
			}

			public void setValue(Object value) {
				this.value = value;
			}

		}
		
		static class LikeCondition extends EqualCondition {

			public LikeCondition() {
				super();
			}

			public LikeCondition(String name, Object value) {
				super(name, value);
			}

		}

		static class CompareCondition extends EqualCondition {

			/**
			 * 0, equal, 1 great, 2 great and equal, 3. less 4 less equal
			 */
			protected int type = 0;

			public CompareCondition() {
				super();
			}

			public CompareCondition(String name, Object value, int type) {
				super(name, value);
				this.type = type;
			}

			public int getType() {
				return type;
			}

			public void setType(int type) {
				this.type = type;
			}

		}

		static class SortCondition {

			protected String name;

			protected boolean asc;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public boolean isAsc() {
				return asc;
			}

			public void setAsc(boolean asc) {
				this.asc = asc;
			}
		}
	}
}
