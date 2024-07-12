package dev.codescreen.controllers;

import dev.codescreen.models.Transaction;
import dev.codescreen.models.requests.Request;
import dev.codescreen.models.responses.Response;
import dev.codescreen.models.utilities.Ping;
import dev.codescreen.services.TransactionServices.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @RequestMapping("/ping")
    public String ping(){
        try{
            return new Ping().getServerTime();
        }catch (Exception ex){
            return "Ping failed. Server unavailable.";
        }
    }

    @PutMapping("/authorization/{messageId}")
    public Response authorize(@PathVariable("messageId") String messageId, @RequestBody Request authRequest){
        return transactionService.authorization(messageId, authRequest);
    }

    @PutMapping("/load/{messageId}")
    public Response load(@PathVariable("messageId") String messageId, @RequestBody Request loadRequest){
        return transactionService.load(messageId, loadRequest);
    }

    @GetMapping("transaction/all")
    public List<Transaction> getAllTransactions(){
        return transactionService.getAllTransactions();
    }

    @GetMapping("transaction/{userId}")
    public List<Transaction> getTransactionsByUser(@PathVariable("userId") String userId){
        return transactionService.getTransactionsByUser(userId);
    }
}

