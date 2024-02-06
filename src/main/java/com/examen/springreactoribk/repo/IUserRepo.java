package com.examen.springreactoribk.repo;

import com.examen.springreactoribk.model.User;
import reactor.core.publisher.Mono;

public interface IUserRepo extends IGenericRepo<User, String>{

    //SELECT * FROM USER U WHERE U.USERNAME = ?
    //{username : ?}
    //DerivedQueries
    Mono<User> findOneByUsername(String username);
}
