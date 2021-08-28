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
import io.github.kubesys.datafrk.core.items.ItemTypeBuilder;
import io.github.kubesys.datafrk.core.operators.CheckDatabase;
import io.github.kubesys.datafrk.core.operators.CheckTable;
import io.github.kubesys.datafrk.core.operators.CreateDatabase;
import io.github.kubesys.datafrk.core.operators.CreateTable;
import io.github.kubesys.datafrk.core.operators.CreateTableBuilder;
import io.github.kubesys.datafrk.core.operators.DropDatabase;
import io.github.kubesys.datafrk.core.operators.DropTable;
import io.github.kubesys.datafrk.core.operators.InsertData;
import io.github.kubesys.datafrk.core.operators.InsertDataBuilder;
import io.github.kubesys.datafrk.core.operators.QueryData;
import io.github.kubesys.datafrk.core.operators.QueryDataBuilder;
import io.github.kubesys.datafrk.core.operators.RemoveData;
import io.github.kubesys.datafrk.core.operators.RemoveDataBuilder;
import io.github.kubesys.datafrk.core.operators.UpdateData;
import io.github.kubesys.datafrk.core.operators.UpdateDataBuilder;
import io.github.kubesys.kubeclient.KubernetesClient;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.1
 *
 */
public class SQLMapper {

	public static final Logger m_logger = Logger.getLogger(SQLMapper.class.getName());

	public static final Map<String, Map<String,String>> DEF_VALUES = new HashMap<>();
	
	public static final Map<String, String> DEF_POSTGRES_VALUES    = new HashMap<>();
	
	public static final Map<String, String> DEF_MYSQL_VALUES       = new HashMap<>();

	public static final String DEFAULT_POSTGRES_TYPE               = "postgres";
	
	public static final String DEFAULT_MYSQL_TYPE                  = "mysql";
	
	public static final String DATABASE_TYPE                       = System.getenv("jdbcType") != null ? System.getenv("jdbcType") : DEFAULT_POSTGRES_TYPE;
	
	static {
		DEF_POSTGRES_VALUES.put("defaultDriver", "org.postgresql.Driver");
		DEF_POSTGRES_VALUES.put("defaultPrefix", "jdbc:postgresql://");
		DEF_POSTGRES_VALUES.put("defaultHost", "kube-database.kube-system");
		DEF_POSTGRES_VALUES.put("defaultPort", "5432");
		DEF_POSTGRES_VALUES.put("defaultDB", "postgres");
		DEF_POSTGRES_VALUES.put("defaultUser", "postgres");
		DEF_POSTGRES_VALUES.put("defaultPWD", "onceas");
		DEF_POSTGRES_VALUES.put("classname", "io.github.kubesys.datafrk.postgres.PostgresDataContext");
		DEF_POSTGRES_VALUES.put("tableBuilder", "io.github.kubesys.datafrk.postgres.operators.CreatePostgresTableBuilder");
		DEF_POSTGRES_VALUES.put("queryBuilder", "io.github.kubesys.datafrk.postgres.operators.QueryPostgresDataBuilder");
		DEF_POSTGRES_VALUES.put("checkDatabase", "io.github.kubesys.datafrk.postgres.operators.CheckPostgresDatabase");
		DEF_POSTGRES_VALUES.put("createDatabase", "io.github.kubesys.datafrk.postgres.operators.CreatePostgresDatabase");
		DEF_POSTGRES_VALUES.put("dropDatabase", "io.github.kubesys.datafrk.postgres.operators.DropPostgresDatabase");
		DEF_POSTGRES_VALUES.put("checkTable", "io.github.kubesys.datafrk.postgres.operators.CheckPostgresTable");
		DEF_POSTGRES_VALUES.put("dropTable", "io.github.kubesys.datafrk.postgres.operators.DropPostgresTable");
		DEF_POSTGRES_VALUES.put("insertObject", "io.github.kubesys.datafrk.postgres.operators.InsertPostgresDataBuilder");
		DEF_POSTGRES_VALUES.put("updateObject", "io.github.kubesys.datafrk.postgres.operators.UpdatePostgresDataBuilder");
		DEF_POSTGRES_VALUES.put("deleteObject", "io.github.kubesys.datafrk.postgres.operators.RemovePostgresDataBuilder");
		
		DEF_MYSQL_VALUES.put("defaultDriver", "com.mysql.cj.jdbc.Driver");
		DEF_MYSQL_VALUES.put("defaultPrefix", "jdbc:mysql://");
		DEF_MYSQL_VALUES.put("defaultHost", "kube-database.kube-system");
		DEF_MYSQL_VALUES.put("defaultPort", "3306");
		DEF_MYSQL_VALUES.put("defaultDB", "mysql");
		DEF_MYSQL_VALUES.put("defaultUser", "root");
		DEF_MYSQL_VALUES.put("defaultPWD", "onceas");
		DEF_MYSQL_VALUES.put("classname", "io.github.kubesys.datafrk.mysql.MysqlDataContext");
		DEF_MYSQL_VALUES.put("tableBuilder", "io.github.kubesys.datafrk.mysql.operators.CreateMysqlTableBuilder");
		DEF_MYSQL_VALUES.put("queryBuilder", "io.github.kubesys.datafrk.mysql.operators.QueryMysqlDataBuilder");
		DEF_MYSQL_VALUES.put("checkDatabase", "io.github.kubesys.datafrk.postgres.operators.CheckMysqlDatabase");
		DEF_MYSQL_VALUES.put("createDatabase", "io.github.kubesys.datafrk.postgres.operators.CreateMysqlDatabase");
		DEF_MYSQL_VALUES.put("dropDatabase", "io.github.kubesys.datafrk.postgres.operators.DropMysqlDatabase");
		DEF_MYSQL_VALUES.put("checkTable", "io.github.kubesys.datafrk.postgres.operators.CheckMysqlTable");
		DEF_MYSQL_VALUES.put("dropTable", "io.github.kubesys.datafrk.postgres.operators.DropMysqlTable");
		DEF_MYSQL_VALUES.put("insertObject", "io.github.kubesys.datafrk.mysql.operators.InsertMysqlDataBuilder");
		DEF_MYSQL_VALUES.put("updateObject", "io.github.kubesys.datafrk.mysql.operators.UpdateMysqlDataBuilder");
		DEF_MYSQL_VALUES.put("deleteObject", "io.github.kubesys.datafrk.mysql.operators.RemoveMysqlDataBuilder");
		
		DEF_VALUES.put(DEFAULT_POSTGRES_TYPE, DEF_POSTGRES_VALUES);
		DEF_VALUES.put(DEFAULT_MYSQL_TYPE, DEF_MYSQL_VALUES);
	}
	
