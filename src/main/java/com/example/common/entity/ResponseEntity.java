package com.example.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonInclude( JsonInclude.Include.NON_EMPTY )
@Data
@AllArgsConstructor
public class ResponseEntity< T > {

    private Object status;
    private String errorMessage;
    private T result;

    public ResponseEntity( Object status, String errorMessage ) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.result = null;
    }

    public ResponseEntity( Object status, T result ) {
        this.status = status;
        this.errorMessage = null;
        this.result = result;
    }
}
