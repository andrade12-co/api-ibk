package com.examen.springreactoribk.controller;


import com.examen.springreactoribk.model.Money;
import com.examen.springreactoribk.service.IMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/money")
public class MoneyController {

    @Autowired
    private IMoneyService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Money>>> findAll() {
        Flux<Money> fx= service.findAll();
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Money>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(e -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(e)
                )
        .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Money>> save(@RequestBody Money money, final ServerHttpRequest req){

       double tipocambiod = 3.83;
      double tipocambios = 0.26;
       double montodestino= 0.0;

        if (money.getMonedaOrigen().equals("SOLES")){
            montodestino = money.getMonto()*tipocambios;
            money.setTipocambio(tipocambios);

        } else if (money.getMonedaOrigen().equals("DOLARES")){
            montodestino = money.getMonto()*tipocambiod;
            money.setTipocambio(tipocambiod);

        } else {
              throw new RuntimeException("Debe ingresar soles o dolares");
        }
    //    money.setCambiomoneda( money.getMontoOrigen() * money.getMonto() * money.getMontoDestino());
        money.setMontoDestino(montodestino);

     return service.save(money)

             .map(e ->ResponseEntity
                     .created(URI.create(req.getURI().toString().concat("/").concat(e.getId())))
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(e)
             );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Money>> update(@PathVariable("id")String id, @RequestBody Money money) {

        Mono<Money> monoBody = Mono.just(money);
        Mono<Money> monoBD = service.findById(id);


        return monoBD.zipWith(monoBody, (bd, d) -> {
            bd.setId(id);
            bd.setMontoDestino(d.getMontoDestino());
                    bd.setMonedaOrigen(d.getMonedaOrigen());
                    bd.setMonedaDestino(d.getMonedaDestino());
                    bd.setMonto(d.getMonto());
            return bd;
        })
            .flatMap(service::update)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
     }

     @DeleteMapping("/{id}")
     public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id){
        return service.findById(id)
                .flatMap(e -> service.delete(e.getId())
                        .thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }



}
