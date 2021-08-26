/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.kubesys.datafrk.core.DataContext;
import io.github.kubesys.datafrk.core.Table;
import io.github.kubesys.datafrk.core.items.ItemTypeBuilder;
import io.github.kubesys.datafrk.core.operators.CreateTable;
import io.github.kubesys.datafrk.core.operators.CreateTableBuilder;
import io.github.kubesys.datafrk.core.operators.QueryData;
import io.github.kubesys.datafrk.core.operators.QueryDataBuilder;
import io.github.kubesys.datafrk.druid.DruidDataContext;
import io.github.kubesys.datafrk.postgres.operators.CheckPostgresDatabase;
import io.github.kubesys.datafrk.postgres.operators.CheckPostgresTable;
import io.github.kubesys.datafrk.postgres.operators.CreatePostgresDatabase;
import io.github.kubesys.datafrk.postgres.operators.DropPostgresDatabase;
import io.github.kubesys.datafrk.postgres.operators.DropPostgresTable;
import io.github.kubesys.datafrk.postgres.operators.InsertPostgresDataBuilder;
import io.github.kubesys.datafrk.postgres.operators.RemovePostgresDataBuilder;
import io.github.kubesys.datafrk.postgres.operators.UpdatePostgresDataBuilder;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.1
 *
 */
public class SQLMapper {

	private static final String SELECT                 = "SELECT #TARGET# FROM #TABLE#";

	private static final String WHERE                  = " WHERE ";

	
	private static final String POSTGRE_CONDITION      = " data#ITEM# like '%#VALUE#%' AND ";

	private static final String POSTGRE_LIMIT          = " LIMIT #LIMIT# OFFSET #OFFSET#";
	
	
	public static final Logger m_logger = Logger.getLogger(SQLMapper.class.getName());

	public static final Map<String, Map<String,String>> DEF_VALUES = new HashMap<>();
	
	public static final Map<String, String> DEF_POSTGRES_VALUES    = new HashMap<>();
	
	public static final Map<String, String> DEF_MYSQL_VALUES       = new HashMap<>();

	public static final String DEFAULT_POSTGRES_TYPE               = "postgres";
	
	public static final String DEFAULT_MYSQL_TYPE                  = "mysql";
	
	public static final String LABEL_NAME                          = "name";
	
	public static final String LABEL_NAMESPACE                     = "namespace";
	
	public static final String LABEL_APIGROUP                      = "apigroup";
	
	public static final String LABEL_CREATED                       = "created";
	
	public static final String LABEL_UPDATED                       = "updated";
	
	public static final String LABEL_DATA                          = "data";
	
	public static final String DATABASE_TYPE                       = System.getenv("jdbcType") != null ? System.getenv("jdbcType") : DEFAULT_POSTGRES_TYPE;
	
	static {
		DEF_POSTGRES_VALUES.put("driver", "org.postgresql.Driver");
		DEF_POSTGRES_VALUES.put("prefix", "jdbc:postgresql://");
		DEF_POSTGRES_VALUES.put("host", "kube-database.kube-system");
		DEF_POSTGRES_VALUES.put("port", "5432");
		DEF_POSTGRES_VALUES.put("db", "postgres");
		DEF_POSTGRES_VALUES.put("user", "postgres");
		DEF_POSTGRES_VALUES.put("pwd", "onceas");
		DEF_POSTGRES_VALUES.put("classname", "io.github.kubesys.datafrk.postgres.PostgresDataContext");
		DEF_POSTGRES_VALUES.put("tableBuilder", "io.github.kubesys.datafrk.postgres.operators.CreatePostgresTableBuilder");
		DEF_POSTGRES_VALUES.put("queryBuilder", "io.github.kubesys.datafrk.postgres.operators.QueryPostgresDataBuilder");
		
		DEF_MYSQL_VALUES.put("driver", "com.mysql.cj.jdbc.Driver");
		DEF_MYSQL_VALUES.put("prefix", "jdbc:mysql://");
		DEF_MYSQL_VALUES.put("host", "kube-database.kube-system");
		DEF_MYSQL_VALUES.put("port", "3306");
		DEF_MYSQL_VALUES.put("db", "mysql");
		DEF_MYSQL_VALUES.put("user", "root");
		DEF_MYSQL_VALUES.put("pwd", "onceas");
		DEF_MYSQL_VALUES.put("classname", "io.github.kubesys.datafrk.mysql.MysqlDataContext");
		DEF_MYSQL_VALUES.put("tableBuilder", "io.github.kubesys.datafrk.mysql.operators.CreateMysqlTableBuilder");
		DEF_MYSQL_VALUES.put("queryBuilder", "io.github.kubesys.datafrk.mysql.operators.QueryMysqlDataBuilder");
		
		DEF_VALUES.put(DEFAULT_POSTGRES_TYPE, DEF_POSTGRES_VALUES);
		DEF_VALUES.put(DEFAULT_MYSQL_TYPE, DEF_MYSQL_VALUES);
	}
	
	protected final DataContext context;
	
	
	public SQLMapper() {
		this.context = createDataContext();
	}
	
	public SQLMapper(DruidDataContext context) {
		super();
		this.context = context;
	}

	public void close() throws Exception {
		this.context.currentDatabase().close();
	}

