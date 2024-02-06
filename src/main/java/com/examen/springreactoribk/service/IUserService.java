package com.examen.springreactoribk.service;


import reactor.core.publisher.Mono;

public interface IUserService extends ICRUD<com.examen.springreactoribk.model.User, String>{

    Mono<com.examen.springreactoribk.model.User> saveHash(com.examen.springreactoribk.model.User user);
    Mono<com.examen.springreactoribk.security.User> searchByUser(String user);

}
