/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.utils;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.backend.SQLMapper;
import io.github.kubesys.kubeclient.KubernetesCRDWacther;
import io.github.kubesys.kubeclient.KubernetesClient;
import io.github.kubesys.kubeclient.KubernetesConstants;
import io.github.kubesys.kubeclient.KubernetesWatcher;


/**
 * @author lichengzhi99@otcaix.iscas.ac.cn
 * @author wuheng@otcaix.iscas.ac.cn
 *
 */
public class ClientUtil {

	/******************************************
    *
    *         kubeClient
    *
    ***********************************************/
	
    public final static Map<String, KubernetesClient> tokenMaps = new HashMap<>();
    
    static {
    	try {
	    	KubernetesClient client = new KubernetesClient(
					System.getenv("kubeUrl"), 
					System.getenv("kubeToken"));
			tokenMaps.put("default", client);
			tokenMaps.put("admin-" + System.getenv("kubeToken").substring(0, 8), client);
			recoverAllRoles(client);
		
			client.watchResources("apiextensions.k8s.io.CustomResourceDefinition", new KubernetesCRDWacther(client));
			ClientUtil.getClient("default").watchResources("User", "", 
					new UserWatcher(ClientUtil.getClient("default")));
		} catch (Exception e) {
		}
    }

    private static class UserWatcher extends KubernetesWatcher {

		public UserWatcher(KubernetesClient kubeClient) {
			super(kubeClient);
		}

		@Override
		public void doAdded(JsonNode node) {
			
			try {
				String user = node.get("metadata").get("name").asText();
				String token = node.get("spec").get("token").asText();
				tokenMaps.put(user + "-" + token.substring(0, 8),
						new KubernetesClient(
								System.getenv("kubeUrl"), 
								token));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void doModified(JsonNode node) {
			
		}

		@Override
		public void doDeleted(JsonNode node) {
			String user = node.get("metadata").get("name").asText();
			String token = node.get("spec").get("token").asText();
			tokenMaps.remove(user + "-" + token.substring(0, 8));
		}

		@Override
		public void doClose() {
			try {
				ClientUtil.getClient("default").watchResources("User", "", 
						new UserWatcher(ClientUtil.getClient("default")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
    
	protected static void recoverAllRoles(KubernetesClient client) {
		try {
			JsonNode json = client.listResources("UserRole", 
					KubernetesConstants.VALUE_ALL_NAMESPACES).get("items");
			for (int i = 0; i < json.size(); i++) {
				JsonNode item = json.get(i);
				String namespace = KubeUtil.getNamespace(item);
				String name = KubeUtil.getName(item);
				String token = KubeUtil.getToken(client, namespace, name);
				KubernetesClient roleClient = new KubernetesClient(
						client.getHttpCaller().getMasterUrl(), token);
				tokenMaps.put(token, roleClient);
			}
			
		} catch (Exception e) {
		}
	}
    
    public static KubernetesClient getClient(String token) {
    	KubernetesClient client = tokenMaps.get(token);
    	if (client == null) {
    		throw new RuntimeException("miss or wrong token");
    	}
		return client;
    }
    
    public static void createClientIfNotExist(String token) {
    	if (!tokenMaps.containsKey(token)) {
    		try {
				tokenMaps.put(token, new KubernetesClient(
						tokenMaps.get("default").getHttpCaller().getMasterUrl(), token));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    public static void deleteClientIfExist(String token) throws Exception {
    	if (tokenMaps.containsKey(token)) {
    		KubernetesClient client = tokenMaps.remove(token);
    		client.getHttpCaller().getHttpClient().close();
    		client = null;
    	}
    }
    
    /******************************************
     *
     *         SqlClient
     *
     ***********************************************/
    
    protected static SQLMapper sqlClient = null;
	
	/**
	 * @return                           sqlClient
	 */
	public static synchronized SQLMapper sqlClient() {
		
		try {
			if (sqlClient == null) {
				
				sqlClient =  new SQLMapper(tokenMaps.get("default"));
			}
		} catch (Exception ex) {
			return null;
		}
		
		return sqlClient;
	}
	
}
