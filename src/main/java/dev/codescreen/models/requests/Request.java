package dev.codescreen.models.requests;

import dev.codescreen.models.Amount;
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
