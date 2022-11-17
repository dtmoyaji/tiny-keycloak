package org.tiny.keycloak;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

/**
 *
 * @author DtmOyaji
 */
public class KeycloakClient {

    public static final String AUTH_SERVER_URL = "auth_server_url";
    public static final String ADMIN_CONTEXT = "admin_context";
    public static final String ADMIN_PASSWORD = "admin_password";
    public static final String ADMIN_REALM = "admin_realm";
    public static final String CLIENT_REALM = "client_realm";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";

    private HashMap<String, String> properties = new HashMap<>();

    private String accessToken;

    public KeycloakClient(String propertyPath) {
        PropertyReader preader = new PropertyReader(propertyPath);
        this.setProperty(
                KeycloakClient.AUTH_SERVER_URL,
                preader.getProperty(KeycloakClient.AUTH_SERVER_URL)
        );
        this.setProperty(
                KeycloakClient.ADMIN_CONTEXT,
                preader.getProperty(KeycloakClient.ADMIN_CONTEXT)
        );
        this.setProperty(
                KeycloakClient.ADMIN_PASSWORD,
                preader.getProperty(KeycloakClient.ADMIN_PASSWORD)
        );
        this.setProperty(
                KeycloakClient.ADMIN_REALM,
                preader.getProperty(KeycloakClient.ADMIN_REALM)
        );
        this.setProperty(KeycloakClient.CLIENT_REALM,
                preader.getProperty(KeycloakClient.CLIENT_REALM)
        );
        this.setProperty(KeycloakClient.CLIENT_ID,
                preader.getProperty(KeycloakClient.CLIENT_ID)
        );
        this.setProperty(KeycloakClient.CLIENT_SECRET,
                preader.getProperty(KeycloakClient.CLIENT_SECRET)
        );
    }

    public KeycloakClient() {

    }

    public String getProperty(String key) {
        return this.properties.get(key);
    }

    public final void setProperty(String key, String value) {
        this.properties.put(key, value);
    }

    public boolean auth(String uid, String password) {
        boolean rvalue = false;

        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret",
                this.getProperty(KeycloakClient.CLIENT_SECRET)
        );

        Configuration configuration = new Configuration(
                this.getProperty(KeycloakClient.AUTH_SERVER_URL),
                this.getProperty(KeycloakClient.CLIENT_REALM),
                this.getProperty(KeycloakClient.CLIENT_ID),
                clientCredentials,
                null
        );

        AuthzClient authzClient = AuthzClient.create(configuration);
        AuthorizationRequest request = new AuthorizationRequest();
        AuthorizationResponse response = authzClient.authorization(uid, password)
                .authorize(request);
        this.accessToken = response.getToken();
        Logger.getLogger(KeycloakClient.class.getName()).log(Level.INFO, this.accessToken);

        return rvalue;
    }

    public UserRepresentation getUserRepresentation(String uid) {
        UserRepresentation rvalue = null;
        Keycloak keycloak = KeycloakBuilder
                .builder()
                .serverUrl(this.getProperty(KeycloakClient.AUTH_SERVER_URL))
                .realm(this.getProperty(KeycloakClient.ADMIN_REALM))
                .username(this.getProperty(KeycloakClient.ADMIN_CONTEXT))
                .password(this.getProperty(KeycloakClient.ADMIN_PASSWORD))
                .clientId(this.getProperty(KeycloakClient.CLIENT_ID))
                .build();

        UsersResource usersResource = keycloak.realm(
                this.getProperty(KeycloakClient.CLIENT_REALM)
        ).users();
        
        List<UserRepresentation> usersInfo = usersResource.list();
        for (UserRepresentation userInfo : usersInfo) {
            if (userInfo.getUsername().equals(uid)) {
                rvalue = userInfo;
                break;
            }
        }
        return rvalue;
    }

    public static void main(String[] args) throws IOException {

        PropertyReader preader = new PropertyReader("target/classes/tinycore.properties");
        KeycloakClient kcc = new KeycloakClient("target/classes/tinycore.properties");
        kcc.auth(preader.getProperty("test_user"), preader.getProperty("test_password"));
        UserRepresentation ur = kcc.getUserRepresentation(preader.getProperty("test_user"));
        Logger.getLogger(KeycloakClient.class.getName()).log(Level.INFO, ur.getId());
    }
}