	/****************************************************************************
	 * 
	 * 
	 *                         Init DataContext
	 * 
	 * 
	 *****************************************************************************/
	
	private DataContext createDataContext() {
		try {
			Class<?> clz = Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("classname"));
			Constructor<?> c = clz.getConstructor(Properties.class);
			return (DataContext) c.newInstance(createProperties());
		} catch (Exception ex) {
			m_logger.severe(ex.toString());
			return null;
		}
	}
	
	private Properties createProperties() {
		Properties props = new Properties();
		props.put("druid.driverClassName", getDriver()); 
		props.put("druid.url", getUrl());
		props.put("druid.username",getUser()); 
		props.put("druid.password", getPassword());
		props.put("druid.initialSize", 10); 
		props.put("druid.maxActive", 100);
		props.put("druid.maxWait", 0);
		return props;
	}
	
	private String getDriver() {
		return getValue(System.getenv("jdbcDriver"), DEF_VALUES.get(DATABASE_TYPE).get("driver"));
	}
	
	private String getUrl() {
		return DEF_VALUES.get(DATABASE_TYPE).get("prefix") 
				+ getValue(System.getenv("jdbcHost"), DEF_VALUES.get(DATABASE_TYPE).get("host")) + ":"
				+ getValue(System.getenv("jdbcPort"), DEF_VALUES.get(DATABASE_TYPE).get("port")) + "/"
				+ getValue(System.getenv("jdbcDB"), DEF_VALUES.get(DATABASE_TYPE).get("db")) 
				+ "?useUnicode=true&characterEncoding=UTF8&connectTimeout=2000&socketTimeout=6000&autoReconnect=true&&serverTimezone=Asia/Shang";
	}
	
	private String getUser() {
		return getValue(System.getenv("jdbcUser"), DEF_VALUES.get(DATABASE_TYPE).get("user"));
	}
	
	private String getPassword() {
		return getValue(System.getenv("jdbcPassword"), DEF_VALUES.get(DATABASE_TYPE).get("pwd"));
	}
	
	private String getValue(String inputValue, String defValue) {
		return (inputValue == null) ? defValue : inputValue;
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
	
	@SuppressWarnings("unchecked")
	public boolean createTable(String table) throws Exception {
		CreateTableBuilder<?, CreateTable> tableBuilder = (CreateTableBuilder<?, CreateTable>) Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("tableBuilder")).newInstance();
		ItemTypeBuilder typeBuilder =  tableBuilder.getTypeBuilder();
		tableBuilder = (CreateTableBuilder<?, CreateTable>) tableBuilder.createTable(table);
		tableBuilder = (CreateTableBuilder<?, CreateTable>)  tableBuilder.addItem("name",      typeBuilder.varchar(512), true);
		tableBuilder = (CreateTableBuilder<?, CreateTable>)  tableBuilder.addItem("namespace", typeBuilder.varchar(128), true);
		tableBuilder = (CreateTableBuilder<?, CreateTable>)  tableBuilder.addItem("apigroup",  typeBuilder.varchar(128), true);
		tableBuilder = (CreateTableBuilder<?, CreateTable>)  tableBuilder.addItem("created",   typeBuilder.datatime());
		tableBuilder = (CreateTableBuilder<?, CreateTable>)  tableBuilder.addItem("updated",   typeBuilder.datatime());
		tableBuilder = (CreateTableBuilder<?, CreateTable>)  tableBuilder.addItem("data",      typeBuilder.json());							
		tableBuilder = (CreateTableBuilder<?, CreateTable>)  tableBuilder.endCreate();						
		return context.currentDatabase().createTable(tableBuilder.build());
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
		
		Table<?> thisTable = context.currentDatabase().get(table);
		ObjectNode node = new ObjectMapper().createObjectNode();
		node.put("kind", kind + "List");
		node.put("apiVersion", "v1");
		ObjectNode meta = new ObjectMapper().createObjectNode();
		{
			String classname = DEF_VALUES.get(DATABASE_TYPE).get("queryBuilder");
			meta.put("totalCount", getCount(table, thisTable, classname));
			meta.put("continue", String.valueOf(page + 1));
		}
		node.set("metadata", meta);
		node.set("items", getItems(table, limit, page, sqlBase));
		return node;
	}

	@SuppressWarnings("unchecked")
	private int getCount(String table, Table<?> thisTable, String classname) throws Exception {
		QueryDataBuilder<?, QueryData> qdb = (QueryDataBuilder<?, QueryData>) Class.forName(classname).newInstance();
		qdb = (QueryDataBuilder<?, QueryData>) qdb.selectCount(table);
		QueryData queryData = qdb.build();
		ResultSet rs = (ResultSet) thisTable.query(new QueryData(queryData.toSQL()));
		rs.next();
		return rs.getInt("count");
	}
	
	private ArrayNode getItems(String table, int limit, int page, StringBuilder sqlBase) throws Exception {
		
		sqlBase.append(" order by updated desc ").append(queryLimit(limit, page));
		String dataSql = sqlBase.toString().replace("#TARGET#", "*");
		ResultSet rsd = (ResultSet) context.currentDatabase().get(table).query(new QueryData(dataSql));
		ArrayNode items = new ObjectMapper().createArrayNode();
		
		while (rsd.next()) {
			items.add(new ObjectMapper().readTree(rsd.getString("data")));
		}
		rsd.close();
		return items;
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
