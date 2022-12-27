/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package org.tiny.keycloak;

import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.representations.idm.UserRepresentation;

/**
 *
 */
public class KeycloakClientTest {

    public static final String TEST_USER = "tiny.keycloak.test_user";
    public static final String TEST_PASSWORD = "tiny.keycloak.test_password";

    KeycloakClient kcc;
    PropertyReader pr;

    public KeycloakClientTest() {
    }

    @Before
    public void setUp() {
        kcc = new KeycloakClient("target/classes/tinycore.properties");
        pr = new PropertyReader("target/classes/tinycore.properties");
    }

    @Test
    public void testAuth() {
        System.out.println("auth");
        boolean rvalue = kcc.auth(pr.getProperty(TEST_USER), pr.getProperty(TEST_PASSWORD));
        assertTrue(rvalue);
        rvalue = kcc.auth(kcc.getAccessToken());
        if (rvalue) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "アクセストークン {0}", kcc.getAccessToken());
        }
    }

    @Test
    public void testGetUserRepresentation() {
        System.out.println("getUserRepresentation");
        UserRepresentation ur = kcc.getUserRepresentation(pr.getProperty(TEST_USER));
        assertEquals(pr.getProperty(TEST_USER), ur.getUsername());
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "username {0}", ur.getUsername());
    }

}
