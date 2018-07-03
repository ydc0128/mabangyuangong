package com.example.administrator.ydxcfwpt.Bean;

/**
 * Created by Administrator on 2017/8/18.
 */

public class Error {

    private String ErrorStr;

    @Override
    public String toString() {
        return "Error{" +
                "ErrorStr='" + ErrorStr + '\'' +
                '}';
    }

    public Error() {
    }

    public Error(String errorStr) {

        ErrorStr = errorStr;
    }

    public String getErrorStr() {

        return ErrorStr;
    }

    public void setErrorStr(String errorStr) {
        ErrorStr = errorStr;
    }
}
