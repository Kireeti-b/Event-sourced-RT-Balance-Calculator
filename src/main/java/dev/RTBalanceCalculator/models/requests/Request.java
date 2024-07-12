package dev.RTBalanceCalculator.models.requests;

import dev.RTBalanceCalculator.models.Amount;
import lombok.Data;

@Data
public class Request {
    private String userId;
    private String messageId;
    private Amount transactionAmount;

    public Request(String userId, String messageId, Amount transactionAmount) {
        this.userId = userId;
        this.messageId = messageId;
        this.transactionAmount = transactionAmount;
    }

    public Request(){}
}
