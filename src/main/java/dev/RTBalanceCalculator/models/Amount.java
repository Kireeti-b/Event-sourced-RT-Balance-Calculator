package dev.RTBalanceCalculator.models;

import dev.RTBalanceCalculator.models.enums.DebitOrCredit;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Amount {
    private BigDecimal amount;
    private Currency currency;
    private DebitOrCredit debitOrCredit;

    public Amount(BigDecimal amount, Currency currency, DebitOrCredit debitOrCredit) {
        this.amount = amount;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
    }

    public Amount(){}
}