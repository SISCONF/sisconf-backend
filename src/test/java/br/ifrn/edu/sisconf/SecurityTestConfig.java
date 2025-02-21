package br.ifrn.edu.sisconf;

import static org.mockito.Mockito.mock;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@TestConfiguration
public class SecurityTestConfig {
    @Bean
    public JwtDecoder jwtDecoder() {
        return mock(JwtDecoder.class);
    }

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @MockitoBean
    private RabbitAdmin rabbitAdmin;
}
