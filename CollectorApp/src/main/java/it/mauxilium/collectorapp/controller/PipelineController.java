package it.mauxilium.collectorapp.controller;

import it.mauxilium.collectorapp.model.SampleRequestIn;
import it.mauxilium.collectorapp.model.SampleResponse;
import it.mauxilium.collectorapp.service.SampleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/sample")
public class PipelineController {

    private final SampleService sampleService;

    @PostMapping
    public ResponseEntity<SampleResponse> pushSample(@RequestBody SampleRequestIn request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sampleService.buildResponse(request));
    }
}
