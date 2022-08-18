package com.dcl.practise.reactive.bankingapp.banking.controller;

import com.dcl.practise.reactive.bankingapp.banking.model.Transaction;
import com.dcl.practise.reactive.bankingapp.banking.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/mapping")
public class TransactionController {

    @Autowired
    TransactionService service;

    @PostMapping(value="/process")
    //public Mono<Transaction> process(@RequestBody Transaction transaction){
    public ResponseEntity process(@RequestBody Transaction transaction){
        log.info("Process transaction with details : {} ", transaction);
        // return service.process(transaction); //sync call
        service.asyncProcess(transaction);
        return ResponseEntity.accepted().build();
    }
}
