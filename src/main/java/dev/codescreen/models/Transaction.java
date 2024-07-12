package dev.codescreen.models;

import dev.codescreen.models.enums.DebitOrCredit;
import dev.codescreen.models.enums.ResponseCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document
@Data
public class Transaction {
    @Id
    private String transactionId;
    private String userId;
    private String messageId;
    private DebitOrCredit debitOrCredit;
    private ResponseCode responseCode;
    private Amount transactionAmount;

    public Transaction(String userId,String messageId, DebitOrCredit debitOrCredit,Amount transactionAmount){
        this.userId = userId;
        this.messageId = messageId;
        this.debitOrCredit = debitOrCredit;
        this.transactionAmount = transactionAmount;
    }

    public Transaction(String userId,String messageId, DebitOrCredit debitOrCredit,Amount transactionAmount, ResponseCode responseCode){
        this.userId = userId;
        this.messageId = messageId;
        this.debitOrCredit = debitOrCredit;
        this.transactionAmount = transactionAmount;
        this.responseCode = responseCode;
    }

    public Transaction(){}
}
