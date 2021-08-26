/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.datafrk.postgres.PostgresDataContext;
import io.github.kubesys.kubeclient.KubernetesCRDWacther;
import io.github.kubesys.kubeclient.KubernetesClient;
import io.github.kubesys.kubeclient.KubernetesConstants;
import io.github.kubesys.kubeclient.KubernetesWatcher;
import io.github.kubesys.kubeclient.core.KubernetesRuleBase;


/**
 * @author wuheng@iscas.ac.cn
 * @since  2.0.0
 */
public class KubeMirror {

	/**
	 * m_logger
	 */
	public static final Logger m_logger = Logger.getLogger(KubeMirror.class.getName());

	/**
	 * sources
	 */
	public Set<String> sources = new HashSet<>();

	/**
	 * kube client
	 */
	protected final KubernetesClient kubeClient;

	/**
	 * sql client
	 */
	protected final KubeSQLClient sqlClient;
	
	
	/**
	 * threads
	 */
	protected final Map<String, Thread> watchers = new HashMap<>();
	
	public KubeMirror(KubernetesClient client) throws Exception {
		Properties props = new Properties();
		props.put("druid.driverClassName", System.getenv("jdbcDriver")); 
		props.put("druid.url", System.getenv("jdbcUrl")); 
		props.put("druid.username", System.getenv("jdbcUser")); 
		props.put("druid.password", System.getenv("jdbcPassword"));
		props.put("druid.initialSize", 10); 
		props.put("druid.maxActive", 100);
		props.put("druid.maxWait", 0);
		
		KubeSQLClient sqlTempClient = new KubeSQLClient(new PostgresDataContext(props));
		
		String db = System.getenv("jdbcDB");
		if (!sqlTempClient.checkDatabase(db)) {
			sqlTempClient.createDatbase(db);
		}
		sqlTempClient.close();
		
		props.put("druid.url", realUrl(System.getenv("jdbcUrl"), db)); 
		this.sqlClient = new KubeSQLClient(new PostgresDataContext(props));
		
		this.kubeClient = client;
	}

	private String realUrl(String oldUrl, String database) {
		int stx = oldUrl.indexOf("/", getUrlPrefix().length());
		int etx = oldUrl.indexOf("?");
		return (etx == - 1) ? oldUrl.substring(0, stx + 1) + database :
				oldUrl.substring(0, stx + 1) + database + oldUrl.substring(etx);
	}

	private String getUrlPrefix() {
		return "jdbc:postgresql://";
	}
	
	/**
	 * @return                               mirror
	 * @throws Exception                     exception
	 */
	public KubeMirror fromSources() throws Exception {
		try {
			for (String kind : kubeClient.getAnalyzer().getConvertor()
						.getRuleBase().getApiPrefixMapping().keySet()) {
				this.sources.add(kind);
			}
		} catch (Exception ex) {
			m_logger.severe("wrong token, or token with lower permissions, or unwatchable resources ");
			System.exit(1);
		}

		return this;
	}
	
	/**
	 * @param  kind                          kind
	 * @return                               mirror
	 * @throws Exception                     exception
	 */
	public KubeMirror fromSource(String kind) throws Exception {
		this.sources.add(kind);
		return this;
	}
	
	/**
	 * @param  kinds                         kinds
	 * @return                               mirror
	 * @throws Exception                     exception
	 */
	public KubeMirror fromSource(String[] kinds) throws Exception {
		for (String kind : kinds) {
			this.sources.add(kind);
		}
		return this;
	}
	
	/**
	 * @throws Exception                    exception
	 */
	public void start() throws Exception {
		fromSources().toTargets();
	}
	
	/**
	 * @param  kind                         kind
	 * @throws Exception                    exception
	 */
	public void start(String kind) throws Exception {
		fromSource(kind).toTargets();
	}
	
	/**
	 * @param  kinds                        kinds
	 * @throws Exception                    exception
	 */
	public void start(String[] kinds) throws Exception {
		fromSource(kinds).toTargets();
	}


