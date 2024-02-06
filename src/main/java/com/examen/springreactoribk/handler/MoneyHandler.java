package com.examen.springreactoribk.handler;


import com.examen.springreactoribk.model.Money;
import com.examen.springreactoribk.service.IMoneyService;
import com.examen.springreactoribk.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class MoneyHandler {

    @Autowired
    private IMoneyService service;

    /*@Autowired
    private Validator validator;*/

    @Autowired
    private RequestValidator requestValidator;

    public Mono<ServerResponse> findAll(ServerRequest req) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Money.class);
    }

    public Mono<ServerResponse> findById(ServerRequest req) {
        String id = req.pathVariable("id");

        return service.findById(id) //Mono<Money>
                .flatMap(c -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(c))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest req) {
        Mono<Money> monoMoney = req.bodyToMono(Money.class);

        /*return monoMoney
                .flatMap(c -> {
                    Errors errors = new BeanPropertyBindingResult(c, Money.class.getName());
                    validator.validate(c, errors);

                    if(errors.hasErrors()){
                        return Flux.fromIterable(errors.getFieldErrors())
                                .map(error -> new ValidationDTO(error.getField(), error.getDefaultMessage()))
                                .collectList()
                                .flatMap(list -> ServerResponse
                                        .badRequest()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(fromValue(list))
                                );
                    }else{
                        return service.save(c)
                                .flatMap(cdb -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(c.getId())))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(fromValue(cdb))
                                );
                    }
                });*/

        return monoMoney
                .flatMap(requestValidator::validate)
                .flatMap(service::save)
                .flatMap(c -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(c))
                );

    }

    public Mono<ServerResponse> update(ServerRequest req) {
        String id = req.pathVariable("id");

        Mono<Money> monoMoney = req.bodyToMono(Money.class);
        Mono<Money> monoDB = service.findById(id);

        return monoDB
                .zipWith(monoMoney, (db, cl) -> {
                    db.setId(id);
                    return db;
                })
                .flatMap(requestValidator::validate)
                .flatMap(service::update)
                .flatMap(c -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(c))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest req){
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(c -> service.delete(c.getId())
                        .then(ServerResponse.noContent().build())
                )
                //.defaultIfEmpty(ServerResponse.notFound().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
