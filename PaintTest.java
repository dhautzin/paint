/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import javafx.scene.control.ColorPicker;

/**
 *
 * @author Daniel
 */
public class PaintTest {
    
    public PaintTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception{
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception{
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testTestTwo()
    {
        System.out.println("Test two");
        String h = "Hello ";
        String w = "World!";
        Paint instance = new Paint();
        String expResult = "Hello World!";
        String result = instance.testTwo(h, w);
        assertEquals(expResult, result);
    }
    

    @Test
    public void testTestThree()
    {
        System.out.println("Test Three");
        double a1 = 2;
        double a2 = 6;
        Paint instance = new Paint();
        double expResult = 3;
        double result = instance.testThree(a1, a2);
        assertEquals(expResult, result);
    }
    

    

    
    
    @Test
    public void testTest() {
        System.out.println("Test One");
        String a1 = "2";
        String a2 = "2";
        Paint instance = new Paint();
        int expResult = 4;
        int result = instance.test(a1, a2);
        assertEquals(expResult, result);
        
    }
}
