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
import java.time.temporal.ChronoField;
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

        long timeBeforeUSec = LocalDateTime.now(internalClock).getLong(ChronoField.MICRO_OF_DAY);
        ResponseEntity<SampleStepsResponse> nextLevelResponse = restTemplate.postForEntity(requestUrl, reqOut, SampleStepsResponse.class);
        long timeAfterUSec = LocalDateTime.now(internalClock).getLong(ChronoField.MICRO_OF_DAY);

        return evaluateResponse(nextLevelResponse, timeBeforeUSec, timeAfterUSec);
    }

    private String buildDestUrl() {
        return "http://" + destLayer + "/api/v1/sample";
    }

    private SampleRequestPropagated buildRequest(SampleRequestIn input) {
        SampleRequestPropagated reqOut = new SampleRequestPropagated();
        reqOut.setSampleValue(input.getSampleValue());
        return reqOut;
    }

    private SampleStepsResponse evaluateResponse(ResponseEntity<SampleStepsResponse> nextLevelResponse,
                                                 long timeBeforeUSec, long timeAfterUSec) {
        if (nextLevelResponse.getStatusCode() == HttpStatus.OK) {
            log.debug("Timing (USec): before={}, After={}", timeBeforeUSec, timeAfterUSec);
            String logDurationMSec = String.valueOf(Math.floor((timeAfterUSec - timeBeforeUSec) / 1_000.0));

            String msgToAdd = String.format("%s (%s) to %s -> Duration %s (msec)",
                    layerName,
                    hostName,
                    destLayer,
                    logDurationMSec);

            SampleStepsResponse resp = new SampleStepsResponse();
            if (nextLevelResponse.getBody() != null && nextLevelResponse.getBody().getSampleSteps() != null) {
                resp.setSampleSteps(nextLevelResponse.getBody().getSampleSteps());
                resp.getSampleSteps().add(1, msgToAdd);
            } else {
                resp.setSampleSteps(Collections.singletonList(msgToAdd));
            }

            log.debug("Returns a list with {} elements", resp.getSampleSteps().size());

            return resp;
        } else {
            throw new IllegalStateException("Unexpected response status: " + nextLevelResponse.getStatusCode());
        }
    }
}
