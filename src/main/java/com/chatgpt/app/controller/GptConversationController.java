package com.chatgpt.app.controller;

import com.chatgpt.app.dto.ChatGptRequest;
import com.chatgpt.app.dto.ChatGptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/gpt/conversation")
@CrossOrigin(origins = "*")
public class GptConversationController {

    @Value("${openai.model.name}")
    private String model;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.chat.completion.url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient webClient;

    @GetMapping
    public String completeChat(@RequestBody String prompt) {
        ChatGptResponse chatGptResponse = null;
        ChatGptRequest chatGptRequest = new ChatGptRequest(model, prompt, false);
        chatGptResponse = restTemplate.postForObject(url, chatGptRequest, ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }

    @PostMapping("/completions")
    public Flux<ServerSentEvent<String>> completeConversation(@RequestBody String prompt) {
        ChatGptRequest chatGptRequest = new ChatGptRequest(model, prompt, true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
        Flux<ServerSentEvent<String>> streamedResponse =  webClient.method(HttpMethod.POST)
                .uri(url).bodyValue(chatGptRequest)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve().bodyToFlux(ChatGptResponse.class)
                .map(response -> ServerSentEvent.builder(response.getChoices().get(0).getDelta().getContent()).build())
                .doOnCancel(() -> {
                    // Perform any cleanup or cancellation logic if needed
                    System.out.println("doOnCancel triggered");
                })
                .onErrorResume(throwable -> {
                    // Handle any errors that occur during the stream
                    System.out.println("onError triggered");
                    return Mono.empty();
                });
        System.out.println(streamedResponse);
        return streamedResponse;
    }
}
