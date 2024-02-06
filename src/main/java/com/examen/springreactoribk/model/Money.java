package com.examen.springreactoribk.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "money")
public class Money {

    @EqualsAndHashCode.Include
    @Id
    private String id;
    @Field
    private int monto;

    @Field
    private double montoDestino;

    @Field
    private String monedaOrigen;

    @Field
    private String monedaDestino;

    @Field
    private double tipocambio;

}
