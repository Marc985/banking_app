package com.prog3.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SoldWithLoan implements Serializable {
   private double sold;
   private double loan;
   private double loanInterest;
}
