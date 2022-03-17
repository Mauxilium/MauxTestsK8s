package it.mauxilium.layerapp.controller;

import it.mauxilium.layerapp.model.SampleRequestIn;
import it.mauxilium.layerapp.model.SampleStepsResponse;
import it.mauxilium.layerapp.service.SampleService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@WebMvcTest(PipelineController.class)
public class PipelineControllerTest {

    @MockBean
    private SampleService sampleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void callExecution() throws Exception {
        final SampleStepsResponse expectedResponse = new SampleStepsResponse();
        expectedResponse.setSampleSteps(Arrays.asList("Mauxilium test", "Step one"));
        Mockito.when(sampleService.buildSampleSteps(Mockito.any(SampleRequestIn.class))).thenReturn(expectedResponse);
        MockHttpServletRequestBuilder requestToPerform = MockMvcRequestBuilders
                .post("/api/v1/sample")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"samplePath\": [\"Mauxilium test\"] }");

        mockMvc.perform(requestToPerform)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sampleSteps.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sampleSteps", Matchers.containsInAnyOrder("Mauxilium test", "Step one")))
                .andReturn();
    }
}