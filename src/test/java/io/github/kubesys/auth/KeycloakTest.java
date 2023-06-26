/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.auth;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;


/**
 * @author wuheng09@gmail.com
 *
 */
public class KeycloakTest {

	
	public static void main(String[] args) throws Exception {
		 String serverUrl = "http://139.9.165.93:30302/auth";
	        String realm = "admin";
	        String clientId = "eb694547-81a2-4ca7-a618-c729b4a69b79";
	        String clientSecret = "";
//	        String clientSecret = "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI1ZTBlNDc2OC1mYzI5LTQwNjEtYTRmZi1kNWI4NDlmMTNmY2QifQ.eyJleHAiOjIwMDMxMDQ3ODIsImlhdCI6MTY4Nzc0NDc4MiwianRpIjoiMjMwMmMzNjMtOWMzMy00NGJkLWE0NWItZjg0ODQzMmY4NDMyIiwiaXNzIjoiaHR0cDovLzEzOS45LjE2NS45MzozMDMwMi9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiaHR0cDovLzEzOS45LjE2NS45MzozMDMwMi9yZWFsbXMvbWFzdGVyIiwidHlwIjoiSW5pdGlhbEFjY2Vzc1Rva2VuIn0.v1Xars6qdGjMa6r1XD5uYAgXP8j86h1GfUbvtbEu5Wc";
	        String username = "user";
	        String password = "bitnami";

	        // 创建 Keycloak 客户端
	        Keycloak keycloak = KeycloakBuilder.builder()
	                .serverUrl(serverUrl)
	                .realm(realm)
	                .clientId(clientId)
	                .clientSecret(clientSecret)
	                .grantType(OAuth2Constants.PASSWORD)
	                .username(username)
	                .password(password)
	                .build();

	        // 获取访问令牌
	        AccessTokenResponse accessTokenResponse = keycloak.tokenManager().getAccessToken();

	        // 获取 Realm 资源
	        RealmResource realmResource = keycloak.realm(realm);

	        // 获取用户资源
	        UserResource userResource = realmResource.users().get("user-id");

	        // 获取用户信息
	        UserRepresentation userRepresentation = userResource.toRepresentation();

	        // 打印用户信息
	        System.out.println("User ID: " + userRepresentation.getId());
	        System.out.println("Username: " + userRepresentation.getUsername());

	        // 关闭 Keycloak 客户端
	        keycloak.close();
	
	}


}
