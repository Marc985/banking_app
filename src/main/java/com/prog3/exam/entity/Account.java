package com.prog3.exam.entity;

import com.prog3.exam.idgenerator.UniqueNumberGenerator;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

 private    Long accountNumber=UniqueNumberGenerator.generateUniqueId();
  private String clientName;
   private String clientLastName;
   private Date birthdate;
   private double monthlyNetIncome;
   private boolean isEligible;

    public Account(String clientName, String clientLastName, Date birthdate, double monthlyNetIncome){
        this.clientName=clientLastName;
        this.clientLastName=clientLastName;
        this.birthdate=birthdate;
        this.monthlyNetIncome=monthlyNetIncome;
    }
public boolean getIsEligible(){
        return isEligible;
}

}
