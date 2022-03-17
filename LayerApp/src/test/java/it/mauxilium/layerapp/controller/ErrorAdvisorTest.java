package it.mauxilium.layerapp.controller;

import org.junit.Assert;
import org.junit.Test;

public class ErrorAdvisorTest {

    private ErrorAdvisor instance = new ErrorAdvisor();

    @Test
    public void verifyException() {
        IllegalStateException generatedEx = new IllegalStateException("Test Cause");

        String response = instance.onIllegalStateEx(generatedEx);

        Assert.assertEquals("Pipeline fails: java.lang.IllegalStateException: Test Cause", response);
    }

}