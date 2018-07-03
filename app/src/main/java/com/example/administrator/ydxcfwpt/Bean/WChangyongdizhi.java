package com.example.administrator.ydxcfwpt.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/19.
 */

public class WChangyongdizhi implements Serializable {
 private int AW_ID;//	是	 	否
private String	AW_WTel	;//	 	 	 	否	 	手机号
private String 	AW_Adress;//	 	 	 	否	 	常用地址
private String	AW_AdressWrite;	 	 	 	//是	 	手写常用地址
private float	AW_Lng;//	 	 	否	 	常用地址经度
private float	AW_Lat;//	 	 	否	 	常用地址纬度
private int	AW_Number;//	 	 	否	1	常用地址次数
private String	AW_Remark;//	 	是	 	备注
public WChangyongdizhi() {}

    @Override
    public String toString() {
        return "WChangyongdizhi{" +
                "AW_ID=" + AW_ID +
                ", AW_WTel='" + AW_WTel + '\'' +
                ", AW_Adress='" + AW_Adress + '\'' +
                ", AW_AdressWrite='" + AW_AdressWrite + '\'' +
                ", AW_Lng=" + AW_Lng +
                ", AW_Lat=" + AW_Lat +
                ", AW_Number=" + AW_Number +
                ", AW_Remark='" + AW_Remark + '\'' +
                '}';
    }

    public int getAW_ID() {
        return AW_ID;
    }

    public void setAW_ID(int AW_ID) {
        this.AW_ID = AW_ID;
    }

    public String getAW_WTel() {
        return AW_WTel;
    }

    public void setAW_WTel(String AW_WTel) {
        this.AW_WTel = AW_WTel;
    }

    public String getAW_Adress() {
        return AW_Adress;
    }

    public void setAW_Adress(String AW_Adress) {
        this.AW_Adress = AW_Adress;
    }

    public String getAW_AdressWrite() {
        return AW_AdressWrite;
    }

    public void setAW_AdressWrite(String AW_AdressWrite) {
        this.AW_AdressWrite = AW_AdressWrite;
    }

    public float getAW_Lng() {
        return AW_Lng;
    }

    public void setAW_Lng(float AW_Lng) {
        this.AW_Lng = AW_Lng;
    }

    public float getAW_Lat() {
        return AW_Lat;
    }

    public void setAW_Lat(float AW_Lat) {
        this.AW_Lat = AW_Lat;
    }

    public int getAW_Number() {
        return AW_Number;
    }

    public void setAW_Number(int AW_Number) {
        this.AW_Number = AW_Number;
    }

    public String getAW_Remark() {
        return AW_Remark;
    }

    public void setAW_Remark(String AW_Remark) {
        this.AW_Remark = AW_Remark;
    }
}
