package com.examen.springreactoribk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "menus")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Menu {

    @Id
    private String id;

    private String icon;
    private String name;
    private String url;
    private List<String> roles;
}
