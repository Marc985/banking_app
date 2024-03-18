package com.prog3.exam.service;

import com.prog3.exam.entity.Account;
import com.prog3.exam.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
   public String createAccount(Account account){
       LocalDate date=LocalDate.now();
       LocalDate birthdate= account.getBirthdate().toLocalDate();
       long age= Math.abs(ChronoUnit.DAYS.between(date,birthdate));

       if(age>=21){
           accountRepository.save(account);
           return "your account is successfully created";
       }
       else
           return "you must be at least 21 years old to create an account";
   }
   }
