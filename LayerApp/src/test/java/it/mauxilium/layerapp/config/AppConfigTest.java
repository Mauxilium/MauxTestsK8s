package it.mauxilium.layerapp.config;

import brave.sampler.Sampler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@TestPropertySource("classpath:application-unittest.yml")
public class AppConfigTest {

    @Autowired
    @Qualifier("layer-name")
    private String layerName;

    @Autowired
    @Qualifier("dest-layer")
    private String destLayer;

    @Autowired
    private Sampler testSampler;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void getLayerName() {
        Assert.assertEquals("Mauxilium", layerName);
    }

    @Test
    public void getDestLayer() {
        Assert.assertEquals("Moon", destLayer);
    }

    @Test
    public void getSampler() {
        Assert.assertEquals(Sampler.ALWAYS_SAMPLE, testSampler);
    }

    @Test
    public void getRestTemplate() {
        Assert.assertNotNull(restTemplate);
    }

}