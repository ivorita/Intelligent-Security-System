package com.antelope.android.bottomnavigation.data;

public class DS<T> {

    private int errno;
    private T data;
    private String error;
    public DS() { }

    /*public DS(int errno, String error, Picture data) {
        this.errno = errno;
        this.error = error;
        this.data = data;
    }*/

    public DS(int errno, T data, String error) {
        this.errno = errno;
        this.data = data;
        this.error = error;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    /*public Picture getData() {
        return data;
    }

    public void setData(Picture data) {
        this.data = data;
    }*/

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
