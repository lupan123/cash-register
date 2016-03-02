package com.lupan.task.modules;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PrintMachineTest {

     PrintMachine pm;

    @Before
    public  void setUp() throws Exception {
        pm = new PrintMachine();
        pm.init();
    }


    @Test
    public void testPrint() throws Exception {
        pm.getBarCodes("src/test/resources/barCodesList.json");
        pm.print();
        assertTrue(true);
    }

    @Test
    public void testPrint2() throws Exception {
        pm.getBarCodes("src/test/resources/barCodesList2.json");
        pm.print();
        assertTrue(true);
    }

    @Test
    public void testPrint3() throws Exception {
        pm.getBarCodes("src/test/resources/barCodesList3.json");
        pm.print();
        assertTrue(true);
    }

    @Test
    public void testPrint4() throws Exception {
        pm.getBarCodes("src/test/resources/barCodesList4.json");
        pm.print();
        assertTrue(true);
    }

    @Test
    public void testPrint5() throws Exception {
        pm.getBarCodes("src/test/resources/barCodesList5.json");
        pm.print();
        assertTrue(true);
    }

    @Test
    public void testPrint6() throws Exception {
        try{
            pm.getBarCodes("src/test/resources/xxx.json");
            pm.print();
        }catch (Exception e){
            if (e instanceof IOException){
                assertTrue(true);
            }
        }
    }
}