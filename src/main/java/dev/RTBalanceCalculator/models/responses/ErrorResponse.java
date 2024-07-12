package dev.RTBalanceCalculator.models.responses;

import lombok.Data;

@Data
public class ErrorResponse extends Response{
    private String message;
    private String code;

    public ErrorResponse(String message, String code){
        this.message = message;
        this.code = code;
    }

    public ErrorResponse(){};
}
