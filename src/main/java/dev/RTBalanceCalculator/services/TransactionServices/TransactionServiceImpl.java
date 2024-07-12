package dev.RTBalanceCalculator.services.TransactionServices;

import dev.RTBalanceCalculator.models.Amount;
import dev.RTBalanceCalculator.models.Currency;
import dev.RTBalanceCalculator.models.Transaction;
import dev.RTBalanceCalculator.models.enums.DebitOrCredit;
import dev.RTBalanceCalculator.models.enums.ResponseCode;
import dev.RTBalanceCalculator.models.exceptions.DBConnectionException;
import dev.RTBalanceCalculator.models.exceptions.InvalidRequestException;
import dev.RTBalanceCalculator.models.repositories.TransactionRepository;
import dev.RTBalanceCalculator.models.requests.Request;
import dev.RTBalanceCalculator.models.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    @Transactional(rollbackFor = DBConnectionException.class)
    public Response authorization(String messageId, Request request) {
        AuthorizationResponse response = new AuthorizationResponse();
        try {
            this.validateRequest(request);
            String userId = request.getUserId();
            Amount transactionAmount = request.getTransactionAmount();

            // Checking if debit is possible based on balance
            boolean debitPossible = sufficientBalance(userId, transactionAmount);
            DebitOrCredit debitOrCredit = transactionAmount.getDebitOrCredit();
            Transaction newTransaction = new Transaction(userId, messageId, debitOrCredit,transactionAmount);

            try {
                if (debitPossible && debitOrCredit == DebitOrCredit.DEBIT) {
                    // Debit is possible and it's a debit transaction
                    this.saveTransaction(newTransaction, ResponseCode.APPROVED);
                    response.setResponseCode(ResponseCode.APPROVED);
                } else {
                    // Debit not possible or it's not a debit transaction
                    this.saveTransaction(newTransaction, ResponseCode.DECLINED);
                    response.setResponseCode(ResponseCode.DECLINED);
                }
            } catch (DBConnectionException ex) {
                // Transaction saving error
                return new ErrorResponse(ex.getErrorMessage(), ex.getErrorCode());
            }

            //Set the response body of Transaction response
            this.setResponseBody(response,request);
        }catch (InvalidRequestException ex){
            //Handling Null pointer
            return new ErrorResponse(ex.getErrorMessage(), ex.getErrorCode());
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = DBConnectionException.class)
    public Response load(String messageId, Request loadRequest) {
        LoadResponse response = new LoadResponse();
        DebitOrCredit debitOrCredit = loadRequest.getTransactionAmount().getDebitOrCredit();
        try{
            this.validateRequest(loadRequest);

            // Process credit transaction only
            try {
                Transaction newTransaction = new Transaction(loadRequest.getUserId(), messageId, debitOrCredit, loadRequest.getTransactionAmount());
                if (debitOrCredit == DebitOrCredit.CREDIT) {
                    this.saveTransaction(newTransaction,ResponseCode.APPROVED);
                } else {
                    // For debit transactions, just set the userId
                    this.saveTransaction(newTransaction, ResponseCode.DECLINED);
                    return new ErrorResponse("Current endpoint only supports Credit transactions.", "1");
                }
            }catch (DBConnectionException ex) {
                // Transaction saving error
                return new ErrorResponse(ex.getErrorMessage(), ex.getErrorCode());
            }

            //Set the response body of Transaction response
            this.setResponseBody(response,loadRequest);
        }
        catch (InvalidRequestException ex){
            //Handle Null pointers
            String errorMessage = "";
            try{
                this.saveTransaction(new Transaction(loadRequest.getUserId(),messageId, debitOrCredit,loadRequest.getTransactionAmount()), ResponseCode.DECLINED);
            }catch (DBConnectionException dbConnectionException){
                errorMessage += dbConnectionException.getErrorMessage();
            }
            return new ErrorResponse(ex.getErrorMessage() + errorMessage, ex.getErrorCode());
        }
        return response;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> getTransactionsByUser(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    private void saveTransaction(Transaction transaction, ResponseCode responseCode) throws DBConnectionException {
        try{
            transaction.setResponseCode(responseCode);
            transactionRepository.save(transaction);
        }catch (Exception ex){
            throw new DBConnectionException();
        }
    }

    private void validateRequest(Request request) throws InvalidRequestException{
        if(request.getUserId() == null || request.getMessageId() == null || request.getTransactionAmount() == null){
            throw new InvalidRequestException();
        }
        Amount requestAmount = request.getTransactionAmount();
        if(requestAmount.getAmount() == null || requestAmount.getCurrency() == null || requestAmount.getDebitOrCredit() == null){
            throw new InvalidRequestException();
        }
        if(requestAmount.getAmount().compareTo(BigDecimal.valueOf(0)) < 0 ){
            throw new InvalidRequestException("Provided Amount is a negative value, make sure provided amount is positive", "3");
        }
    }
    private void setResponseBody(TransactionResponse response, Request request){
        response.setUserId(request.getUserId());
        response.setBalance(calculateBalance(request.getUserId(),request.getTransactionAmount().getCurrency()));
        response.getBalance().setCurrency(request.getTransactionAmount().getCurrency());
        response.getBalance().setDebitOrCredit(request.getTransactionAmount().getDebitOrCredit());
        response.setMessageId(request.getMessageId());
    }

    private Amount calculateBalance(String userId,Currency currency) {
        List<Transaction> userTransactions = transactionRepository.findByUserId(userId);
        // Filter based on currency and only collect Approved transactions
        Amount balance = new Amount();
        balance.setAmount(BigDecimal.valueOf(0));
        if (userTransactions.size() <= 0){
            return balance;
        }

        userTransactions = userTransactions.stream()
                .filter(transaction -> transaction.getTransactionAmount().getCurrency() == currency && transaction.getResponseCode() == ResponseCode.APPROVED)
                .collect(Collectors.toList());
        // Add all CREDIT transactions
        userTransactions.stream()
                .filter(transaction -> transaction.getDebitOrCredit() == DebitOrCredit.CREDIT)
                .forEach(transaction -> balance.setAmount(balance.getAmount().add(transaction.getTransactionAmount().getAmount())));
        // Subtract all DEBIT transactions
        userTransactions.stream()
                .filter(transaction -> transaction.getDebitOrCredit() == DebitOrCredit.DEBIT)
                .forEach(transaction -> balance.setAmount(balance.getAmount().subtract(transaction.getTransactionAmount().getAmount())));
        return balance;
    }

    private boolean sufficientBalance(String userId, Amount amount) {
        return calculateBalance(userId,amount.getCurrency()).getAmount().compareTo(amount.getAmount()) >= 0;
    }
}
