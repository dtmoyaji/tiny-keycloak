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
        boolean rvalue = kcc.auth(pr.getProperty("test_user"), pr.getProperty("test_password"));
        assertTrue(rvalue);
        rvalue = kcc.auth(kcc.getAccessToken());
        if (rvalue) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "アクセストークン {0}", kcc.getAccessToken());
        }
    }

    @Test
    public void testGetUserRepresentation() {
        System.out.println("getUserRepresentation");
        UserRepresentation ur = kcc.getUserRepresentation(pr.getProperty("test_user"));
        assertEquals(pr.getProperty("test_user"), ur.getUsername());
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "username {0}", ur.getUsername());
    }

}
