package com.prog3.exam.service;

import com.prog3.exam.entity.*;
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

            double newSoldValue=receveirSold.getBalance()+amount;
            updateSold(recipientAccount,newSoldValue,effectiveDate);

            addTransaction(recipientAccount,effectiveDate,reason,"credit",amount);



   return "entrance transfert proccessed successfully";
    }

    public String transfertMoney(Transfert transfert,boolean isSameBank){

     Sold senderSold=soldRepository.findLastSoldByIdAccount(transfert.getSenderAccount());

     if(senderSold.getBalance()<transfert.getAmount())
         return "insufficient sold";

     String reference="VIR_"+ LocalDateTime.now();

        if(isSameBank){

              internalTransfert(reference,transfert.getSenderAccount(),senderSold.getBalance(), transfert);




     }
     else {
             externalTransfert(reference,transfert.getSenderAccount(), transfert);




     }
     return  reference;
 }
 private void internalTransfert(String reference,long senderAccount,double senderSold,Transfert transfert){
     Sold recipientSold=soldRepository.findLastSoldByIdAccount(transfert.getRecipientAccount());

     updateSold(senderAccount,(senderSold-transfert.getAmount()),transfert.getRegistrationDate());

     updateSold(transfert.getRecipientAccount(),(recipientSold.getBalance()+transfert.getAmount()),transfert.getRegistrationDate());

     addTransaction(senderAccount,transfert.getRegistrationDate(),transfert.getReason(),"debit",transfert.getAmount());

     addTransaction(transfert.getRecipientAccount(),transfert.getRegistrationDate(),transfert.getReason(),"credit",transfert.getAmount());

     addTransfertHistory(reference,transfert.getAmount(),transfert.getReason(),senderAccount,transfert.getRegistrationDate(),transfert.getRegistrationDate(),"success");
 }
 private void externalTransfert(String reference, long senderAccount,Transfert transfert){

     addTransfertHistory(reference,transfert.getAmount(),transfert.getReason(),senderAccount,transfert.getRegistrationDate(),transfert.getEffectiveDate(),"pending");

 }

 public String multipleTransfert(MultipleTransfert multipleTransfert){
   Sold senderSold= soldRepository.findLastSoldByIdAccount(multipleTransfert.getSenderAccount());
   double totalAmount=multipleTransfert.getAmount()*multipleTransfert.getReceivers().size();
   if(senderSold.getBalance()<(totalAmount))
       return "cannot process the transfert because your sold is insufficient";
   updateSold(multipleTransfert.getSenderAccount(),senderSold.getBalance()-totalAmount,multipleTransfert.getRegisterDate());
   addTransaction(multipleTransfert.getSenderAccount(),multipleTransfert.getRegisterDate(),multipleTransfert.getReason(),"debit",totalAmount);

   String reference="VIR_"+LocalDateTime.now();
   addTransfertHistory(reference,totalAmount,multipleTransfert.getReason(),multipleTransfert.getSenderAccount(),multipleTransfert.getRegisterDate(),multipleTransfert.getRegisterDate(),"success");
        for(long recipientAccount:multipleTransfert.getReceivers()){
            Sold recipientSold=soldRepository.findLastSoldByIdAccount(recipientAccount);

            updateSold(recipientAccount,recipientSold.getBalance()+multipleTransfert.getAmount(),multipleTransfert.getRegisterDate());
            addTransaction(recipientAccount,multipleTransfert.getRegisterDate(),multipleTransfert.getReason(),"credit",multipleTransfert.getAmount());
        }
        return "transfert successfully processed";
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
     TransfertModal newTransfert=new TransfertModal();
     newTransfert.setReference(reference);
     newTransfert.setAmount(amount);
     newTransfert.setReason(reason);
     newTransfert.setRegisterDate(registrationDate);
     newTransfert.setEffecitveDate(effectiveDate);
     newTransfert.setStatus(status);
     transfertRepository.saveTransfert(newTransfert,senderAccountNumber);
 }

 public String cancelTransfert(String reference){
      return   transfertRepository.updateStatus("canceled",reference);
 }
}
