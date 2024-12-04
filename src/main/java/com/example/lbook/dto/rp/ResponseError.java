package com.example.lbook.dto.rp;


public class ResponseError<T> extends ResponseData<T> {
    public ResponseError(int status, String message) {
        super(status, message);
    }
}
