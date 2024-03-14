package com.prog3.exam.controller;

import com.prog3.exam.entity.Sold;
import com.prog3.exam.repository.SoldCrudOperation;
import com.prog3.exam.service.SoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
public class SoldController {
    @Autowired
    SoldCrudOperation soldCrudOperation;
    @Autowired
    SoldService soldService;
    public void addSold(Sold sold){
        soldCrudOperation.save(sold);
    }
    @GetMapping("/solds")
    public List<Sold> solds(){
        return  soldCrudOperation.findAll();
    }

    @GetMapping("/sold/{idAccount}")
    public Sold soldByDate(@PathVariable long idAccount, @RequestParam("date") Date date){
        return  soldService.getSoldByDate(idAccount,date);
    }
}
