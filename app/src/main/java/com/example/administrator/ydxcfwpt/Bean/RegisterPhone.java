package com.example.administrator.ydxcfwpt.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/18.
 */

public class RegisterPhone implements Serializable{

    private int W_LSID;                 //当未注册的手机号，返回一个唯一数字
    private String W_Tel;               //手机号
    private String W_IdentityID;        //验证码

    @Override
    public String toString() {
        return "RegisterPhone{" +
                "W_IdentityID='" + W_IdentityID + '\'' +
                ", W_LSID=" + W_LSID +
                ", W_Tel='" + W_Tel + '\'' +
                '}';
    }

    public String getW_IdentityID() {
        return W_IdentityID;
    }

    public void setW_IdentityID(String w_IdentityID) {
        W_IdentityID = w_IdentityID;
    }

    public int getW_LSID() {
        return W_LSID;
    }

    public void setW_LSID(int w_LSID) {
        W_LSID = w_LSID;
    }

    public String getW_Tel() {
        return W_Tel;
    }

    public void setW_Tel(String w_Tel) {
        W_Tel = w_Tel;
    }

    public RegisterPhone() {

    }
}
