package it.mauxilium.layerapp.config;

import brave.sampler.Sampler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Clock;

@Configuration
public class AppConfig {

    @Value("${LAYER_NAME:layerX}")
    private String layerName;

    @Bean
    @Qualifier("layer-name")
    public String getLayerName() {
        return layerName;
    }

    @Value("${DEST_LAYER:unknow}")
    private String destinationLayer;

    @Bean
    @Qualifier("dest-layer")
    public String getDestLayer() {
        return destinationLayer;
    }

    @Bean
    @Qualifier("host-name")
    public String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknow host ("+ e.toString() +")";
        }
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Clock getInternalClock() {
        return Clock.systemDefaultZone();
    }

    //Collects data from Sleuth and provides it to Zipkin Client
    @Bean
    public Sampler samplerOb() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
