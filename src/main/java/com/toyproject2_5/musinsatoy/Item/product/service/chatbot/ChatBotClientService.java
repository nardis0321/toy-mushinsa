package com.toyproject2_5.musinsatoy.Item.product.service.chatbot;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

//RestTemplate은 동기식으로 통신  - 응답을 받을 때 까지 blocking , deprecated
// 어짜피 같이쓰면 MVC로 동작한다고 함. -> 프론트에서 바로 플라스크로.
public class ChatBotClientService {

    //현재 프론트에서 바로 flask로 요청
    String url = "http://localhost:5000/";
    public Mono<?> chatBot(String message){


        WebClient webClient = WebClient.builder()
                .baseUrl(url)
//                .defaultCookie("cookieKey","cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<?> resMono = webClient.post()
                .uri(url)
                .bodyValue(message)
//                .body(BodyInserters.fromValue(message))
                .retrieve()
                .bodyToMono(ChatDto.class);


//        resMono.subscribe(System.out::println);// 사용시 요청이 2번 보내짐.


        return resMono;
    }

}