	protected final DataContext context;
	
	protected final KubernetesClient kubeClient;
	
	/****************************************************************************
	 * 
	 * 
	 *                         Init DataContext
	 * 
	 * 
	 *****************************************************************************/
	
	public SQLMapper(KubernetesClient kubeClient) throws Exception {
		this(kubeClient, createDataContext());
	}
	
	public SQLMapper(KubernetesClient kubeClient, DataContext context) {
		super();
		this.context = context;
		this.kubeClient = kubeClient;
	}

	public void close() throws Exception {
		this.context.currentDatabase().close();
	}

	
	private static String realUrl(String oldUrl, String database) {
		int stx = oldUrl.indexOf("/", filterPrefix(oldUrl));
		int etx = oldUrl.indexOf("?");
		return (etx == - 1) ? oldUrl.substring(0, stx + 1) + database :
				oldUrl.substring(0, stx + 1) + database + oldUrl.substring(etx);
	}

	
	private static int filterPrefix(String oldUrl) {
		int idx = oldUrl.indexOf("://");
		return idx + "://".length() + 1;
	}
	
	private static DataContext createDataContext() throws Exception {
		Properties props = createProperties();
		
		DataContext fake = createDataContext(props);
		String db = getValue(System.getenv("jdbcDB"), "kube");
		if (!fake.checkDababase(createCheckDatabase(db))) {
			fake.createDatabase(createCreateDatabase(db));
		}
		fake.currentDatabase().close();
		
		props.put("druid.url", realUrl(props.getProperty("druid.url"), db)); 
		return createDataContext(props);
	}
	
