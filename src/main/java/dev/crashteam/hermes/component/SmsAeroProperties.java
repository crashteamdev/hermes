package dev.crashteam.hermes.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.integration.sms-aero")
public class SmsAeroProperties {

    private String url;

}
