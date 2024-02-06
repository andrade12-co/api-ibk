package com.examen.springreactoribk.service.impl;

import com.examen.springreactoribk.model.User;
import com.examen.springreactoribk.repo.IGenericRepo;
import com.examen.springreactoribk.repo.IRoleRepo;
import com.examen.springreactoribk.repo.IUserRepo;
import com.examen.springreactoribk.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends CRUDImpl<com.examen.springreactoribk.model.User, String> implements IUserService {

    @Autowired
    private IUserRepo repo;

    @Autowired
    private IRoleRepo rolRepo;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Override
    protected IGenericRepo<User, String> getRepo() {
        return repo;
    }

    @Override
    public Mono<com.examen.springreactoribk.security.User> searchByUser(String username) {
        Mono<com.examen.springreactoribk.model.User> monoUser = repo.findOneByUsername(username);

        List<String> roles = new ArrayList<>();

        return monoUser.flatMap(u -> {
                    return Flux.fromIterable(u.getRoles())
                            .flatMap(rol -> {
                                return rolRepo.findById(rol.getId())
                                        .map(r -> {
                                            roles.add(r.getName());
                                            return r;
                                        });
                            }).collectList().flatMap(list -> {
                                u.setRoles(list);
                                return Mono.just(u);
                            });
                })
                .flatMap(u -> {
                    return Mono.just(new com.examen.springreactoribk.security.User(u.getUsername(), u.getPassword(), u.getStatus(), roles));
                });
    }

    @Override
    public Mono<com.examen.springreactoribk.model.User> saveHash(com.examen.springreactoribk.model.User user) {
        user.setPassword(bcrypt.encode(user.getPassword()));
        return repo.save(user);
    }
}