	private static DataContext createDataContext(Properties props) {
		try {
			Class<?> clz = Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("classname"));
			Constructor<?> c = clz.getConstructor(Properties.class);
			return (DataContext) c.newInstance(props);
		} catch (Exception ex) {
			m_logger.severe(ex.toString());
			return null;
		}
	}
	
	private static CheckDatabase createCheckDatabase(String db) {
		try {
			Class<?> clz = Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("checkDatabase"));
			Constructor<?> c = clz.getConstructor(String.class);
			return (CheckDatabase) c.newInstance(db);
		} catch (Exception ex) {
			m_logger.severe(ex.toString());
			return null;
		}
	}
	
	private static CreateDatabase createCreateDatabase(String db) {
		try {
			Class<?> clz = Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("createDatabase"));
			Constructor<?> c = clz.getConstructor(String.class);
			return (CreateDatabase) c.newInstance(db);
		} catch (Exception ex) {
			m_logger.severe(ex.toString());
			return null;
		}
	}
	
	public KubernetesClient getKubeClient() {
		return kubeClient;
	}

	/****************************************************************************
	 * 
	 * 
	 *                        Properties
	 * 
	 * 
	 *****************************************************************************/
	
	public static Properties createProperties() {
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
	
	private static String getDriver() {
		return getValue(System.getenv("jdbcDriver"), DEF_VALUES.get(DATABASE_TYPE).get("defaultDriver"));
	}
	
	private static String getUrl() {
		return DEF_VALUES.get(DATABASE_TYPE).get("defaultPrefix") 
				+ getValue(System.getenv("jdbcHost"), DEF_VALUES.get(DATABASE_TYPE).get("defaultHost")) + ":"
				+ getValue(System.getenv("jdbcPort"), DEF_VALUES.get(DATABASE_TYPE).get("defaultPort")) + "/"
				+ DEF_VALUES.get(DATABASE_TYPE).get("defaultDB") 
				+ "?useUnicode=true&characterEncoding=UTF8&connectTimeout=2000&socketTimeout=6000&autoReconnect=true&&serverTimezone=Asia/Shang";
	}
	
	private static String getUser() {
		return getValue(System.getenv("jdbcUser"), DEF_VALUES.get(DATABASE_TYPE).get("defaultUser"));
	}
	
	private static String getPassword() {
		return getValue(System.getenv("jdbcPassword"), DEF_VALUES.get(DATABASE_TYPE).get("defaultPWD"));
	}
	
	private static String getValue(String inputValue, String defValue) {
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
	@SuppressWarnings("unchecked")
	public boolean insertObject(String table, String name, String namespace, String group, String created, String updated, String json) throws Exception {
		InsertDataBuilder<?, InsertData> builder = (InsertDataBuilder<?, InsertData>) Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("insertObject")).newInstance();
		builder = (InsertDataBuilder<?, InsertData>) builder.insertTo(table);
		builder = (InsertDataBuilder<?, InsertData>) builder.beginValue(name);
		builder = (InsertDataBuilder<?, InsertData>) builder.andValue(namespace);
		builder = (InsertDataBuilder<?, InsertData>) builder.andValue(group);
		builder = (InsertDataBuilder<?, InsertData>) builder.andValue(created);
		builder = (InsertDataBuilder<?, InsertData>) builder.andValue(updated);
		builder = (InsertDataBuilder<?, InsertData>) builder.endValue(json, true);
		if (!context.currentDatabase().get(table).insert(builder.build())) {
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
	@SuppressWarnings("unchecked")
	public boolean updateObject(String table, String name, String namespace, String group, String updated, String json) throws Exception {
		UpdateDataBuilder<?, UpdateData> builder = (UpdateDataBuilder<?, UpdateData>) Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("updateObject")).newInstance();
		builder = (UpdateDataBuilder<?, UpdateData>) builder.update(table);
		builder = (UpdateDataBuilder<?, UpdateData>) builder.set("updated");
		builder = (UpdateDataBuilder<?, UpdateData>) builder.eq(updated);
		builder = (UpdateDataBuilder<?, UpdateData>) builder.andSet("data");
		builder = (UpdateDataBuilder<?, UpdateData>) builder.eq(json, true);
		builder = (UpdateDataBuilder<?, UpdateData>) builder.where("name");
		builder = (UpdateDataBuilder<?, UpdateData>) builder.eq(name);
		builder = (UpdateDataBuilder<?, UpdateData>) builder.and("namespace");
		builder = (UpdateDataBuilder<?, UpdateData>) builder.eq(namespace);
		builder = (UpdateDataBuilder<?, UpdateData>) builder.and("apigroup");
		builder = (UpdateDataBuilder<?, UpdateData>) builder.eq(group);
		return context.currentDatabase().get(table).update(builder.build());
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
	@SuppressWarnings("unchecked")
	public boolean deleteObject(String table, String name, String namespace, String group, String json) throws Exception {
		RemoveDataBuilder<?, RemoveData> builder = (RemoveDataBuilder<?, RemoveData>) Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("deleteObject")).newInstance();
		builder = (RemoveDataBuilder<?, RemoveData>) builder.delete(table);
		builder = (RemoveDataBuilder<?, RemoveData>) builder.where("name");
		builder = (RemoveDataBuilder<?, RemoveData>) builder.eq(name);
		builder = (RemoveDataBuilder<?, RemoveData>) builder.and("namespace");
		builder = (RemoveDataBuilder<?, RemoveData>) builder.eq(namespace);
		builder = (RemoveDataBuilder<?, RemoveData>) builder.where("apigroup");
		builder = (RemoveDataBuilder<?, RemoveData>) builder.eq(group);
		return context.currentDatabase().get(table).delete(builder.build());
	}

	public boolean createDatbase(String db) throws Exception {
		CreateDatabase cdb = (CreateDatabase) Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("createDatabase")).newInstance();
		return context.createDatabase(cdb);
	}

	public boolean dropDatabase(String db) throws Exception {
		DropDatabase ddb = (DropDatabase) Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("dropDatabase")).newInstance();
		return context.dropDababase(ddb);
	}

	public boolean checkDatabase(String db) throws Exception {
		CheckDatabase cdb = (CheckDatabase) Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("checkDatabase")).newInstance();
		return context.checkDababase(cdb);
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

	public boolean dropTable(String table) throws Exception {
		Constructor<?> c = Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("dropTable")).getConstructor(String.class);
		DropTable dt = (DropTable) c.newInstance(table);
		return context.currentDatabase().dropTable(dt);
	}

	public boolean checkTable(String table) throws Exception {
		Constructor<?> c = Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("checkTable")).getConstructor(String.class);
		CheckTable ct = (CheckTable) c.newInstance(table);
		return context.currentDatabase().checkTable(ct);
	}
	
	/****************************************************************************
	 * 
	 * 
	 *                         query
	 * 
	 * 
	 *****************************************************************************/

	public JsonNode query(String table, String kind, int limit, int page, Map<String, String> labels) throws Exception {
		ObjectNode node = new ObjectMapper().createObjectNode();
		node.put("kind", kind + "List");
		node.put("apiVersion", "v1");
		ObjectNode meta = new ObjectMapper().createObjectNode();
		{
			meta.put("totalCount", getCount(table));
			meta.put("continue", String.valueOf(page + 1));
		}
		node.set("metadata", meta);
		node.set("items", getItems(table, limit, page, labels));
		return node;
	}
	
	@SuppressWarnings("unchecked")
	private int getCount(String table) throws Exception {
		QueryDataBuilder<?, QueryData> queryBuilder = (QueryDataBuilder<?, QueryData>) 
				Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("queryBuilder")).newInstance();
		
		QueryData queryData = ((QueryDataBuilder<?, QueryData>) 
				queryBuilder.selectCount(table)).build();
		
		ResultSet rs = (ResultSet) context.currentDatabase()
				.get(table).query(new QueryData(queryData.toSQL()));
		
		rs.next();
		
		return rs.getInt("count");
	}
	
	@SuppressWarnings("unchecked")
	private ArrayNode getItems(String table, int limit, int page, Map<String, String> labels) throws Exception {
		
		QueryDataBuilder<?, QueryData> queryBuilder = (QueryDataBuilder<?, QueryData>) 
				Class.forName(DEF_VALUES.get(DATABASE_TYPE).get("queryBuilder")).newInstance();
		
		queryBuilder = (QueryDataBuilder<?, QueryData>) queryBuilder.selectAll(table);
		
		int i = 0;
		for (String key : labels.keySet()) {
			if (i == 0) {
				queryBuilder = (QueryDataBuilder<?, QueryData>) queryBuilder.where(key, true);
				queryBuilder = (QueryDataBuilder<?, QueryData>) queryBuilder.like(labels.get(key));
			} else {
				queryBuilder = (QueryDataBuilder<?, QueryData>) queryBuilder.and(key, true);
				queryBuilder = (QueryDataBuilder<?, QueryData>) queryBuilder.like(labels.get(key));
			}
			++i;
		}
		queryBuilder = (QueryDataBuilder<?, QueryData>) queryBuilder.orderBy("updated", true);
		queryBuilder = (QueryDataBuilder<?, QueryData>) queryBuilder.limit(limit, page);
		
		
		ResultSet rsd = (ResultSet) context.currentDatabase().get(table).query(queryBuilder.build());
		
		ArrayNode items = new ObjectMapper().createArrayNode();
		
		while (rsd.next()) {
			items.add(new ObjectMapper().readTree(rsd.getString("data")));
		}
		rsd.close();
		return items;
	}

}
