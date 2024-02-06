package com.examen.springreactoribk.service.impl;

import com.examen.springreactoribk.model.Money;
import com.examen.springreactoribk.repo.IGenericRepo;
import com.examen.springreactoribk.repo.IMoneyRepo;
import com.examen.springreactoribk.service.IMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MoneyServiceImpl extends CRUDImpl<Money, String> implements IMoneyService {

    @Autowired
    private IMoneyRepo repo;

    @Override
    protected IGenericRepo getRepo() {
        return repo;
    }
}
