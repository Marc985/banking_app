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
public class Sold {

    double balance;
    Date date;
    long accountId;


}
