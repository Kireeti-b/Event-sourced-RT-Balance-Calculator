package dev.codescreen.services.TransactionServices;

import dev.codescreen.models.Transaction;
import dev.codescreen.models.requests.Request;
import dev.codescreen.models.responses.Response;
import java.util.List;

public interface TransactionService {

    Response authorization(String messageId, Request request);
    Response load(String messageId, Request request);
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionsByUser(String userId);

}
