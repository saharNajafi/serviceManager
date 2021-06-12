package org.tiddev.serviceManager.service.keycloak;

import java.util.Arrays;
import java.util.Collections;

import javax.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

public class KeycloakAdminClientUtils {

	public static void main(String[] args) {

		String serverUrl = "http://localhost:8080/auth";
		String realm = "ServiceManager";
		String clientId = "serviceManager-app";
		String clientSecret = "aa96bb38-afb4-48a4-aa54-9e2e90dd0c3e";

//		// Client "idm-client" needs service-account with at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"
//		Keycloak keycloak = KeycloakBuilder.builder() 
//				.serverUrl(serverUrl) 
//				.realm(realm) 
//				.grantType(OAuth2Constants.CLIENT_CREDENTIALS) 
//				.clientId(clientId) 
//				.clientSecret(clientSecret)
//				.build();

		// User "javaland" needs at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"
		Keycloak keycloak = KeycloakBuilder.builder()
				.serverUrl(serverUrl)
				.realm(realm)
				.grantType(OAuth2Constants.PASSWORD)
				.clientId(clientId)
				.clientSecret(clientSecret)
				.username("najafi") 
				.password("najafi") 
				.build();

		// Define user
		UserRepresentation user = new UserRepresentation();
		user.setEnabled(true);
		user.setUsername("tester1");
		user.setFirstName("First");
		user.setLastName("Last");
		user.setEmail("tom+tester1@tdlabs.local");
		user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));

		// Get realm
		RealmResource realmResource = keycloak.realm(realm);
		UsersResource userResource = realmResource.users();

		// Create user (requires manage-users role)
		Response response = userResource.create(user);
		System.out.println("Repsonse: " + response.getStatusInfo());
//		System.out.println(response.getLocation());
//		String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
//
//		System.out.printf("User created with userId: %s%n", userId);
//
//		// Get realm role "tester" (requires view-realm role)
//		RoleRepresentation testerRealmRole = realmResource.roles()//
//				.get("tester").toRepresentation();
//
//		// Assign realm role tester to user
//		userResource.get(userId).roles().realmLevel() //
//				.add(Arrays.asList(testerRealmRole));
//
//		// Get client
//		ClientRepresentation app1Client = realmResource.clients() //
//				.findByClientId("app-javaee-petclinic").get(0);
//
//		// Get client level role (requires view-clients role)
//		RoleRepresentation userClientRole = realmResource.clients().get(app1Client.getId()) //
//				.roles().get("user").toRepresentation();
//
//		// Assign client level role to user
//		userResource.get(userId).roles() //
//				.clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));
//
//		// Define password credential
//		CredentialRepresentation passwordCred = new CredentialRepresentation();
//		passwordCred.setTemporary(false);
//		passwordCred.setType(CredentialRepresentation.PASSWORD);
//		passwordCred.setValue("test");
//
//		// Set password credential
//		userResource.get(userId).resetPassword(passwordCred);
	}
}
