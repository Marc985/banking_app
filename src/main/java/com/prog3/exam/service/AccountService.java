package com.prog3.exam.service;

import com.prog3.exam.entity.Account;
import com.prog3.exam.repository.AccountCrudOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AccountService {
    @Autowired
    AccountCrudOperation accountCrudOperation;
   public String createAccount(Account account){
       LocalDate date=LocalDate.now();
       LocalDate birthdate= account.getBirthdate().toLocalDate();
       long age= ChronoUnit.DAYS.between(date,birthdate);

       if(age>=21){
           accountCrudOperation.save(account);
           return "your account is successfully created";
       }
       else
           return "you must be at least 21 years old to create an account";
   }
   }
