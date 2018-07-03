package com.example.administrator.ydxcfwpt.Bean;

import com.example.administrator.ydxcfwpt.Utils.StringUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/1.
 */

public class Worker implements Serializable {

    private int W_ID;    //
    private String W_Name;    //洗车工姓名
    private String W_Image;    //洗车工照片
    private int W_Sex;    //洗车工性别,1代表男性，0代表女性
    private int W_Age;    //年龄
    private String W_Tel;    //洗车工手机号
    private String W_CreatTime;    //注册时间
    private String W_UpdateTime;    //修改时间
    private String W_Pwd;    //密码
    private int W_IdentityType;    //证件类型（1居民身份证、2护照、3军官证、4驾驶证、5港澳回乡证台胞证
    private String W_IdentityCard;    //身份证号
    private String W_IdentityCardImage1;    //身份证正面拍照
    private String W_IdentityCardImage2;    //身份证背面拍照
    private String W_IdentityCardImage3;    //手持身份证拍照
    private String W_WorkID;    //工作证号
    private int W_Deposit;    //押金
    private int W_IsBuy;    //设备是购买还是租借，1表示购买，0表示租借
    private int W_HaveMachine;    //设备是在洗车工手，0不在，1在
    private int W_MachineState;    //设备状态，1正常，2故障
    private double W_Money;    //余额
    private int W_IsLock;    //是否锁定该账户，默认0，1代表锁定，0代表正常
    private int W_IsPush;    //是否接受推送，1接受，0不接受
    private int W_WSID;    //接活状态，WorkerState表的WS_ID列的外键
    private int W_Isonline;    //当前状态,1在线，0下线，默认0
    private String W_OnlineTime;    //上线时间
    private String W_LSTime;    //临时刷新时间，防止意外关闭程序
    private String W_OnlineOutTime;    //下线时间
    private int W_OnlineTodayTime;    //在线时长 分钟,默认0
    private int W_OnlineAllTime;    //总在线时长，分钟为单位，默认0
    private double W_TodayMoney;    //当天营业额
    private double W_AllMoney;    //洗车工总营业额
    private double W_GetMoney;    //洗车工提现营业额
    //    private	double W_GetPrice;	//洗车工提现金额
    private String W_IMEI;//洗车工校验码
    private String W_IdentityID;    //保存验证码
    private int W_LSID;    //临时列，当注册时候，写入一个临时ID
    private double W_Lng;
    private double W_Lat;
    private int W_IsFormal;    //是否认证0：未认证，1：认证过了；默认0
    private int W_IsStart;    //代理商是否允许开始接单0：未认证，1：认证过了；默认0
    private int W_IsFormalMe;   //公司是否认证0：未认证，1：认证过了；默认0
    private String W_Province;   //省名称
    private String W_City;   //市名称
    private String W_BankName;    //开户行名称
    private String W_BankCard;    //开户行卡号
    private String W_BankAdress;    //开户行地址50个字符
    private String W_County;    //区县名称
    private int W_AgentID;    //所属代理商ID
    private int W_Type;    //直营加盟；1代表公司直营，2代表加盟
    private String W_Remark;//备注
    private double F_Lat;
    private double F_Lng;
    private int NumLng;
    private String W_Home;//	 	家地址
    private String W_HomeWrite;//是	 	家地址手写
    private float W_HomeLng;            //是	 	家地址经度
    private float W_HomeLat;    //	 	家地址纬度
    private String W_Office;            //是	 	公司地址
    private String W_OfficeWrite;         //	是	 	公司地址手写
    private float W_OfficeLng;            //是	 	公司经度
    private float W_OfficeLat;        //是	 	公司纬度
    private int W_OID;
    private int W_OrderCount;
    private String W_Star;
    private String W_AllPrice;
    private String W_GetAllMoney;
    private int W_OrderEndCount;
    public Worker() {
    }

