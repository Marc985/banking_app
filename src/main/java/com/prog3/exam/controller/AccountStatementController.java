package com.prog3.exam.controller;

import com.prog3.exam.entity.AccountStatement;
import com.prog3.exam.repository.AccountStatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountStatementController {
    @Autowired
    AccountStatementRepository accountStatementRepository;
    @GetMapping("/test/{accountNumber}")
    public List<AccountStatement> getALl(@PathVariable long accountNumber){
        return accountStatementRepository.getAccountStatement(accountNumber);
    }
}