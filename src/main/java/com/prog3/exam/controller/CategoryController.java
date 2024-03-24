package com.prog3.exam.controller;

import com.prog3.exam.entity.CategorySum;
import com.prog3.exam.repository.CategorySumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    CategorySumRepository categorySumRepository;
    @GetMapping("/categorySum/{accountNumber}")
    public List<CategorySum> getCategoryAmountSum(@PathVariable long accountNumber, @RequestParam Date startDate, @RequestParam Date endDate){
        return categorySumRepository.getCategoryAmountSum(accountNumber,startDate,endDate);
    }
}
