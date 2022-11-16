/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package org.tiny.keycloak;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

/**
 *
 * @author bythe
 */
public class TinyKeycloak {

    public static void main(String[] args) throws IOException {
        AuthzClient authzClient = AuthzClient.create();
        AuthorizationRequest request = new AuthorizationRequest();
        AuthorizationResponse response = authzClient.authorization("<USER_ID>", "<PASSWORD>")
                .authorize(request);
        String token = response.getToken();
        Logger.getLogger(TinyKeycloak.class.getName()).log(Level.INFO, token);
    }
}
