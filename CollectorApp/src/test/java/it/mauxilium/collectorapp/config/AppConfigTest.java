package it.mauxilium.collectorapp.config;

import brave.sampler.Sampler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@TestPropertySource("classpath:application-unittest.yml")
public class AppConfigTest {

    @Autowired
    @Qualifier("layer-name")
    private String layerName;

    @Autowired
    @Qualifier("host-name")
    private String hostName;

    @Autowired
    private Sampler testSampler;

    @Test
    public void getLayerName() {
        Assert.assertEquals("Mauxilium", layerName);
    }

    @Test
    public void getSampler() {
        Assert.assertEquals(Sampler.ALWAYS_SAMPLE, testSampler);
    }

    @Test
    public void getHostName() {
        String expectedHostName = null;
        try {
            expectedHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            expectedHostName = "Unknow host (" + e.toString() + ")";
        }

        Assert.assertEquals(expectedHostName, this.hostName);
    }

}