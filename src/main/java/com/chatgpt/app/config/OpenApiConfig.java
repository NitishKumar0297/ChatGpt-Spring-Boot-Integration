package com.chatgpt.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenApiConfig {

    @Value("${openai.api.key}")
    private String apikey;

    @Bean
    public RestTemplate template(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + apikey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    @Bean
    public WebClient webClient() {
        // Configure the WebClient with desired options
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector())
                .build();
    }
}
