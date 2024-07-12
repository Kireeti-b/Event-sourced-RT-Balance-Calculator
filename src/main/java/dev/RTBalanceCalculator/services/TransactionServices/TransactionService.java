package dev.RTBalanceCalculator.services.TransactionServices;

import dev.RTBalanceCalculator.models.Transaction;
import dev.RTBalanceCalculator.models.requests.Request;
import dev.RTBalanceCalculator.models.responses.Response;
import java.util.List;

public interface TransactionService {

    Response authorization(String messageId, Request request);
    Response load(String messageId, Request request);
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionsByUser(String userId);

}
