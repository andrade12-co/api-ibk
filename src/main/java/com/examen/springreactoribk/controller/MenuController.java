package com.examen.springreactoribk.controller;


import com.examen.springreactoribk.model.Menu;
import com.examen.springreactoribk.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private IMenuService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Menu>>> listAll(){
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities)
                .map(roles -> {
                    String rolesString = roles.stream().map(Object::toString).collect(Collectors.joining(",")); // "ADMIN,USER"
                    String[] strings = rolesString.split(",");
                    return service.getMenus(strings);
                })
                .flatMap(fx -> Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx)));
    }
}
