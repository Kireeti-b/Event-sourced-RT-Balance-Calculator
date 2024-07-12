package dev.RTBalanceCalculator.models.exceptions;

import lombok.Data;

@Data
public class InvalidRequestException extends Exception{
    private String errorMessage = "Bad Request, Object received is improper, try again with a proper request";
    private String errorCode = "0";

    public InvalidRequestException(String errorMessage, String errorCode){
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public InvalidRequestException(){}
}
