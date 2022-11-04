/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.client.KubernetesClient;
import io.github.kubesys.client.KubernetesConstants;
import io.github.kubesys.client.KubernetesWatcher;
import io.github.kubesys.client.core.KubernetesRuleBase;


/**
 * @author wuheng@iscas.ac.cn
 * @since  2.0.0
 * 
 * This class is used for extracting data from Kubernetes,
 * and store it to database.
 */
public class KubeMirror {

	/**
	 * m_logger
	 */
	public static final Logger m_logger = Logger.getLogger(KubeMirror.class.getName());

	/**
	 * sources
	 */
	public Set<String> kindChannels = new HashSet<>();

	/**
	 * kubeClient
	 */
	protected KubernetesClient fromKube;
	
	/**
	 * sql client
	 */
	protected SQLMapper toSQL;
	
	
	/**
	 * threads
	 */
	protected final Map<String, Thread> watchers = new HashMap<>();
	
	/****************************************************************************
	 * 
	 * 
	 *                         Insert, Update, Delete objects
	 * 
	 * 
	 *****************************************************************************/
	
	public KubeMirror(KubernetesClient kubeClient) throws Exception {
		try {
			this.fromKube = kubeClient;
			this.toSQL = new SQLMapper();
		} catch (Exception ex) {
			m_logger.severe(ex.toString());
			System.exit(1);
		}
	}


	/**
	 * @throws Exception                    exception
	 */
	public void start() throws Exception {
		fromKuberbetes().toDatabase();
	}
	
	/**
	 * @param  kind                         kind
	 * @throws Exception                    exception
	 */
	public void start(String kind) throws Exception {
		fromKuberbetes(kind).toDatabase();
	}
	
	/**
	 * @return                               mirror
	 * @throws Exception                     exception
	 */
	public KubeMirror fromKuberbetes() throws Exception {
		try {
			KubernetesRuleBase ruleBase = this.fromKube.getAnalyzer()
										.getConvertor().getRuleBase();
			return fromSource(ruleBase.getFullKinds().values().toArray(new String[] {}));
		} catch (Exception ex) {
			m_logger.severe(ex.toString());
			System.exit(1);
		}

		return this;
	}
	
	/**
	 * @param  fullKind                      kind
	 * @return                               mirror
	 * @throws Exception                     exception
	 */
	public KubeMirror fromKuberbetes(String fullKind) throws Exception {
		return fromSource(new String[] {fullKind});
	}
	
	/**
	 * @param  fullkinds                     kinds
	 * @return                               mirror
	 * @throws Exception                     exception
	 */
	public KubeMirror fromSource(String[] fullkinds) throws Exception {
		
		for (String fullkind : fullkinds) {
			
			this.kindChannels.add(fullkind);
			
			String table = this.fromKube.getAnalyzer()
					.getConvertor().getRuleBase().getName(fullkind);

			createTableIfNeeds(fullkind, table);
			
		}
		return this;
	}


	private void createTableIfNeeds(String fullkind, String table) throws Exception {
		
		if (invalidTableName(table) || createdTable(table)) {
			return;
		}
		
		toSQL.createTable(table);
	}


	private boolean createdTable(String table) throws Exception {
		return toSQL.checkTable(table);
	}

	private boolean invalidTableName(String table) {
		return table.contains("/") || table.contains("-");
	}
	

	/**
	 * @return                              mirror
	 * @throws Exception                    exception
	 */
	protected KubeMirror toDatabase() throws Exception {
		for (String kind : kindChannels) {
			try {
				createKubeToSQLChannel(kind);
			} catch (Exception ex) {
				m_logger.severe(ex.toString());
				continue;
			}
		}
		return this;
	}

	/**
	 * @param kind                          kind
	 * @throws Exception                    exception
	 */
	protected void createKubeToSQLChannel(String kind) throws Exception {
		
		KubernetesRuleBase ruleBase = this.fromKube.getAnalyzer()
									.getConvertor().getRuleBase();
		
		String table = ruleBase.getName(kind);

		try {
			m_logger.info("starting Watcher " + kind);
			KubeToSQL toSQL = new KubeToSQL(kind, table, this);
			watchers.put(table, this.fromKube.watchResources(kind, toSQL));
			m_logger.info("Watcher " + kind + " is working");
		} catch (Exception ex) {
			m_logger.info("fail to start Watcher " + kind);
			m_logger.severe(ex.toString());
		}
	}

	
	/**
	 * @param table                   table
	 * @throws Exception              exception
	 */
	protected void deleteDataIfTableExit(String table) throws Exception {
		if (createdTable(table)) {
			toSQL.deleteData(table);
		}
	}
	
