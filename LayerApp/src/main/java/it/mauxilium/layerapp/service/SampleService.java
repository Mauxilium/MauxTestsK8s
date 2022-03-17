package it.mauxilium.layerapp.service;

import it.mauxilium.layerapp.model.SampleRequestIn;
import it.mauxilium.layerapp.model.SampleRequestPropagated;
import it.mauxilium.layerapp.model.SampleStepsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Service
public class SampleService {

    private final String layerName;
    private final String destLayer;
    private final String hostName;
    private final Clock internalClock;
    private final RestTemplate restTemplate;

    @Autowired
    public SampleService(
            @Qualifier("layer-name") String layerName,
            @Qualifier("dest-layer") String destLayer,
            @Qualifier("host-name") String hostName,
            Clock internalClock,
            RestTemplate restTemplate) {
        this.layerName = layerName;
        this.destLayer = destLayer;
        this.hostName = hostName;
        this.internalClock = internalClock;
        this.restTemplate = restTemplate;
    }

    public SampleStepsResponse buildSampleSteps(SampleRequestIn input) {
        String requestUrl = buildDestUrl();
        SampleRequestPropagated reqOut = buildRequest(input);

        long timeBefore = LocalDateTime.now(internalClock).getNano();
        ResponseEntity<SampleStepsResponse> nextLevelResponse = restTemplate.postForEntity(requestUrl, reqOut, SampleStepsResponse.class);
        long timeAfter = LocalDateTime.now(internalClock).getNano();

        return evaluateResponse(nextLevelResponse, timeBefore, timeAfter);
    }

    private String buildDestUrl() {
        return "http://" + destLayer + "/api/v1/sample";
    }

    private SampleRequestPropagated buildRequest(SampleRequestIn input) {
        SampleRequestPropagated reqOut = new SampleRequestPropagated();
        reqOut.setSampleValue(input.getSampleValue());
        return reqOut;
    }

    private SampleStepsResponse evaluateResponse(ResponseEntity<SampleStepsResponse> nextLevelResponse, long timeBeforeNano, long timeAfterNano) {
        if (nextLevelResponse.getStatusCode() == HttpStatus.OK) {
            String logDuration = String.valueOf(Math.round((timeAfterNano - timeBeforeNano) / 1_000_000.0));
            String msgToAdd = hostName + " to " + destLayer + " -> Duration " + logDuration + " (msec)";

            SampleStepsResponse resp = new SampleStepsResponse();
            if (nextLevelResponse.getBody() != null && nextLevelResponse.getBody().getSampleSteps() != null) {
                resp.setSampleSteps(nextLevelResponse.getBody().getSampleSteps());
                resp.getSampleSteps().add(1, msgToAdd);
            } else {
                resp.setSampleSteps(Collections.singletonList(msgToAdd));
            }

            log.info("Returns a list with {} elements", resp.getSampleSteps().size());

            return resp;
        } else {
            throw new IllegalStateException("Unexpected response status: " + nextLevelResponse.getStatusCode());
        }
    }
}
