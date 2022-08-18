package com.dcl.practise.reactive.bankingapp.banking.init;

import com.dcl.practise.reactive.bankingapp.banking.kafka.producer.TransactionProducer;
import com.dcl.practise.reactive.bankingapp.banking.model.Transaction;
import com.dcl.practise.reactive.bankingapp.banking.model.User;
import com.dcl.practise.reactive.bankingapp.banking.repository.TransactionRepository;
import com.dcl.practise.reactive.bankingapp.banking.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class AppInit implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    TransactionProducer producer;

    @Autowired
    private TransactionRepository transactionRepo;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Application started");
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<User>> typeReferenceUser = new TypeReference<List<User>>(){};
        InputStream streamUser = TypeReference.class.getResourceAsStream("/json/users.json");
        try{
            List<User> userList = mapper.readValue(streamUser,typeReferenceUser);
            userList.forEach( u -> {
                User user = userRepo.findByCardId(u.getCardId())
                        .share()
                        .block();
                if(Objects.isNull(user)){
                    u.setLastUpdated(new Date());
                    userRepo.save(u).subscribe();
                }
            });
            log.info("User saved!");
        }catch(IOException ex){
            log.error("Unable to save user: "+ex.getMessage());
        }

        TypeReference<List<Transaction>> typeRefTransactions  =  new TypeReference<List<Transaction>>() {};
        InputStream transactionStreamReader = TypeReference.class.getResourceAsStream("/json/transactions.json");
        try{
            List<Transaction> transactionList = mapper.readValue(transactionStreamReader,typeRefTransactions);
            transactionList.forEach(t -> t.setLastUpdated(new Date()));
            transactionList.forEach(t -> producer.sendMessage(t));
            transactionList.forEach(t -> transactionRepo.save(t).subscribe());
            log.info("Transaction(s) send and saved :> "+transactionList.size());
        }catch(IOException ex){
            log.error("Unable to dispatch transactions to kafka topic : " + ex.getMessage());
        }
    }
}