	/**
	 * @return                              mirror
	 * @throws Exception                    exception
	 */
	protected KubeMirror toTargets() throws Exception {
		for (String kind : sources) {
			try {
				doWatcher(kind);
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
	protected void doWatcher(String kind) throws Exception {
		
		KubernetesRuleBase ruleBase = kubeClient.getAnalyzer().getConvertor().getRuleBase();
		String table = ruleBase.getName(kind);

		if (table.contains("/") || table.contains("-")) {
			return;
		}
		
		deleteTableIfExit(table);
		sqlClient.createTable(table);

		m_logger.info("fullname is " + kind);
		SourceToSink watcher = new SourceToSink(kind, table, this);
		watchers.put(table, kubeClient.watchResources(kind, watcher));
	}

	/**
	 * @param table                   table
	 */
	protected void deleteTableIfExit(String table) {
		if (sqlClient.checkTable(table)) {
			sqlClient.dropTable(table);
		}
	}
	
	/**
	 * @param kind                          kind
	 * @throws Exception                    exception
	 */
	@SuppressWarnings("deprecation")
	protected void stopWatcher(String kind) throws Exception {
		String table = kubeClient.getAnalyzer().getConvertor().getRuleBase().getName(kind);
		if (sqlClient.checkTable(table)) {
			sqlClient.dropTable(table);
		} 
		watchers.get(table).stop();
		watchers.remove(table);
	}
	
	
	public void addSource(String kind) {
		if (!sources.contains(kind)) {
			sources.add(kind);
			try {
				doWatcher(kind);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deleteSource(String kind) {
		if (sources.contains(kind)) {
			sources.remove(kind);
			try {
				stopWatcher(kind);
			} catch (Exception e) {
			}
		}
	}

	public boolean beenWatched(String kind) {
		String table = kubeClient.getAnalyzer().getConvertor().getRuleBase().getName(kind);
		return watchers.containsKey(table);
	}

	public KubernetesClient getKubeClient() {
		return kubeClient;
	}

	public KubeSQLClient getSqlClient() {
		return sqlClient;
	}
	
	
	/**
	 * @author wuheng
	 * @since 2019.4.20
	 */
	public static class SourceToSink extends KubernetesWatcher {

		public static final Logger m_logger = Logger.getLogger(SourceToSink.class.getName());

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
		protected final KubeMirror kubeMirror;
		
		/**
		 * crdWatcher
		 */
		protected final KubernetesCRDWacther crdWatcher;
		
		public SourceToSink(String kind, String table, KubeMirror kubeMirror) {
			super(kubeMirror.getKubeClient());
			this.kind = kind;
			this.table = table;
			this.kubeMirror = kubeMirror;
			this.crdWatcher = new KubernetesCRDWacther(kubeMirror.getKubeClient());
		}

		@Override
		public void doAdded(JsonNode json) {
			
			try {
				kubeMirror.getSqlClient().insertObject(table, KubeUtils.getName(json),
						KubeUtils.getNamespace(json),
						getGroup(json),
						createDateTime(json),
						currentDateTime(),
						KubeUtils.getJsonWithoutAnotation(json));
				m_logger.info("insert object  " + json + " successfully.");
				
				if ("CustomResourceDefinition".equals(json.get("kind").asText())) {
					this.crdWatcher.doAdded(json);
					JsonNode spec = json.get(KubernetesConstants.KUBE_SPEC);
					JsonNode names = spec.get(KubernetesConstants.KUBE_SPEC_NAMES);
					String kind = names.get(KubernetesConstants.KUBE_SPEC_NAMES_KIND).asText();
					String group = spec.get("group").asText();
					kubeMirror.addSource(group + "." + kind);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				m_logger.severe("fail to insert object :" + json + ". Becasue: " + e);
			}
			
		}

		@Override
		public void doModified(JsonNode json) {
			
			try {
				kubeMirror.getSqlClient().updateObject(table, KubeUtils.getName(json),
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
				kubeMirror.getSqlClient().deleteObject(table, KubeUtils.getName(json),
						KubeUtils.getNamespace(json),
						getGroup(json),
						KubeUtils.getJsonWithoutAnotation(json));
				m_logger.info("delete object  " + json + " successfully.");
				
				if ("CustomResourceDefinition".equals(json.get("kind").asText())) {
					this.crdWatcher.doDeleted(json);
					JsonNode spec = json.get(KubernetesConstants.KUBE_SPEC);
					JsonNode names = spec.get(KubernetesConstants.KUBE_SPEC_NAMES);
					String kind = names.get(KubernetesConstants.KUBE_SPEC_NAMES_KIND).asText();
					String group = spec.get("group").asText();
					kubeMirror.deleteSource(group + "." + kind);
				}
				
			} catch (Exception e) {
				m_logger.severe("fail to delete object :" + e);
			}
			
		}

		@Override
		public void doClose() {
			try {
				client.watchResources(kind, new SourceToSink(
						kind, table, kubeMirror));
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
