package com.prog3.exam.entity;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.prog3.exam.idgenerator.UniqueNumberGenerator;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private  Long accountNumber=UniqueNumberGenerator.generateUniqueId();
    private String accountName;
    private String idClient;
    private boolean isEligible;


public boolean getIsEligible(){
        return isEligible;
}

}
