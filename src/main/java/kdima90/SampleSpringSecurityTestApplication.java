package kdima90;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("kdima90.property")
public class SampleSpringSecurityTestApplication {

    static void main(String[] args) {
        SpringApplication.run(SampleSpringSecurityTestApplication.class, args);
    }

}