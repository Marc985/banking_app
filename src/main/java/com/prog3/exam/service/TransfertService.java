package com.prog3.exam.service;

import com.prog3.exam.entity.Sold;
import com.prog3.exam.entity.Transaction;
import com.prog3.exam.entity.Transfert;
import com.prog3.exam.repository.AccountRepository;
import com.prog3.exam.repository.SoldRepository;
import com.prog3.exam.repository.TransactionRepository;
import com.prog3.exam.repository.TransfertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransfertService {
    @Autowired
    SoldRepository soldRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransfertRepository transfertRepository;
    public String supplyAccount(long recipientAccount,double amount,Date effectiveDate,Date registrationDate,
                                String reason){
        Sold receveirSold=soldRepository.findLastSoldByIdAccount(recipientAccount);
        String reference="VIR_"+ LocalDateTime.now();
        LocalDate currentDate=LocalDate.now();
        LocalDate effective=effectiveDate.toLocalDate();
        Transfert newTransfert=new Transfert();
        newTransfert.setReference(reference);
        newTransfert.setAmount(amount);
        newTransfert.setReason(reason);
        newTransfert.setRecipientAccount(recipientAccount);
        newTransfert.setRegistrationDate(registrationDate);
        newTransfert.setEffectiveDate(effectiveDate);
        newTransfert.setStatus("pending");


       // newTransfert.set
       // transfertRepository.saveTransfert()
        //if(currentDate.isAfter(effective) || currentDate.isEqual(effective)){
            double newSoldValue=receveirSold.getBalance()+amount;
            updateSold(recipientAccount,newSoldValue,effectiveDate);

            addTransaction(recipientAccount,effectiveDate,reason,"credit",amount);

        //}


   return transfertRepository.saveTransfert(newTransfert);
    }
double senderBalance=0;
    public String transfertMoney(double amount, long senderAccountNumber, long recipientAccountNumber
 , Date effectiveDate,Date registrationDate,String reason,String status,boolean isSameBank
                              ){

     Sold senderSold=soldRepository.findLastSoldByIdAccount(senderAccountNumber);
     Sold recipientSold=soldRepository.findLastSoldByIdAccount(recipientAccountNumber);
        senderBalance = senderSold.getBalance();
     if(senderSold.getBalance()<amount)
         return "insufficient sold";
     if(isSameBank){
         effectiveDate=registrationDate;

        updateSold(senderAccountNumber,(senderSold.getBalance()-amount),effectiveDate);

        updateSold(recipientAccountNumber,(recipientSold.getBalance()+amount),effectiveDate);

         addTransaction(senderAccountNumber,effectiveDate,reason,"debit",amount);

         addTransaction(recipientAccountNumber,effectiveDate,reason,"credit",amount);



     }
     else {

         String reference="VIR_"+ LocalDateTime.now();
         Transfert newTransfert=new Transfert();
         newTransfert.setReference(reference);
         newTransfert.setAmount(amount);
         newTransfert.setReason(reason);
         newTransfert.setRecipientAccount(senderAccountNumber);
         newTransfert.setRegistrationDate(registrationDate);
         newTransfert.setEffectiveDate(effectiveDate);
         newTransfert.setStatus("pending");


         transfertRepository.saveTransfert(newTransfert);


     }
     return  null;
 }
    @Scheduled(cron = "0 * * * * *")
 private  void processExternalTransfert(){
     List<Transfert> externalTransfertToProcess=transfertRepository.findByEffectiveDate(Date.valueOf(LocalDate.now()));
     for(Transfert transfert:externalTransfertToProcess){
         if(transfert.getStatus().equals("pending")){
             updateSold(transfert.getSenderAccount(),senderBalance-transfert.getAmount(),transfert.getEffectiveDate());
             transfertRepository.updateStatus("success",transfert.getReference());
         }
     }
 }
 private void addTransaction(long accountNumber,Date transactionDate,String reason,String type,double amount){
        Transaction newTransaction=new Transaction();
        newTransaction.setDate(transactionDate);
        newTransaction.setType(type);
        newTransaction.setReason(reason);
        newTransaction.setAmount(amount);
        newTransaction.setAccountNumber(accountNumber);
        transactionRepository.saveTransaction(newTransaction);
 }
 private  void updateSold(long accountId,double balance,Date date){
        Sold newSold=new Sold();
        newSold.setAccountId(accountId);
        newSold.setDate(date);
        newSold.setBalance(balance);
        soldRepository.save(newSold);
 }
}
