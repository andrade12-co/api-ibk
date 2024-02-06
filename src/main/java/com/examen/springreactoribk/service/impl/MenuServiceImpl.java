package com.examen.springreactoribk.service.impl;

import com.examen.springreactoribk.model.Menu;
import com.examen.springreactoribk.repo.IGenericRepo;
import com.examen.springreactoribk.repo.IMenuRepo;
import com.examen.springreactoribk.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MenuServiceImpl extends CRUDImpl<Menu, String> implements IMenuService {

    @Autowired
    private IMenuRepo repo;

    @Override
    protected IGenericRepo<Menu, String> getRepo() {
        return repo;
    }

    @Override
    public Flux<Menu> getMenus(String[] roles) {
        return repo.getMenus(roles);
    }

}
