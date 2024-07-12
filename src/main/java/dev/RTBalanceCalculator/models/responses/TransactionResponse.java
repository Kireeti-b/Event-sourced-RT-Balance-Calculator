package dev.RTBalanceCalculator.models.responses;

import dev.RTBalanceCalculator.models.Amount;
import lombok.Data;

@Data
public class TransactionResponse extends Response{
    private String userId;
    private String messageId;
    private Amount balance;

    public TransactionResponse(){
        this.balance = new Amount();
    }
}
