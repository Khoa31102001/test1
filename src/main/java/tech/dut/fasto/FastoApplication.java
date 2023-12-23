package tech.dut.fasto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tech.dut.fasto.config.properties.FastoProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FastoProperties.class })
public class FastoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastoApplication.class, args);
    }

}
