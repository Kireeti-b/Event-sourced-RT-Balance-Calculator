package dev.RTBalanceCalculator.models.responses;

import dev.RTBalanceCalculator.models.enums.ResponseCode;
import lombok.Data;

@Data
public class AuthorizationResponse extends TransactionResponse {
    private ResponseCode responseCode;
}
