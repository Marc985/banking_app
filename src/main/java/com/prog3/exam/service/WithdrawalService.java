package com.prog3.exam.service;

import com.prog3.exam.entity.Account;
import com.prog3.exam.entity.Sold;
import com.prog3.exam.repository.AccountCrudOperation;
import com.prog3.exam.repository.SoldCrudOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Date;

@Service
public class WithdrawalService {
    @Autowired
    AccountCrudOperation accountCrudOperation;
    @Autowired
    SoldCrudOperation soldCrudOperation;
    public String makeWithdrawal(long idAccount, double amount, Date date){
        Account account=accountCrudOperation.findAccountById(idAccount);
        Sold sold=soldCrudOperation.findLastSoldByIdAccount(idAccount);
        double actualSold=sold.getBalance();
        double allowedCredit=account.getMonthlyNetIncome()/3;
        if((allowedCredit+actualSold)>=amount){
            Sold newSold=new Sold();
            newSold.setIdSold(30);

            newSold.setLoansinterest(1);
            newSold.setDate(date);
            newSold.setAccountId(idAccount);
            if(actualSold>=amount){
                newSold.setBalance(actualSold-amount);
                newSold.setLoans(0);
            }

            else{
                newSold.setLoans(actualSold-amount);
                newSold.setBalance(0);
            }
            soldCrudOperation.save(newSold);
            return "success";
        }
    else
        return "the allowed credit + your actual sold don't cover the withdrawal";
    }
}
