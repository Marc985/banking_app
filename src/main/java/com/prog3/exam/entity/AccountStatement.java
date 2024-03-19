package com.prog3.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountStatement implements Serializable {
    private Date date;
    private String reference;
    private String reason;
    private double credit;
    private double debit;
    private double sold;
}
