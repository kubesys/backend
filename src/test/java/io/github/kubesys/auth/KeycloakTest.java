/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author wuheng09@gmail.com
 *
 */
public class KeycloakTest {

	
	public static void main(String[] args) throws Exception {
		 String serverUrl = "http://139.9.165.93:30302";
	        String realm = "master";
	        String clientId = "admin-cli";
	        String username = "user";
	        String password = "bitnami";

	        // 创建 Keycloak 客户端
//	        Keycloak keycloak = KeycloakBuilder.builder()
//	                .serverUrl(serverUrl)
//	                .realm(realm)
//	                .grantType(OAuth2Constants.PASSWORD)
//	                .clientId(clientId)
//	                .username(username)
//	                .password(password)
//	                .build();

	        // 获取访问令牌
//	        RealmResource realmResource = keycloak.realm(realm);
//	        UsersResource userResource = realmResource.users();
//	        Map<String, String> attributeMap = new HashMap<>();
//	        List<UserRepresentation> userList = userResource.list();


//	        for (UserRepresentation user : userList) {
//	        	System.out.println(user.getUsername());
//	        }
	        // 关闭 Keycloak 客户端
//	        keycloak.close();
	
	}


}
