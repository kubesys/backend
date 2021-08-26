/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.kubesys.datafrk.core.operators.QueryData;
import io.github.kubesys.datafrk.druid.DruidDataContext;
import io.github.kubesys.datafrk.postgres.operators.CheckPostgresDatabase;
import io.github.kubesys.datafrk.postgres.operators.CheckPostgresTable;
import io.github.kubesys.datafrk.postgres.operators.CreatePostgresDatabase;
import io.github.kubesys.datafrk.postgres.operators.CreatePostgresTableBuilder;
import io.github.kubesys.datafrk.postgres.operators.DropPostgresDatabase;
import io.github.kubesys.datafrk.postgres.operators.DropPostgresTable;
import io.github.kubesys.datafrk.postgres.operators.InsertPostgresDataBuilder;
import io.github.kubesys.datafrk.postgres.operators.RemovePostgresDataBuilder;
import io.github.kubesys.datafrk.postgres.operators.UpdatePostgresDataBuilder;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.0
 *
 */
public class KubeSQLClient {

	public static final String CREATE_POSTGRE_TABLE    = "CREATE TABLE #TABLE# (name varchar(512), namespace varchar(128), apigroup varchar(128), created timestamp, updated timestamp, "
			+ "data json, primary key(name, namespace, apigroup))";
	
	public static final String LABEL_NAME      = "name";
	
	public static final String LABEL_NAMESPACE = "namespace";
	
	public static final String LABEL_APIGROUP  = "apigroup";
	
	public static final String LABEL_CREATED   = "created";
	
	public static final String LABEL_UPDATED   = "updated";
	
	public static final String LABEL_DATA      = "data";
	
	private static final String SELECT                 = "SELECT #TARGET# FROM #TABLE#";

	private static final String WHERE                  = " WHERE ";

	private static final String TARGET_DATA            = "*";

	private static final String TARGET_COUNT           = "count(*) as count";
	
	private static final String METADATA               = "metadata";
	
	private static final String POSTGRE_CONDITION      = " data#ITEM# like '%#VALUE#%' AND ";

	private static final String POSTGRE_LIMIT          = " LIMIT #LIMIT# OFFSET #OFFSET#";
	
	protected final DruidDataContext context;
	
	public KubeSQLClient(DruidDataContext context) {
		super();
		this.context = context;
	}

	public void close() throws Exception {
		this.context.currentDatabase().close();
	}
	
	/****************************************************************************
	 * 
	 * 
	 *                         Insert, Update, Delete objects
	 * 
	 * 
	 *****************************************************************************/
	/**
	 * @param table                                  table
	 * @param name                                   name
	 * @param namespace                              namespace
	 * @param group                                  group
	 * @param created                                created
	 * @param updated                                updated
	 * @param json                                   json
	 * @return                                       true or false
	 * @throws Exception                             exception
	 */
	public boolean insertObject(String table, String name, String namespace, String group, String created, String updated, String json) throws Exception {
		if (!context.currentDatabase().get(table).insert(
				new InsertPostgresDataBuilder()
				.insertTo(table)
				.beginValue(name)
				.andValue(namespace)
				.andValue(group)
				.andValue(created)
				.andValue(updated)
				.endValue(json, true)
				.build())) {
			return updateObject(table, name, namespace, group, updated, json);
		}
		return true;
	}
	
	/**
	 * @param table                                  table
	 * @param name                                   name
	 * @param namespace                              namespace
	 * @param group                                  group
	 * @param updated                                updated
	 * @param json                                   json
	 * @return                                       true or false
	 * @throws Exception                             exception
	 */
	public boolean updateObject(String table, String name, String namespace, String group, String updated, String json) throws Exception {
		return context.currentDatabase().get(table).update(
				new UpdatePostgresDataBuilder()
				.update(table)
				.set(LABEL_UPDATED).eq(updated)
				.andSet(LABEL_DATA).eq(json, true)
				.where(LABEL_NAME).eq(name)
				.and(LABEL_NAMESPACE).eq(namespace)
				.and(LABEL_APIGROUP).eq(group)
				.build());
	}
	
