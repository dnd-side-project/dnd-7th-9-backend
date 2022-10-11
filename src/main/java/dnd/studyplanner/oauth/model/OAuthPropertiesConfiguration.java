package dnd.studyplanner.oauth.model;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {OAuthProperties.class})
public class OAuthPropertiesConfiguration {
}
