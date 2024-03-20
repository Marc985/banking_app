package com.prog3.exam.controller;

import com.prog3.exam.entity.MultipleTransfert;
import com.prog3.exam.entity.Transfert;
import com.prog3.exam.service.TransfertService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransfertController {
    @Autowired
    TransfertService transfertService;
    @PostMapping("/transfert")
    public String transfert(@RequestBody Transfert transfert){

     return    transfertService.transfertMoney(transfert);

    }

    @PutMapping("/transfert/cancel")
    public String cancelTransfert(@RequestParam String reference){
        return transfertService.cancelTransfert(reference);
    }
    @PostMapping("/transfertMultiple")
    public String multipleTransfert(@RequestBody MultipleTransfert multipleTransfert){
        return transfertService.multipleTransfert(multipleTransfert);
    }
}
