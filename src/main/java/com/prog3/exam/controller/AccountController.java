package com.prog3.exam.controller;

import com.prog3.exam.entity.Account;
import com.prog3.exam.entity.Sold;
import com.prog3.exam.repository.AccountCrudOperation;
import com.prog3.exam.service.AccountService;
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

    @Autowired
    AccountService accountService;
   @GetMapping("/accounts")
    public List<Account> getAccounts(){
        return  accountCrudOperation.findAll();
    }
    @PutMapping("/account")
    public Account updateAccount(@PathParam("id") BigDecimal id, @RequestBody Account account){
        return accountCrudOperation.updateAccount(account);
    }

    @PostMapping("/account")
    public String createAccount(@RequestBody Account accouont){
        return accountService.createAccount(accouont);
    }
@GetMapping("/account/{id}")
    public Account findById(@PathVariable  long id){
       return accountCrudOperation.findAccountById(id);
}
@PutMapping("/account/{idAccount}/eligibility")
    public String setEligibility(@PathVariable float idAccount,@RequestParam boolean isEligible){
       return accountCrudOperation.updateEligibility(idAccount,isEligible);
}
}
