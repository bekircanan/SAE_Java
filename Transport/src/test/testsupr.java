/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;
import application.Main;
import static org.junit.Assert.*;
import org.junit.Test;
/**
 *
 * @author Enzo
 */
public class testsupr {
   
  @Test
  public void testString() throws Exception {
    String s = "Bonjour le monde";

    assertEquals("Bonjour le monde", s);
    assertEquals("Bonsoir le monde", s);
    //assertFalse(s.isBlank());
  }


}
