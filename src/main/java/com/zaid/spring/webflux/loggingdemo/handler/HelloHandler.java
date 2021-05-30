package com.zaid.spring.webflux.loggingdemo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.zaid.spring.webflux.loggingdemo.util.LogHelper.*;

@Slf4j
@Component
public class HelloHandler {

    public Mono<ServerResponse> hello(ServerRequest request) {
        return Mono.deferContextual(context -> {
            // Log message without request information.
            log.info("Serving request");

            // Log message with request information.
            log(context, () -> log.info("Serving request"));

            return ServerResponse.ok()
                    .bodyValue("Hello " + request.queryParam("name").orElse(""));
        });
    }

    public Mono<ServerResponse> helloAgain(ServerRequest request) {
        return ServerResponse.ok()
                .bodyValue("Hello " + request.queryParam("name").orElse(""))
                .doOnEach(logOnNext(response -> log.info("Response status code {}", response.statusCode())));
    }

    public Mono<ServerResponse> helloFailure(ServerRequest request) {
        return ServerResponse.badRequest().build()
                .map(serverResponse -> {
                    // Simulate Mono failure
                    if (serverResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        throw new RuntimeException("a runtime exception occurred while building mono");
                    }
                    return serverResponse;
                }).doOnEach(logOnError(throwable -> log.error("Failed to process request", throwable)));
    }

}
