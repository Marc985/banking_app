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
public class Account {
    Long accountNumber;
    String clientName;
    String clientLastName;
    Date birthdate;
    double monthlyNetIncome;

    public Account(String clientName, String clientLastName, Date birthdate, double monthlyNetIncome){
        this.clientName=clientLastName;
        this.clientLastName=clientLastName;
        this.birthdate=birthdate;
        this.monthlyNetIncome=monthlyNetIncome;
    }


}
