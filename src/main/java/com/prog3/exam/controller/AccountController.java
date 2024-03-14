package com.prog3.exam.controller;

import com.prog3.exam.entity.Account;
import com.prog3.exam.repository.AccountCrudOperation;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin
public class AccountController {
    @Autowired
    AccountCrudOperation accountCrudOperation;
   @GetMapping("/accounts")
    public List<Account> getAccounts(){
        return  accountCrudOperation.findAll();
    }
    @PutMapping("/account")
    public Account updateAccount(@PathParam("id") BigDecimal id, @RequestBody Account account){
        return accountCrudOperation.updateAccount(account);
    }

    @PostMapping("/account")
    public Account createAccount(@RequestBody Account accouont){
        return accountCrudOperation.save(accouont);
    }
}