    @Override
    public String toString() {
        return "Worker{" +
                "W_ID=" + W_ID +
                ", W_Name='" + W_Name + '\'' +
                ", W_Image='" + W_Image + '\'' +
                ", W_Sex=" + W_Sex +
                ", W_Age=" + W_Age +
                ", W_Tel='" + W_Tel + '\'' +
                ", W_CreatTime='" + W_CreatTime + '\'' +
                ", W_UpdateTime='" + W_UpdateTime + '\'' +
                ", W_Pwd='" + W_Pwd + '\'' +
                ", W_IdentityType=" + W_IdentityType +
                ", W_IdentityCard='" + W_IdentityCard + '\'' +
                ", W_IdentityCardImage1='" + W_IdentityCardImage1 + '\'' +
                ", W_IdentityCardImage2='" + W_IdentityCardImage2 + '\'' +
                ", W_IdentityCardImage3='" + W_IdentityCardImage3 + '\'' +
                ", W_WorkID='" + W_WorkID + '\'' +
                ", W_Deposit=" + W_Deposit +
                ", W_IsBuy=" + W_IsBuy +
                ", W_HaveMachine=" + W_HaveMachine +
                ", W_MachineState=" + W_MachineState +
                ", W_Money=" + W_Money +
                ", W_IsLock=" + W_IsLock +
                ", W_IsPush=" + W_IsPush +
                ", W_WSID=" + W_WSID +
                ", W_Isonline=" + W_Isonline +
                ", W_OnlineTime='" + W_OnlineTime + '\'' +
                ", W_LSTime='" + W_LSTime + '\'' +
                ", W_OnlineOutTime='" + W_OnlineOutTime + '\'' +
                ", W_OnlineTodayTime=" + W_OnlineTodayTime +
                ", W_OnlineAllTime=" + W_OnlineAllTime +
                ", W_TodayMoney=" + W_TodayMoney +
                ", W_AllMoney=" + W_AllMoney +
                ", W_GetMoney=" + W_GetMoney +
                ", W_IMEI='" + W_IMEI + '\'' +
                ", W_IdentityID='" + W_IdentityID + '\'' +
                ", W_LSID=" + W_LSID +
                ", W_Lng=" + W_Lng +
                ", W_Lat=" + W_Lat +
                ", W_IsFormal=" + W_IsFormal +
                ", W_IsStart=" + W_IsStart +
                ", W_IsFormalMe=" + W_IsFormalMe +
                ", W_Province='" + W_Province + '\'' +
                ", W_City='" + W_City + '\'' +
                ", W_BankName='" + W_BankName + '\'' +
                ", W_BankCard='" + W_BankCard + '\'' +
                ", W_BankAdress='" + W_BankAdress + '\'' +
                ", W_County='" + W_County + '\'' +
                ", W_AgentID=" + W_AgentID +
                ", W_Type=" + W_Type +
                ", W_Remark='" + W_Remark + '\'' +
                ", F_Lat=" + F_Lat +
                ", F_Lng=" + F_Lng +
                ", NumLng=" + NumLng +
                ", W_Home='" + W_Home + '\'' +
                ", W_HomeWrite='" + W_HomeWrite + '\'' +
                ", W_HomeLng=" + W_HomeLng +
                ", W_HomeLat=" + W_HomeLat +
                ", W_Office='" + W_Office + '\'' +
                ", W_OfficeWrite='" + W_OfficeWrite + '\'' +
                ", W_OfficeLng=" + W_OfficeLng +
                ", W_OfficeLat=" + W_OfficeLat +
                ", W_OID=" + W_OID +
                ", W_OrderCount=" + W_OrderCount +
                ", W_Star='" + W_Star + '\'' +
                ", W_AllPrice='" + W_AllPrice + '\'' +
                ", W_GetAllMoney='" + W_GetAllMoney + '\'' +
                ", W_OrderEndCount=" + W_OrderEndCount +
                '}';
    }

    public int getW_ID() {
        return W_ID;
    }

    public void setW_ID(int w_ID) {
        W_ID = w_ID;
    }

    public String getW_Name() {
        return W_Name;
    }

    public void setW_Name(String w_Name) {
        W_Name = w_Name;
    }

    public String getW_Image() {
        return W_Image;
    }

    public void setW_Image(String w_Image) {
        W_Image = w_Image;
    }

    public int getW_Sex() {
        return W_Sex;
    }

    public void setW_Sex(int w_Sex) {
        W_Sex = w_Sex;
    }

    public int getW_Age() {
        return W_Age;
    }

    public void setW_Age(int w_Age) {
        W_Age = w_Age;
    }

    public String getW_Tel() {
        return W_Tel;
    }

    public void setW_Tel(String w_Tel) {
        W_Tel = w_Tel;
    }

    public String getW_CreatTime() {
        return W_CreatTime;
    }

    public void setW_CreatTime(String w_CreatTime) {
        W_CreatTime = w_CreatTime;
    }

    public String getW_UpdateTime() {
        return W_UpdateTime;
    }

    public void setW_UpdateTime(String w_UpdateTime) {
        W_UpdateTime = w_UpdateTime;
    }

    public String getW_Pwd() {
        return W_Pwd;
    }

    public void setW_Pwd(String w_Pwd) {
        W_Pwd = w_Pwd;
    }

    public int getW_IdentityType() {
        return W_IdentityType;
    }

    public void setW_IdentityType(int w_IdentityType) {
        W_IdentityType = w_IdentityType;
    }

    public String getW_IdentityCard() {
        return W_IdentityCard;
    }

