package com.jachaofen.utils;


import com.jachaofen.entities.basic.ErrorExtend;

/**
 * Created by steve on 17-7-15.
 * 处理异常类
 */
public class UncheckExceptions extends RuntimeException{

    private int statusCode;
    private ErrorExtend error;

    public UncheckExceptions(int code, ErrorExtend error){
        this.statusCode = code;
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ErrorExtend getError() {
        return error;
    }

    public void setError(ErrorExtend error) {
        this.error = error;
    }

}
