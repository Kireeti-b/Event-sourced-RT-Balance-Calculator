package dev.codescreen.models.exceptions;

import lombok.Data;

@Data
public class DBConnectionException extends Exception{
    private String errorMessage = "Transaction could not be committed. DB connection issue. Try again";
    private String errorCode = "2";
}
