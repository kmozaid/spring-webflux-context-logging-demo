package com.zaid.spring.webflux.loggingdemo.router;

import com.zaid.spring.webflux.loggingdemo.handler.HelloHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.util.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static com.zaid.spring.webflux.loggingdemo.util.LogHelper.MDC_CONTEXT;

@Configuration
public class HelloRouter {

    @Bean
    public RouterFunction<ServerResponse> helloRoute(HelloHandler helloHandler) {
        return RouterFunctions.route()
                .GET("/hello", helloHandler::hello)
                .GET("/hello-again", helloHandler::helloAgain)
                .GET("/hello-failure", helloHandler::helloFailure)
                .filter(loggingContextFilter())
                .build();
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> loggingContextFilter() {
        return (request, handler) -> handler.handle(request).contextWrite(loggingContext(request));
    }

    private Function<Context, Context> loggingContext(ServerRequest request) {
        Map<String, String> mdcContext = new HashMap<>();
        mdcContext.put("sessionId", UUID.randomUUID().toString());

        // Add all requests headers to context map.
        // Generally we need only few to be part of log message
        mdcContext.putAll(request.headers().asHttpHeaders().toSingleValueMap());
        return reactiveContext -> reactiveContext.put(MDC_CONTEXT, mdcContext);
    }

}
