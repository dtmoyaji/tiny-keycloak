/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package org.tiny.keycloak;

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
        kcc=new KeycloakClient("target/classes/tinycore.properties");
        pr=new PropertyReader("target/classes/tinycore.properties");
    }

    @Test
    public void testAuth_String_String() {
        System.out.println("auth(String,String)");
        boolean rvalue = kcc.auth(pr.getProperty("test_user"), pr.getProperty("test_password"));
        assertTrue(rvalue);
    }
    
    @Test
    public void testGetUserRepresentation(){
        System.out.println("getUserRepresentation");
        UserRepresentation ur = kcc.getUserRepresentation(pr.getProperty("test_user"));
        assertEquals(pr.getProperty("test_user"), ur.getUsername());
    }

}
