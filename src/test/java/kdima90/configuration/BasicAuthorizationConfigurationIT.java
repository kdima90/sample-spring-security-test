package kdima90.configuration;

import kdima90.property.SampleSpringSecurityTestCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.test.web.servlet.client.assertj.RestTestClientResponse;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.client.assertj.RestTestClientResponse.from;

@SpringBootTest
class BasicAuthorizationConfigurationIT {

    private RestTestClient restTestClient;
    @Autowired
    private SampleSpringSecurityTestCredentials sampleSpringSecurityTestCredentials;


    @BeforeEach
    void setup(WebApplicationContext webApplicationContext){
        /**+
         *
         * Since spring security is not automatically provided, it needs to be configured.
         * Therefore we need here the dependency
         * <dependency>
         *     <groupId>org.springframework.security</groupId>
         *     <artifactId>spring-security-test</artifactId>
         *     <scope>test</scope>
         * </dependency>
         */
        restTestClient = RestTestClient
                .bindToApplicationContext(webApplicationContext)
                .configureServer(defaultMockMvcBuilder -> defaultMockMvcBuilder
                        .apply(SecurityMockMvcConfigurers.springSecurity())).build();
    }

    @Test
    void actuatorShouldBeNotProtected(){
        RestTestClientResponse response = from(restTestClient.get().uri("/actuator/health").exchange());
        assertThat(response).hasStatusOk().bodyJson().extractingPath("$.status").isEqualTo("UP");
    }

    @Test
    void helloEndpointShouldReturn200(){
        RestTestClientResponse response = from(restTestClient.get().uri("/hello").headers(
                httpHeaders -> httpHeaders.setBasicAuth(
                        sampleSpringSecurityTestCredentials.username(),
                        sampleSpringSecurityTestCredentials.password())).exchange());
        assertThat(response).hasStatusOk().bodyText().isEqualTo("hello security world");
    }

    @Test
    void helloEndpointShouldReturn401(){
        RestTestClientResponse response = from(restTestClient.get().uri("/hello").headers(
                httpHeaders -> httpHeaders.setBasicAuth(
                        "Malory",
                        "pwd")).exchange());
        assertThat(response).hasStatus(HttpStatus.UNAUTHORIZED);
    }
}