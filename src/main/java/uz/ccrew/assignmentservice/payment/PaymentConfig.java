package uz.ccrew.assignmentservice.payment;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
