package com.skyros.app.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class AppResponse<T> implements Serializable {

    private Date responseDate;
    private HttpStatus httpStatus;
    private String message;
    private String details;
    private String errorCode;
    private T data;

    public AppResponse(T data) {
        this.data = data;
        this.responseDate = new Date();
        this.httpStatus = HttpStatus.OK;
        this.details = null;
        this.message = null;
        this.errorCode = "0";
    }

    public AppResponse() {
        this.data = null;
        this.responseDate = new Date();
        this.httpStatus = HttpStatus.OK;
        this.message = "success";
        this.details = null;
        this.errorCode = "0";
    }

    public AppResponse(String msg) {
        this.data = null;
        this.responseDate = new Date();
        this.httpStatus = HttpStatus.FORBIDDEN;
        this.message = msg;
        this.details = null;
        this.errorCode = "0";
    }

}
