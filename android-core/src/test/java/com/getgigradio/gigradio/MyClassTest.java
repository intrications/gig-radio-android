package com.getgigradio.gigradio;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyClassTest {

    @Test
    public void testHelloWorld() throws Exception {
        MyClass myJvmClass = new MyClass();

        String text = myJvmClass.getHelloWorld();

        assertEquals(text, "Hello World");
    }

}
