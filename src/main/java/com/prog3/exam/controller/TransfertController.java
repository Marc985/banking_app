package com.prog3.exam.controller;

import com.prog3.exam.entity.MultipleTransfert;
import com.prog3.exam.entity.Transfert;
import com.prog3.exam.repository.TransfertRepository;
import com.prog3.exam.service.TransfertService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfert")
public class TransfertController {
    @Autowired
    TransfertService transfertService;
    @Autowired
    TransfertRepository transfertRepository;
    @PostMapping("/")
    public String transfert(@RequestBody Transfert transfert){

     return    transfertService.transfertMoney(transfert);

    }

    @PutMapping("/cancel")
    public String cancelTransfert(@RequestParam String reference){
        return transfertService.cancelTransfert(reference);
    }
    @PostMapping("/multiple")
    public String multipleTransfert(@RequestBody MultipleTransfert multipleTransfert){
        return transfertService.multipleTransfert(multipleTransfert);
    }
    @GetMapping("/list/{accountNumber}")
    public List<Transfert> getTransfertList(@PathVariable long accountNumber){
        return transfertRepository.findALlTransfertByAccount(accountNumber);
    }

}
