package it.mauxilium.collectorapp.config;

import brave.sampler.Sampler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class AppConfig {

    @Value("${LAYER_NAME:layerX}")
    private String layerName;

    @Bean
    @Qualifier("layer-name")
    public String getLayerName() {
        return layerName;
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

    //Collects data from Sleuth and provides it to Zipkin Client
    @Bean
    public Sampler samplerOb() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