    public void setW_IdentityCard(String w_IdentityCard) {
        W_IdentityCard = w_IdentityCard;
    }

    public String getW_IdentityCardImage1() {
        return W_IdentityCardImage1;
    }

    public void setW_IdentityCardImage1(String w_IdentityCardImage1) {
        W_IdentityCardImage1 = w_IdentityCardImage1;
    }

    public String getW_IdentityCardImage2() {
        return W_IdentityCardImage2;
    }

    public void setW_IdentityCardImage2(String w_IdentityCardImage2) {
        W_IdentityCardImage2 = w_IdentityCardImage2;
    }

    public String getW_IdentityCardImage3() {
        return W_IdentityCardImage3;
    }

    public void setW_IdentityCardImage3(String w_IdentityCardImage3) {
        W_IdentityCardImage3 = w_IdentityCardImage3;
    }

    public String getW_WorkID() {
        return W_WorkID;
    }

    public void setW_WorkID(String w_WorkID) {
        W_WorkID = w_WorkID;
    }

    public int getW_Deposit() {
        return W_Deposit;
    }

    public void setW_Deposit(int w_Deposit) {
        W_Deposit = w_Deposit;
    }

    public int getW_IsBuy() {
        return W_IsBuy;
    }

    public void setW_IsBuy(int w_IsBuy) {
        W_IsBuy = w_IsBuy;
    }

    public int getW_HaveMachine() {
        return W_HaveMachine;
    }

    public void setW_HaveMachine(int w_HaveMachine) {
        W_HaveMachine = w_HaveMachine;
    }

    public int getW_MachineState() {
        return W_MachineState;
    }

    public void setW_MachineState(int w_MachineState) {
        W_MachineState = w_MachineState;
    }

    public double getW_Money() {
        return W_Money;
    }

    public void setW_Money(double w_Money) {
        W_Money = w_Money;
    }

    public int getW_IsLock() {
        return W_IsLock;
    }

    public void setW_IsLock(int w_IsLock) {
        W_IsLock = w_IsLock;
    }

    public int getW_IsPush() {
        return W_IsPush;
    }

    public void setW_IsPush(int w_IsPush) {
        W_IsPush = w_IsPush;
    }

    public int getW_WSID() {
        return W_WSID;
    }

    public void setW_WSID(int w_WSID) {
        W_WSID = w_WSID;
    }

    public int getW_Isonline() {
        return W_Isonline;
    }

    public void setW_Isonline(int w_Isonline) {
        W_Isonline = w_Isonline;
    }

    public String getW_OnlineTime() {
        return W_OnlineTime;
    }

    public void setW_OnlineTime(String w_OnlineTime) {
        W_OnlineTime = w_OnlineTime;
    }

    public String getW_LSTime() {
        return W_LSTime;
    }

    public void setW_LSTime(String w_LSTime) {
        W_LSTime = w_LSTime;
    }

    public String getW_OnlineOutTime() {
        return W_OnlineOutTime;
    }

    public void setW_OnlineOutTime(String w_OnlineOutTime) {
        W_OnlineOutTime = w_OnlineOutTime;
    }

    public int getW_OnlineTodayTime() {
        return W_OnlineTodayTime;
    }

    public void setW_OnlineTodayTime(int w_OnlineTodayTime) {
        W_OnlineTodayTime = w_OnlineTodayTime;
    }

    public int getW_OnlineAllTime() {
        return W_OnlineAllTime;
    }

    public void setW_OnlineAllTime(int w_OnlineAllTime) {
        W_OnlineAllTime = w_OnlineAllTime;
    }

    public double getW_TodayMoney() {
        return W_TodayMoney;
    }

    public void setW_TodayMoney(double w_TodayMoney) {
        W_TodayMoney = w_TodayMoney;
    }

    public double getW_AllMoney() {
        return W_AllMoney;
    }

    public void setW_AllMoney(double w_AllMoney) {
        W_AllMoney = w_AllMoney;
    }

    public double getW_GetMoney() {
        return W_GetMoney;
    }

    public void setW_GetMoney(double w_GetMoney) {
        W_GetMoney = w_GetMoney;
    }

    public String getW_IMEI() {
        return W_IMEI;
    }

