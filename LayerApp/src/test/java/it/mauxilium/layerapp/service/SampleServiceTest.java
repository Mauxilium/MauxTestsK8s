package it.mauxilium.layerapp.service;

import it.mauxilium.layerapp.model.SampleRequestIn;
import it.mauxilium.layerapp.model.SampleRequestPropagated;
import it.mauxilium.layerapp.model.SampleStepsResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

@RunWith(SpringRunner.class)
public class SampleServiceTest {

    @Mock
    private RestTemplate myTemplate;

    private SampleService instance;

    @Before
    public void setup() {
        Instant execTime = Instant.parse("2018-11-30T18:35:24.00123Z");
        Clock myClock = Clock.fixed(execTime, ZoneOffset.UTC);

        instance = new SampleService(
                "test_layer",
                "dest_layer_test",
                "testHost",
                myClock,
                myTemplate);
    }

    @Test
    public void buildResponse_WithoutBodySteps() {
        SampleRequestIn requestIn = buildRequestIn(null, HttpStatus.OK);

        SampleStepsResponse response = instance.buildSampleSteps(requestIn);

        Assert.assertEquals(1, response.getSampleSteps().size());
        Assert.assertEquals("testHost to dest_layer_test -> Duration 0 (msec)", response.getSampleSteps().get(0));
    }

    @Test
    public void buildResponse_WithBodySteps() {

        ArrayList<String> exampleofRemoteResponse = new ArrayList<>(Arrays.asList("Collector response"));
        SampleRequestIn requestIn = buildRequestIn(exampleofRemoteResponse, HttpStatus.OK);

        SampleStepsResponse response = instance.buildSampleSteps(requestIn);

        Assert.assertEquals(2, response.getSampleSteps().size());
        Assert.assertEquals("Collector response", response.getSampleSteps().get(0));
        Assert.assertEquals("testHost to dest_layer_test -> Duration 0 (msec)", response.getSampleSteps().get(1));
    }

    @Test
    public void buildResponse_WhenRemoteCallFails() {
        SampleRequestIn requestIn = buildRequestIn(null, HttpStatus.SERVICE_UNAVAILABLE);

        try {
            SampleStepsResponse response = instance.buildSampleSteps(requestIn);
        } catch (IllegalStateException ex) {
            Assert.assertEquals("java.lang.IllegalStateException: Unexpected response status: 503 SERVICE_UNAVAILABLE", ex.toString());
            return;
        }

        Assert.fail("In this test an IllegalStateException is expected, why is not thrown?");
    }

    private SampleRequestIn buildRequestIn(List<String> remoteResponseBody, HttpStatus remoteResponseStatus) {
        SampleRequestIn requestIn = new SampleRequestIn();
        requestIn.setSampleValue("TestValue");

        SampleStepsResponse sampleStepsResponse = new SampleStepsResponse();
        if (remoteResponseBody != null) {
            sampleStepsResponse.setSampleSteps(remoteResponseBody);
        }
        ResponseEntity<SampleStepsResponse> nextLayerCallResponse = ResponseEntity.status(remoteResponseStatus).body(sampleStepsResponse);

        Mockito.when(myTemplate.postForEntity(
                        Mockito.anyString(),
                        Mockito.any(SampleRequestPropagated.class),
                        Mockito.any(Class.class)))
                .thenReturn(nextLayerCallResponse);
        return requestIn;
    }

}