	/**
	 * @param kind                          kind
	 * @throws Exception                    exception
	 */
	@SuppressWarnings("deprecation")
	protected void stopWatcher(String kind) throws Exception {
		String table = this.fromKube.getAnalyzer().getConvertor().getRuleBase().getName(kind);
		if (createdTable(table)) {
			toSQL.dropTable(table);
		} 
		watchers.get(table).stop();
		watchers.remove(table);
	}
	
	
	public void addSource(String kind) {
		if (!kindChannels.contains(kind)) {
			kindChannels.add(kind);
			try {
				createKubeToSQLChannel(kind);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deleteSource(String kind) {
		if (kindChannels.contains(kind)) {
			kindChannels.remove(kind);
			try {
				stopWatcher(kind);
			} catch (Exception e) {
			}
		}
	}

	public boolean beenWatched(String kind) {
		return watchers.containsKey(this.fromKube
				.getAnalyzer().getConvertor().getRuleBase().getName(kind));
	}

	public KubernetesClient getKubeClient() {
		return this.fromKube;
	}

	public SQLMapper getSqlClient() {
		return toSQL;
	}
	
	
	/**
	 * @author wuheng
	 * @since 2019.4.20
	 */
	public static class KubeToSQL extends KubernetesWatcher {

		public static final Logger m_logger = Logger.getLogger(KubeToSQL.class.getName());

		/**
		 * kind
		 */
		protected final String kind;

		/**
		 * table
		 */
		protected final String table;

		/**
		 * sqlClient
		 */
		protected final KubeMirror mirror;
		
		public KubeToSQL(String kind, String table, KubeMirror kubeMirror) throws Exception {
			super(kubeMirror.getKubeClient());
			this.kind = kind;
			this.table = table;
			this.mirror = kubeMirror;
		}

		@Override
		public void doAdded(JsonNode json) {
			try {
				mirror.getSqlClient().insertObject(table, KubeUtils.getName(json),
						KubeUtils.getNamespace(json),
						getGroup(json),
						createDateTime(json),
						currentDateTime(),
						KubeUtils.getJsonWithoutAnotation(json));
				
				m_logger.info("insert object  " + json + " successfully.");
				
				if ("CustomResourceDefinition".equals(json.get("kind").asText())) {
					JsonNode spec = json.get(KubernetesConstants.KUBE_SPEC);
					JsonNode names = spec.get(KubernetesConstants.KUBE_SPEC_NAMES);
					String kind = names.get(KubernetesConstants.KUBE_SPEC_NAMES_KIND).asText();
					String group = spec.get("group").asText();
					mirror.addSource(group + "." + kind);
				}
				
			} catch (Exception e) {
				m_logger.severe("fail to insert object :" + json + " in table " + table + ". Becasue: " + e);
			}
			
		}

		@Override
		public void doModified(JsonNode json) {
			try {
				mirror.getSqlClient().updateObject(table, KubeUtils.getName(json),
						KubeUtils.getNamespace(json),
						getGroup(json),
						currentDateTime(),
						KubeUtils.getJsonWithoutAnotation(json));
				m_logger.info("update object  " + json + " successfully.");
			} catch (Exception e) {
				m_logger.severe("fail to update object :" + e);
			}
			
		}

		@Override
		public void doDeleted(JsonNode json) {
			try {
				
				mirror.getSqlClient().deleteObject(table, KubeUtils.getName(json),
						KubeUtils.getNamespace(json),
						getGroup(json),
						KubeUtils.getJsonWithoutAnotation(json));
				
				m_logger.info("delete object  " + json + " successfully.");
				
				if ("CustomResourceDefinition".equals(json.get("kind").asText())) {
					JsonNode spec = json.get(KubernetesConstants.KUBE_SPEC);
					JsonNode names = spec.get(KubernetesConstants.KUBE_SPEC_NAMES);
					String kind = names.get(KubernetesConstants.KUBE_SPEC_NAMES_KIND).asText();
					String group = spec.get("group").asText();
					mirror.deleteSource(group + "." + kind);
				}
				
			} catch (Exception e) {
				m_logger.severe("fail to delete object :" + e);
			}
			
		}

		@Override
		public void doClose() {
			try {
				client.watchResources(kind, new KubeToSQL(
						kind, table, mirror));
			} catch (Exception e) {
				System.exit(1);
			}
		}
		
		public static String getGroup (JsonNode json) {
			String apiVersion = json.get("apiVersion").asText();
			int idx = apiVersion.indexOf("/");
			return (idx == -1) ? "" : apiVersion.substring(0, idx);
		}
		
		
	}
	
	public static String createDateTime(JsonNode json) {
		String date = json.get("metadata").get("creationTimestamp").asText();
		return date.replace("T", " ").substring(0, date.length() - 1);
	}
	
	public static String currentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
}
