package it.mauxilium.collectorapp.service;


import it.mauxilium.collectorapp.model.SampleRequestIn;
import it.mauxilium.collectorapp.model.SampleResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SampleServiceTest {

    private SampleService instance;

    @Before
    public void setup() {
        instance = new SampleService("layer", "host");
    }

    @Test
    public void buildResponseOk() {
        ArrayList<String> stepsBefore = new ArrayList<>();
        stepsBefore.add("Step 1");
        stepsBefore.add("Step 2");

        SampleRequestIn request = new SampleRequestIn();
        request.setSampleValue("Input Sample");

        SampleResponse response = instance.buildResponse(request);

        Assert.assertEquals(2, response.getSampleSteps().size());
        Assert.assertEquals("Input Sample", response.getSampleSteps().get(0));
        Assert.assertEquals("host ends pipeline", response.getSampleSteps().get(1));
    }
}