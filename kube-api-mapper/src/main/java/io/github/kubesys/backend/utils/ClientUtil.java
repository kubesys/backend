/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import io.github.kubesys.backend.SQLMapper;
import io.github.kubesys.datafrk.core.Table;
import io.github.kubesys.datafrk.core.operators.QueryData;
import io.github.kubesys.kubeclient.KubernetesCRDWacther;
import io.github.kubesys.kubeclient.KubernetesClient;


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
		initKubeClient();
    }

	public static void initKubeClient() {
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

	public static void register(String role, String token) throws Exception {
		KubernetesClient client = new KubernetesClient(
				System.getenv("kubeUrl"), token);
		roleToTokenMapper.put(role, role.equals("default") ? "default" : token);
		tokenToclientMapper.put(role.equals("default") ? "default" : token, client);
	}
	
	public static String getToken(String role) {
		return roleToTokenMapper.get(role);
    }
	
	public static KubernetesClient getClient(String token) {
    	KubernetesClient client = tokenToclientMapper.get(token);
    	if (client == null) {
    		throw new RuntimeException("invalid role and token");
    	}
		return client;
    }
	
	public static void closeKubeClient(String token) {
		KubernetesClient rmClient = ClientUtil.tokenToclientMapper.remove(token);
		try {
			rmClient.getHttpCaller().getHttpClient().close();
			rmClient = null;
		} catch (Exception ex) {
			
		}
	}
	
	
	/******************************************
	*
	*         sqlMapper
	*
	***********************************************/
	
   
   protected static SQLMapper sqlMapper = null;
	
	/**
	 * @return                           sqlClient
	 */
	public static synchronized SQLMapper sqlMapper() {
		
		try {
			if (sqlMapper == null) {
				sqlMapper =  new SQLMapper();
			}
		} catch (Exception ex) {
			m_logger.severe(ex.toString());
			System.exit(1);
		}
		
		return sqlMapper;
	}
	
	public static String getSecretName(String name)  {
		Table<?> table = null;
		for (Table<?> t : sqlMapper.listTables()) {
			if (t.name().equals("secrets")) {
				table = t;
				break;
			}
		}
		QueryData query = new QueryData("select name from secrets where region = 'local' and name like '%" + name +"-token%'");
		
		ResultSet rs = null;
		try {
			rs = (ResultSet) table.query(query);
			rs.next();
			return rs.getString("name");
		} catch (Exception ex) {
			
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
		}
		return null;
	}
	
}
