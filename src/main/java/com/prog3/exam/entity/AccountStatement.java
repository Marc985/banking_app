package com.prog3.exam.entity;

import java.io.Serializable;
import java.sql.Date;


public class AccountStatement implements Serializable {
    private Date date;
    private String reference;
    private String reason;
    private double credit;
    private double debit;
    private double sold;
}
