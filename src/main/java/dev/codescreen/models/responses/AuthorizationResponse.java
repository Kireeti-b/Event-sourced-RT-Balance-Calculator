package dev.codescreen.models.responses;

import dev.codescreen.models.enums.ResponseCode;
import lombok.Data;

@Data
public class AuthorizationResponse extends TransactionResponse {
    private ResponseCode responseCode;
}
