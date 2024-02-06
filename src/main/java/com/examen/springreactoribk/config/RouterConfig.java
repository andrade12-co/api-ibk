package com.examen.springreactoribk.config;

import com.examen.springreactoribk.handler.MoneyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

@Configuration
public class RouterConfig {



    @Bean
    public RouterFunction<ServerResponse> routesMoney(MoneyHandler handler) {
        return route(GET("/v2/money"), handler::findAll) //req -> handler.findAll(req)
                .andRoute(GET("/v2/money/{id}"), handler::findById)
                .andRoute(POST("/v2/money"), handler::create)
                .andRoute(PUT("/v2/money/{id}"), handler::update)
                .andRoute(DELETE("/v2/money/{id}"), handler::delete);
    }

}