    public void setW_IMEI(String w_IMEI) {
        W_IMEI = w_IMEI;
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

    public double getW_Lng() {
        return W_Lng;
    }

    public void setW_Lng(double w_Lng) {
        W_Lng = w_Lng;
    }

    public double getW_Lat() {
        return W_Lat;
    }

    public void setW_Lat(double w_Lat) {
        W_Lat = w_Lat;
    }

    public int getW_IsFormal() {
        return W_IsFormal;
    }

    public void setW_IsFormal(int w_IsFormal) {
        W_IsFormal = w_IsFormal;
    }

    public int getW_IsStart() {
        return W_IsStart;
    }

    public void setW_IsStart(int w_IsStart) {
        W_IsStart = w_IsStart;
    }

    public int getW_IsFormalMe() {
        return W_IsFormalMe;
    }

    public void setW_IsFormalMe(int w_IsFormalMe) {
        W_IsFormalMe = w_IsFormalMe;
    }

    public String getW_Province() {
        return W_Province;
    }

    public void setW_Province(String w_Province) {
        W_Province = w_Province;
    }

    public String getW_City() {
        return W_City;
    }

    public void setW_City(String w_City) {
        W_City = w_City;
    }

    public String getW_BankName() {
        return W_BankName;
    }

    public void setW_BankName(String w_BankName) {
        W_BankName = w_BankName;
    }

    public String getW_BankCard() {
        return W_BankCard;
    }

    public void setW_BankCard(String w_BankCard) {
        W_BankCard = w_BankCard;
    }

    public String getW_BankAdress() {
        return W_BankAdress;
    }

    public void setW_BankAdress(String w_BankAdress) {
        W_BankAdress = w_BankAdress;
    }

    public String getW_County() {
        return W_County;
    }

    public void setW_County(String w_County) {
        W_County = w_County;
    }

    public int getW_AgentID() {
        return W_AgentID;
    }

    public void setW_AgentID(int w_AgentID) {
        W_AgentID = w_AgentID;
    }

    public int getW_Type() {
        return W_Type;
    }

    public void setW_Type(int w_Type) {
        W_Type = w_Type;
    }

    public String getW_Remark() {
        return W_Remark;
    }

    public void setW_Remark(String w_Remark) {
        W_Remark = w_Remark;
    }

    public double getF_Lat() {
        return F_Lat;
    }

    public void setF_Lat(double f_Lat) {
        F_Lat = f_Lat;
    }

    public double getF_Lng() {
        return F_Lng;
    }

    public void setF_Lng(double f_Lng) {
        F_Lng = f_Lng;
    }

    public int getNumLng() {
        return NumLng;
    }

    public void setNumLng(int numLng) {
        NumLng = numLng;
    }

    public String getW_Home() {
        return W_Home;
    }

    public void setW_Home(String w_Home) {
        W_Home = w_Home;
    }

    public String getW_HomeWrite() {
        return W_HomeWrite;
    }

    public void setW_HomeWrite(String w_HomeWrite) {
        W_HomeWrite = w_HomeWrite;
    }

    public float getW_HomeLng() {
        return W_HomeLng;
    }

    public void setW_HomeLng(float w_HomeLng) {
        W_HomeLng = w_HomeLng;
    }

    public float getW_HomeLat() {
        return W_HomeLat;
    }

    public void setW_HomeLat(float w_HomeLat) {
        W_HomeLat = w_HomeLat;
    }

    public String getW_Office() {
        return W_Office;
    }

    public void setW_Office(String w_Office) {
        W_Office = w_Office;
    }

    public String getW_OfficeWrite() {
        return W_OfficeWrite;
    }

    public void setW_OfficeWrite(String w_OfficeWrite) {
        W_OfficeWrite = w_OfficeWrite;
    }

    public float getW_OfficeLng() {
        return W_OfficeLng;
    }

    public void setW_OfficeLng(float w_OfficeLng) {
        W_OfficeLng = w_OfficeLng;
    }

    public float getW_OfficeLat() {
        return W_OfficeLat;
    }

    public void setW_OfficeLat(float w_OfficeLat) {
        W_OfficeLat = w_OfficeLat;
    }

    public int getW_OID() {
        return W_OID;
    }

    public void setW_OID(int w_OID) {
        W_OID = w_OID;
    }

    public int getW_OrderCount() {
        return W_OrderCount;
    }

    public void setW_OrderCount(int w_OrderCount) {
        W_OrderCount = w_OrderCount;
    }

    public String getW_Star() {
        return W_Star;
    }

    public void setW_Star(String w_Star) {
        W_Star = w_Star;
    }

    public String getW_AllPrice() {
        return W_AllPrice;
    }

    public void setW_AllPrice(String w_AllPrice) {
        W_AllPrice = w_AllPrice;
    }

    public String getW_GetAllMoney() {
        return W_GetAllMoney;
    }

    public void setW_GetAllMoney(String w_GetAllMoney) {
        W_GetAllMoney = w_GetAllMoney;
    }

    public int getW_OrderEndCount() {
        return W_OrderEndCount;
    }

    public void setW_OrderEndCount(int w_OrderEndCount) {
        W_OrderEndCount = w_OrderEndCount;
    }
}
