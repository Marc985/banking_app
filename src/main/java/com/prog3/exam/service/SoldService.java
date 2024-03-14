package com.prog3.exam.service;

import com.prog3.exam.entity.Sold;
import com.prog3.exam.repository.SoldCrudOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class SoldService {
    @Autowired
SoldCrudOperation soldCrudOperation;
    public Sold getSoldByDate(long idAccount, Date date){
        List<Sold> solds=soldCrudOperation.findAll();
        Sold soldByDate=null;
        for(Sold sold:solds){
            if((sold.getDate().before(date)) || sold.getDate().equals(date) && (soldByDate==null || sold.getDate().after(soldByDate.getDate())))
            soldByDate=sold;
        }

            return soldByDate;


    }
}
