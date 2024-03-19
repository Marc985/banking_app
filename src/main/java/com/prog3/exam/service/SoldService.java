package com.prog3.exam.service;

import com.prog3.exam.entity.InterestRate;
import com.prog3.exam.entity.Sold;
import com.prog3.exam.entity.SoldWithLoan;
import com.prog3.exam.repository.InterestRateRepository;
import com.prog3.exam.repository.SoldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SoldService {
    @Autowired
    SoldRepository soldRepository;


    @Autowired
    InterestRateRepository interestRateRepository;


    public Sold getSoldByDate(long idAccount, Date date){
        List<Sold> solds= soldRepository.findAll();
        Sold soldByDate=null;
        for(Sold sold:solds){
            if((sold.getDate().before(date)) || sold.getDate().equals(date) && (soldByDate==null || sold.getDate().after(soldByDate.getDate())))
            soldByDate=sold;
        }

            return soldByDate;


    }

public SoldWithLoan getCurrentSolds(long accountNumber){
        Sold lastSold=soldRepository.findLastSoldByIdAccount(accountNumber);



        LocalDate loanDate=lastSold.getDate().toLocalDate();
        InterestRate interestRate=interestRateRepository.getInterestRate();
        float interestPercentage=0;
        double soldValue=lastSold.getBalance();

        LocalDate currentDate= LocalDate.now();
       long retardationDay=Math.abs(ChronoUnit.DAYS.between(loanDate,currentDate));

    SoldWithLoan soldWithLoan=new SoldWithLoan();
    if(soldValue<0){
        double loanValue=Math.abs(soldValue);
       double loanInterest= calculateLoanInterest(loanValue,interestRate,retardationDay);
        soldWithLoan.setSold(0);
        soldWithLoan.setLoanInterest(loanInterest);
        soldWithLoan.setLoan(loanValue);
        }
    else{
        soldWithLoan.setSold(soldValue);
        soldWithLoan.setLoan(0);
        soldWithLoan.setLoanInterest(0);
    }
        return soldWithLoan;
}

private double calculateLoanInterest(double loanValue,InterestRate interestRate,long retardationDate){
        double loanInterest=0;
        if(retardationDate<=7){
            loanInterest=calculateInterestForFirst7days(loanValue,interestRate,retardationDate);
        }
        else
            loanInterest=calculateInterestAfter7days(loanValue,interestRate,retardationDate);

        return  loanInterest;
}
private double calculateInterestForFirst7days(double loanValue,InterestRate interestRate,long retardationDay){
   double interestPercentage=interestRate.getFirst7days();
    return ((loanValue*interestPercentage)/100)*retardationDay;

}
private  double calculateInterestAfter7days(double loanValue,InterestRate interestRate,long retardationDay){
   double interestPercentage=interestRate.getAfter7days();
    double before7days=((interestRate.getFirst7days()*loanValue)/100)*7;
    float daysAfter7=retardationDay-7;

    return  ((interestPercentage*loanValue)/100)*daysAfter7+before7days;
}



}
