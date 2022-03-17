package it.mauxilium.layerapp.controller;

import it.mauxilium.layerapp.model.SampleRequestIn;
import it.mauxilium.layerapp.model.SampleStepsResponse;
import it.mauxilium.layerapp.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/sample")
public class PipelineController {

    @Autowired
    private SampleService sampleService;

    @PostMapping
    public ResponseEntity<SampleStepsResponse> pushSample(@RequestBody SampleRequestIn sample) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sampleService.buildSampleSteps(sample));
    }
}
