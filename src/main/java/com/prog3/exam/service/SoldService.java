package com.prog3.exam.service;

import com.prog3.exam.entity.InterestRate;
import com.prog3.exam.entity.Loan;
import com.prog3.exam.entity.Sold;
import com.prog3.exam.entity.SoldWithLoan;
import com.prog3.exam.repository.InterestRateRepository;
import com.prog3.exam.repository.LoanRepository;
import com.prog3.exam.repository.SoldRepository;
import com.prog3.exam.repository.SoldWithLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Service
public class SoldService {
    @Autowired
    SoldRepository soldRepository;
    @Autowired
    LoanRepository loanRepository;

    @Autowired
    InterestRateRepository interestRateRepository;
    @Autowired
    SoldWithLoanRepository soldWithLoanRepository;

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

        SoldWithLoan soldWithLoan=soldWithLoanRepository.getActualSoldAndLoan(accountNumber);
        Loan loan =loanRepository.getLastLoan(accountNumber);
        LocalDate loanDate= loan.getLoan_date().toLocalDate();
        InterestRate interestRate=interestRateRepository.getInterestRate();
        float interestPercentage=0;
        double loanValue= soldWithLoan.getLoan();

        LocalDate currentDate= LocalDate.now();
       long retardationDay=ChronoUnit.DAYS.between(loanDate,currentDate);
    if(loanValue>0){
       double loanInterest= calculateLoanInterest(loanValue,interestRate,retardationDay);
        soldWithLoan.setLoanInterest(loanInterest);
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
