package com.example.common.entitty;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseEntity<T> {

    private Object status;
    private String errorMessage;
    private T result;

    public ResponseEntity(Object status, String errorMessage){
        this.status = status;
        this.errorMessage = errorMessage;
        this.result = null;
    }

    public ResponseEntity(Object status, T result){
        this.status = status;
        this.errorMessage = null;
        this.result = result;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
