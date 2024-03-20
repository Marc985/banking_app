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



            double newSoldValue=receveirSold.getBalance()+amount;
            updateSold(recipientAccount,newSoldValue,effectiveDate);

            addTransaction(recipientAccount,effectiveDate,reason,"credit",amount);



   return transfertRepository.saveTransfert(newTransfert);
    }

    public String transfertMoney(Transfert transfert){

     Sold senderSold=soldRepository.findLastSoldByIdAccount(transfert.getSenderAccount());
     Sold recipientSold=soldRepository.findLastSoldByIdAccount(transfert.getRecipientAccount());
     if(senderSold.getBalance()<transfert.getAmount())
         return "insufficient sold";

     String reference="VIR_"+ LocalDateTime.now();

        if(transfert.getIsSameBank()){


        updateSold(transfert.getSenderAccount(),(senderSold.getBalance()-transfert.getAmount()),transfert.getEffectiveDate());

        updateSold(transfert.getRecipientAccount(),(recipientSold.getBalance()+transfert.getAmount()),transfert.getEffectiveDate());

         addTransaction(transfert.getSenderAccount(),transfert.getEffectiveDate(),transfert.getReason(),"debit",transfert.getAmount());

         addTransaction(transfert.getRecipientAccount(),transfert.getEffectiveDate(),transfert.getReason(),"credit",transfert.getAmount());

         addTransfertHistory(reference,transfert.getAmount(),transfert.getReason(),transfert.getSenderAccount(),transfert.getRegistrationDate(),transfert.getEffectiveDate(),"success");


     }
     else {


        addTransfertHistory(reference,transfert.getAmount(),transfert.getReason(),transfert.getSenderAccount(),transfert.getRegistrationDate(),transfert.getEffectiveDate(),"pending");




     }
     return  reference;
 }
    @Scheduled(cron = "0 * * * * *")
 private  void processExternalTransfert(){
     List<Transfert> externalTransfertToProcess=transfertRepository.findByEffectiveDate(Date.valueOf(LocalDate.now()));

     for(Transfert transfert:externalTransfertToProcess){
         if(transfert.getStatus().equals("pending")){
            Sold sold= soldRepository.findLastSoldByIdAccount(transfert.getSenderAccount());
             updateSold(transfert.getSenderAccount(),sold.getBalance()-transfert.getAmount(),transfert.getEffectiveDate());
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
 private void addTransfertHistory(String reference,double amount,String reason,
                                  long senderAccountNumber,Date registrationDate,
                                  Date effectiveDate,String status
 ){
     Transfert newTransfert=new Transfert();
     newTransfert.setReference(reference);
     newTransfert.setAmount(amount);
     newTransfert.setReason(reason);
     newTransfert.setRecipientAccount(senderAccountNumber);
     newTransfert.setRegistrationDate(registrationDate);
     newTransfert.setEffectiveDate(effectiveDate);
     newTransfert.setStatus(status);
     transfertRepository.saveTransfert(newTransfert);
 }

 public String cancelTransfert(String reference){
      return   transfertRepository.updateStatus("canceled",reference);
 }
}
