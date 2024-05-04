package com.wenh.baselibrary.network;

/**
 * @Author Wenh·Wong
 * @Date 2024/5/4 9:10
 */
public class HttpBody<T> {

    private T data;
    private String message;
    private int state;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


}
