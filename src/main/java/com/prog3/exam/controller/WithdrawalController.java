package com.prog3.exam.controller;

import com.prog3.exam.entity.Withdrawal;
import com.prog3.exam.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WithdrawalController {
    @Autowired
    WithdrawalService withdrawalService;

    @PostMapping("/account/{id}/withdrawals")
    public String withdrawalSold(@PathVariable long id,@RequestBody Withdrawal withdrawal){
    return     withdrawalService
                .makeWithdrawal(id,withdrawal.getAmount(),withdrawal.getDate());

    }
}
