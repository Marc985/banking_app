package com.prog3.exam.service;

import com.prog3.exam.entity.Account;
import com.prog3.exam.entity.Loan;
import com.prog3.exam.entity.Sold;
import com.prog3.exam.repository.AccountRepository;
import com.prog3.exam.repository.LoanRepository;
import com.prog3.exam.repository.SoldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class WithdrawalService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    SoldRepository soldRepository;
    @Autowired
    LoanRepository loanRepository;


    public String makeWithdrawal(long idAccount, double amount, Date date){
        Account account = accountRepository.findAccountById(idAccount);
        if (!account.getIsEligible()) {
            return "This account is not eligible to make withdrawal";
        }

        Loan lastLoan = loanRepository.getLastLoan(idAccount);
        if (lastLoan.getValue() != 0) {
            return "You should pay back your last loan";
        }

        Sold sold = soldRepository.findLastSoldByIdAccount(idAccount);
        double actualSold = sold.getBalance();
        double allowedCredit = account.getMonthlyNetIncome() / 3;
        if ((allowedCredit + actualSold) < amount) {
            return "The allowed credit + your actual sold don't cover the withdrawal";
        }

    processWithDrawal(idAccount,actualSold,amount,date);
        return "success";
    }
    public void processWithDrawal(long idAccount,double actualSold,double amount,Date date){
        if(actualSold>=amount){
         updateSold(date,idAccount,(actualSold-amount));
        }
        else{
            newLoan(date,idAccount,(amount-actualSold));
        }
    }

     private void updateSold(Date date,long idAccount,double value){
         Sold newSold=new Sold();

         newSold.setDate(date);
         newSold.setAccountId(idAccount);
         newSold.setBalance(value);
         soldRepository.save(newSold);
     }

     private void newLoan(Date date,long idAccount,double value){
         Loan loan=new Loan();
         loan.setLoan_date(date);
         loan.setValue(value);
         loan.setIdAccount(idAccount);
         loanRepository.save(loan);

     }
}
