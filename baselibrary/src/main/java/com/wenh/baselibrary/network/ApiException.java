package com.wenh.baselibrary.network;

/**
 * @Author Wenh·Wong
 * @Date 2024/5/4 9:15
 */
public class ApiException extends Exception{
    private int code;
    private final String message;

    public ApiException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiException(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
