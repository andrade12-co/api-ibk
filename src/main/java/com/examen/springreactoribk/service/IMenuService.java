package com.examen.springreactoribk.service;


import com.examen.springreactoribk.model.Menu;
import reactor.core.publisher.Flux;

public interface IMenuService extends ICRUD<Menu, String>{

    Flux<Menu> getMenus(String[] roles);
}