	/**
	 * @param table                                  table
	 * @param name                                   name
	 * @param namespace                              namespace
	 * @param group                                  group
	 * @param json                                   json
	 * @return                                       true or false
	 * @throws Exception                             exception
	 */
	public boolean deleteObject(String table, String name, String namespace, String group, String json) throws Exception {
		return context.currentDatabase().get(table).delete(
				new RemovePostgresDataBuilder()
				.delete(table)
				.where(LABEL_NAME).eq(name)
				.and(LABEL_NAMESPACE).eq(namespace)
				.and(LABEL_APIGROUP).eq(group)
				.build());
	}

	public boolean createDatbase(String db) throws Exception {
		return context.createDatabase(new CreatePostgresDatabase(db));
	}

	public boolean dropDatabase(String db) {
		return context.dropDababase(new DropPostgresDatabase(db));
	}

	public boolean checkDatabase(String db) {
		return context.checkDababase(new CheckPostgresDatabase(db));
	}
	
	public boolean createTable(String table) throws Exception {
		return context.currentDatabase().createTable(new CreatePostgresTableBuilder()
				.sql(CREATE_POSTGRE_TABLE.replace("#TABLE#", table)).build());
	}

	public boolean dropTable(String table) {
		return context.currentDatabase().dropTable(new DropPostgresTable(table));
	}

	public boolean checkTable(String table) {
		return context.currentDatabase().checkTable(new CheckPostgresTable(table));
	}
	
	/****************************************************************************
	 * 
	 * 
	 *                         query
	 * 
	 * 
	 *****************************************************************************/

	public JsonNode query(String table, String kind, int limit, int page, Map<String, String> labels) throws Exception {
		StringBuilder sqlBase = createSqlBase(table, labels);
		return getResults(table, kind, limit, page, sqlBase);
	}
	
	private ObjectNode getResults(String table, String kind, int limit, int page, StringBuilder sqlBase) throws Exception {
		
		ObjectNode node = new ObjectMapper().createObjectNode();
		node.put("kind", kind + "List");
		node.put("apiVersion", "v1");
		ObjectNode meta = new ObjectMapper().createObjectNode();
		{
			meta.put("totalCount", getRows(table, sqlBase));
			meta.put("continue", String.valueOf(page + 1));
		}
		node.set(METADATA, meta);
		node.set("items", getItems(table, limit, page, sqlBase));
		return node;
	}
	
	private ArrayNode getItems(String table, int limit, int page, StringBuilder sqlBase) throws Exception {
		
		sqlBase.append(" order by updated desc ").append(queryLimit(limit, page));
		String dataSql = sqlBase.toString().replace("#TARGET#", TARGET_DATA);
		ResultSet rsd = (ResultSet) context.currentDatabase().get(table).query(new QueryData(dataSql));
		ArrayNode items = new ObjectMapper().createArrayNode();
		
		while (rsd.next()) {
			items.add(new ObjectMapper().readTree(rsd.getString("data")));
		}
		rsd.close();
		return items;
	}

	private int getRows(String table, StringBuilder sqlBase) throws Exception, SQLException {
		ResultSet rsc = (ResultSet) context.currentDatabase().get(table).query(
				new QueryData(sqlBase.toString().replace("#TARGET#", TARGET_COUNT)));
				
		rsc.next();
		
		int total = rsc.getInt("count");
		rsc.close();
		return total;
	}

	private StringBuilder createSqlBase(String table, Map<String, String> labels) {
		StringBuilder sqlBase = new StringBuilder();

		sqlBase.append(SELECT.replace("#TABLE#", table));
		
		if (labels != null && !labels.isEmpty()) {
			
			sqlBase.append(WHERE);
			for (String key : labels.keySet()) {
				
				StringBuilder sb = new StringBuilder();
				String[] items = key.split("#");
				int size = items.length;
				for (int i = 0; i < size; i++) {
					if (i != size - 1) {
						sb.append("->'" + items[i] + "'");
					} else {
						sb.append("->>'" + items[i] + "'");
					}
				}
				
				sqlBase.append(queryConditions().replace("#ITEM#", sb.toString()).replace("#VALUE#", labels.get(key)));
			}

			sqlBase.delete(sqlBase.length() - 4, sqlBase.length());
		}
		
		return sqlBase;
	}
	
	public String queryConditions() {
		return POSTGRE_CONDITION;
	}

	public String queryLimit(int limit, int page) {
		int l = (limit <= 0) ? 10 : limit;
		int p = (page <= 1) ? 1 : page;
		return POSTGRE_LIMIT.replace("#LIMIT#", String.valueOf(limit)).replace("#OFFSET#", String.valueOf((p - 1) * l));
	}
}