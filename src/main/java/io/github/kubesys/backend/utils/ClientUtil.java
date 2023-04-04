/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.backend.clients.SQLMapperClient;
import io.github.kubesys.client.KubernetesCRDWacther;
import io.github.kubesys.client.KubernetesClient;


/**
 * @author lichengzhi99@otcaix.iscas.ac.cn
 * @author wuheng@otcaix.iscas.ac.cn
 *
 */
public class ClientUtil {

	public static Logger m_logger = Logger.getLogger(ClientUtil.class.getName());
	
	/******************************************
    *
    *         kubeClient
    *
    ***********************************************/
	
	public final static Map<String, KubernetesClient> tokenToclientMapper = new HashMap<>();
	
    public final static Map<String, String> roleToTokenMapper = new HashMap<>();
    
    static {
		initDefKubeClient();
		recoverAllRoles();
    }

	public static void initDefKubeClient() {
		if (tokenToclientMapper.size() == 0) {
			try {
				register("default", System.getenv("kubeToken"));
				getClient("default").watchResources(
						"apiextensions.k8s.io.CustomResourceDefinition", 
						new KubernetesCRDWacther(getClient("default")));
			} catch (Exception e) {
				m_logger.severe(e.toString());
				System.exit(1);
			}
		}
		
	}
	
	public static void recoverAllRoles() {
		try {
			for (JsonNode json : getClient("default").listResources("ServiceAccount", "default").get("items")) {
				String name = json.get("metadata").get("name").asText();
				if (!name.equals("default")) {
					String secretName = json.get("secrets").get(0).get("name").asText();
					JsonNode secretJson = getClient("default").getResource("Secret", "default", secretName);
					register(name, new String(Base64.getDecoder().decode(
							secretJson.get("data").get("token").asText())));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void register(String role, String token) throws Exception {
		KubernetesClient client = role.equals("default") ? 
				new KubernetesClient(System.getenv("kubeUrl"), token)
				: new KubernetesClient(System.getenv("kubeUrl"), token, 
						tokenToclientMapper.get("default").getAnalyzer());
		roleToTokenMapper.put(role, role.equals("default") ? "default" : token);
		tokenToclientMapper.put(role.equals("default") ? "default" : token, client);
	}
	
	public static String getBearerToken(String role) {
		return roleToTokenMapper.get(role);
    }
	
	
	public static KubernetesClient getClient(String token) {
    	KubernetesClient client = tokenToclientMapper.get(token);
    	if (client == null) {
    		throw new RuntimeException("invalid role and token");
    	}
		return client;
    }
	
	public static void unregister(String role) {
		if (role.equals("default")) {
			throw new UnsupportedOperationException("Unsupport unregister default");
		}
		
		String token = roleToTokenMapper.remove(role);
		KubernetesClient rmClient = ClientUtil.tokenToclientMapper.remove(token);
		try {
			rmClient.getRequester().getHttpClient().close();
			rmClient = null;
		} catch (Exception ex) {
			
		}
	}
	
	
	/******************************************
	*
	*         sqlMapper
	*
	***********************************************/
	
   
   protected static SQLMapperClient sqlMapper = null;
	
	/**
	 * @return                           sqlClient
	 */
	public static synchronized SQLMapperClient sqlMapper() {
		
		try {
			if (sqlMapper == null) {
				sqlMapper =  new SQLMapperClient();
			}
		} catch (Exception ex) {
			m_logger.severe(ex.toString());
			System.exit(1);
		}
		
		return sqlMapper;
	}
	
	
}