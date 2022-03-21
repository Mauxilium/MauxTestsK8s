package it.mauxilium.collectorapp.service;

import it.mauxilium.collectorapp.model.SampleRequestIn;
import it.mauxilium.collectorapp.model.SampleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class SampleService {

    private final String layerName;
    private final String hostName;

    @Autowired
    public SampleService(@Qualifier("layer-name") String layerName,
                         @Qualifier("host-name") String hostName) {
        this.layerName = layerName;
        this.hostName = hostName;
    }

    public SampleResponse buildResponse(SampleRequestIn sample) {
        SampleResponse resp = new SampleResponse();
        String receivedSample = "Sample: " + sample.getSampleValue();
        String responseStep = String.format("%s (%s) ends pipeline", layerName, hostName);
        resp.setSampleSteps(Arrays.asList(receivedSample, responseStep));

        log.debug("Returns a list with {} elements", resp.getSampleSteps().size());

        return resp;
    }
}
