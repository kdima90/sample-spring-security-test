package kdima90.property;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "sample.spring.security.test.credentials")
@Validated
public record SampleSpringSecurityTestCredentials(@NotBlank String username, @NotBlank String password) {
}
