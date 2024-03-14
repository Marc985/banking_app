package com.prog3.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sold {
    int idSold;
    double balance;
    double loans;
    double loansinterest;
    Date date;
    long accountId;

    public Sold(double balance,double loans,double loanInterest){
        this.balance=balance;
        this.loans=loans;
        this.loansinterest=loanInterest;
    }
}
