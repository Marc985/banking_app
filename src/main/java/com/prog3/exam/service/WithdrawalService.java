package com.prog3.exam.service;

import com.prog3.exam.entity.Account;
import com.prog3.exam.entity.Sold;
import com.prog3.exam.entity.Transaction;
import com.prog3.exam.repository.AccountRepository;
import com.prog3.exam.repository.SoldRepository;
import com.prog3.exam.repository.TransactionRepository;
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
    TransactionRepository transactionRepository;

    public String makeWithdrawal(long idAccount, double amount, Date date){
        Account account = accountRepository.findAccountById(idAccount);
        if (!account.getIsEligible()) {
            return "This account is not eligible to make withdrawal";
        }

       // Loan lastLoan = loanRepository.getLastLoan(idAccount);
        Sold sold = soldRepository.findLastSoldByIdAccount(idAccount);
        double actualSold = sold.getBalance();

        if (actualSold<0) {
            return "You should pay back your last loan";
        }



        double allowedCredit = account.getMonthlyNetIncome() / 3;
        if ((allowedCredit + actualSold) < amount) {
            return "The allowed credit + your actual sold don't cover the withdrawal";
        }

    processWithDrawal(idAccount,actualSold,amount,date);
        return "success";
    }
    private void processWithDrawal(long idAccount,double actualSold,double amount,Date date){

      updateSold(date,idAccount,(actualSold-amount));
      addTransaction(amount,"retrait",date,idAccount);
    }

     private void updateSold(Date date,long idAccount,double value){
         Sold newSold=new Sold();

         newSold.setDate(date);
         newSold.setAccountId(idAccount);
         newSold.setBalance(value);
         soldRepository.save(newSold);



     }
    private void addTransaction(double amount,String reason,Date date,long accountNumber){
        Transaction newTransaction=new Transaction();
        newTransaction.setType("debit");
        newTransaction.setAmount(amount);
        newTransaction.setReason(reason);
        newTransaction.setDate(date);
        newTransaction.setAccountNumber(accountNumber);
        transactionRepository.saveTransaction(newTransaction);
    }

}
