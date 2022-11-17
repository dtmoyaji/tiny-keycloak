package org.tiny.keycloak;

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
 * KeyCloakクライアントクラス
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

    /**
     * アクセストークンを取得する。 この値は、認証するたびにKeycloakから発行されるトークンを格納する。
     *
     * @return
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * プロパティを取得する。
     *
     * @param key
     * @return プロパティ値
     */
    public String getProperty(String key) {
        return this.properties.get(key);
    }

    /**
     * プロパティを登録する.
     *
     * @param key KeycloakClientで定義するキー
     * @param value 値
     */
    public final void setProperty(String key, String value) {
        this.properties.put(key, value);
    }

    /**
     * 認証する.
     *
     * @param uid ユーザーID
     * @param password パスワード
     * @return 認証に成功するとTrue, 失敗するとfalse
     */
    public boolean auth(String uid, String password) {
        boolean rvalue = false;
        this.accessToken = "";
        try {
            AuthzClient authzClient = this.getAuthClient();
            AuthorizationRequest request = new AuthorizationRequest();
            AuthorizationResponse response = authzClient.authorization(uid, password)
                    .authorize(request);
            this.accessToken = response.getToken();
            Logger.getLogger(KeycloakClient.class.getName()).log(Level.INFO, "アクセストークンを格納しました");
            rvalue = true;
        } catch (RuntimeException ex) {
            Logger.getLogger(KeycloakClient.class.getName()).log(Level.SEVERE, ex.getCause().getLocalizedMessage());
        }
        return rvalue;
    }

    /**
     * 認証する.
     *
     * @param accessToken アクセストーク
     * @return 認証に成功するとTrue, 失敗するとfalse
     */
    public boolean auth(String accessToken) {
        boolean rvalue = false;

        try {
            AuthzClient authzClient = this.getAuthClient();
            AuthorizationRequest request = new AuthorizationRequest();
            AuthorizationResponse response = authzClient.authorization(accessToken)
                    .authorize(request);
            this.accessToken = response.getToken();
            Logger.getLogger(KeycloakClient.class.getName()).log(Level.INFO, "アクセストークンを格納しました");
            rvalue = true;
        } catch (RuntimeException ex) {
            Logger.getLogger(KeycloakClient.class.getName()).log(Level.SEVERE, ex.getCause().getLocalizedMessage());
        }

        return rvalue;
    }

    private AuthzClient getAuthClient() {
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

        return AuthzClient.create(configuration);
    }

    /**
     * ユーザー情報を取得する.
     *
     * @param uid
     * @return
     */
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
}
