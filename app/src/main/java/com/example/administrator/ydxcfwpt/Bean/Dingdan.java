package com.example.administrator.ydxcfwpt.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/15.
 */
public class Dingdan implements Serializable {
    private int O_IDS;
    private String O_ID;            //订单号
    private String O_UTel;          //联系人手机号最多填2个手机号
    private String O_CarTel;        //车辆所属人电话
    private String O_CarName;       //车辆所属人姓名
    private String O_WPart;         //洗车类型ID，如果清洗类型多种，用|隔开
    private int O_CTID;             //车型ID，轿车5座、SUV和MPV
    private String O_UName;         //联系人姓名
    private String O_CarImage;      //车辆照片
    private String O_CarColor;      //车辆颜色
    private String O_PlateNumber;   //车牌号
    private int O_TypeID;           //订单状态ID,OrderTypes表OT_ID列的外键
    private int O_Price;            //订单金额
    private String O_Adress;        //爱车位置
    private String O_WriteAdress;   //手动输出的地址
    private float O_Lng;            //坐标经度
    private float O_Lat;            //坐标纬度
    private String O_Time;          //下单时间
    private String O_TimeEnd;       //订单完成时间
    private String O_StartTime;     //订单开始时间
    private int O_IsPhone;          //洗完车是否要给电话通知，1：打电话，0：不打电话
    private int O_EvaluateStar;     //评价等级几星
    private String O_EvaluateType;  //评价类型(服务周到、洗车专业、准时到达、风雨无阻、穿着整洁)用,隔开
    private String O_EvaluateImage;  //评价拍照
    private int O_EvaluateAnonymous; //是否匿名，0匿名，1可显示，默认匿名
    private int O_EvaluateReward;   //打赏金额
    private int O_IsEvaluate;       //是否已经评价，0未评价，1已评价，默认0
    private String O_Evaluate;      //评价100个字
    private int O_Money;            //实际支付金额
    private int O_TID;              //代金券ID
    private int O_Ticket;           //实际支付代金券金额
    private int O_TType;            //代金券洗车类型ID,默认0通用代金券
    private int O_TMoney;           //订单满多少使用
    private String O_TTime;         //代金券到期时间
    private int O_ISCancel;         //是否取消订单，默认为 0 ，1表示取消订单，0表示正常
    private String O_ISCancelValue; //取消订单的原因
    private int O_ISRefund;         //是否申请退款，默认 0，1表示申请退款，0表示正常
    private int O_RefundMoney;      //退款金额
    private int O_WorkerID;         //洗车工ID
    private String O_BeforePic;     //洗前照片
    private String O_AfterPic;      //洗后照片
    private String O_WorkerName;     //洗车工姓名
    private String O_WorkerImage;   //洗车工头像
    private String O_WorkerTel;     //洗车工电话
    private String O_Province;      //	洗车工省名称
    private String O_City;          //	洗车工市名称
    private String O_County;        //洗车工区县名称
    private String O_BrandName;     //汽车品牌名,该字段是CarBrand表 CB_BrandName列的外键
    private String O_BrandTypeName;  //车型名称
    private String O_WashTime;      //洗车时间
    private int O_WashType;         //立即服务0，预约服务1
    private String O_Remark;        //备注
    private String O_ScratchImage1;        //划痕照片1
    private String O_ScratchImage2;        //划痕照片2
    private String O_ScratchImage3;        //划痕照片3
    private String O_ScratchImage4;        //划痕照片4
    private String O_ScratchImage5;        //划痕照片5
    private String O_ScratchImage6;        //划痕照片6
    private int P_ID;
    private int P_TypeID;
    private String P_PayUTel;
    private String P_PayUserType;
    private String P_PayPrice;
    private String P_PayPriceAdd;
    private String  P_PayMoney;
    private String P_TakeUTel;
    private String P_TakeUserType;
    private String P_TakePrice;
    private String P_TakePriceAdd;
    private String P_TakeMoney;
    private String P_Time;
    private String P_Type;
    private String P_Ticket;
    private String P_TicketNum;
    private String P_GetMoneyState;
    private String P_GetMoneyValue;
    private String P_Remark;
    public Dingdan() {
    }

    @Override
    public String toString() {
        return "Dingdan{" +
                "O_IDS=" + O_IDS +
                ", O_ID='" + O_ID + '\'' +
                ", O_UTel='" + O_UTel + '\'' +
                ", O_CarTel='" + O_CarTel + '\'' +
                ", O_CarName='" + O_CarName + '\'' +
                ", O_WPart='" + O_WPart + '\'' +
                ", O_CTID=" + O_CTID +
                ", O_UName='" + O_UName + '\'' +
                ", O_CarImage='" + O_CarImage + '\'' +
                ", O_CarColor='" + O_CarColor + '\'' +
                ", O_PlateNumber='" + O_PlateNumber + '\'' +
                ", O_TypeID=" + O_TypeID +
                ", O_Price=" + O_Price +
                ", O_Adress='" + O_Adress + '\'' +
                ", O_WriteAdress='" + O_WriteAdress + '\'' +
                ", O_Lng=" + O_Lng +
                ", O_Lat=" + O_Lat +
                ", O_Time='" + O_Time + '\'' +
                ", O_TimeEnd='" + O_TimeEnd + '\'' +
                ", O_StartTime='" + O_StartTime + '\'' +
                ", O_IsPhone=" + O_IsPhone +
                ", O_EvaluateStar=" + O_EvaluateStar +
                ", O_EvaluateType='" + O_EvaluateType + '\'' +
                ", O_EvaluateImage='" + O_EvaluateImage + '\'' +
                ", O_EvaluateAnonymous=" + O_EvaluateAnonymous +
                ", O_EvaluateReward=" + O_EvaluateReward +
                ", O_IsEvaluate=" + O_IsEvaluate +
                ", O_Evaluate='" + O_Evaluate + '\'' +
                ", O_Money=" + O_Money +
                ", O_TID=" + O_TID +
                ", O_Ticket=" + O_Ticket +
                ", O_TType=" + O_TType +
                ", O_TMoney=" + O_TMoney +
                ", O_TTime='" + O_TTime + '\'' +
                ", O_ISCancel=" + O_ISCancel +
                ", O_ISCancelValue='" + O_ISCancelValue + '\'' +
                ", O_ISRefund=" + O_ISRefund +
                ", O_RefundMoney=" + O_RefundMoney +
                ", O_WorkerID=" + O_WorkerID +
                ", O_BeforePic='" + O_BeforePic + '\'' +
                ", O_AfterPic='" + O_AfterPic + '\'' +
                ", O_WorkerName='" + O_WorkerName + '\'' +
                ", O_WorkerImage='" + O_WorkerImage + '\'' +
                ", O_WorkerTel='" + O_WorkerTel + '\'' +
                ", O_Province='" + O_Province + '\'' +
                ", O_City='" + O_City + '\'' +
                ", O_County='" + O_County + '\'' +
                ", O_BrandName='" + O_BrandName + '\'' +
                ", O_BrandTypeName='" + O_BrandTypeName + '\'' +
                ", O_WashTime='" + O_WashTime + '\'' +
                ", O_WashType=" + O_WashType +
                ", O_Remark='" + O_Remark + '\'' +
                ", O_ScratchImage1='" + O_ScratchImage1 + '\'' +
                ", O_ScratchImage2='" + O_ScratchImage2 + '\'' +
                ", O_ScratchImage3='" + O_ScratchImage3 + '\'' +
                ", O_ScratchImage4='" + O_ScratchImage4 + '\'' +
                ", O_ScratchImage5='" + O_ScratchImage5 + '\'' +
                ", O_ScratchImage6='" + O_ScratchImage6 + '\'' +
                ", P_ID=" + P_ID +
                ", P_TypeID=" + P_TypeID +
                ", P_PayUTel='" + P_PayUTel + '\'' +
                ", P_PayUserType='" + P_PayUserType + '\'' +
                ", P_PayPrice='" + P_PayPrice + '\'' +
                ", P_PayPriceAdd='" + P_PayPriceAdd + '\'' +
                ", P_PayMoney='" + P_PayMoney + '\'' +
                ", P_TakeUTel='" + P_TakeUTel + '\'' +
                ", P_TakeUserType='" + P_TakeUserType + '\'' +
                ", P_TakePrice='" + P_TakePrice + '\'' +
                ", P_TakePriceAdd='" + P_TakePriceAdd + '\'' +
                ", P_TakeMoney='" + P_TakeMoney + '\'' +
                ", P_Time='" + P_Time + '\'' +
                ", P_Type='" + P_Type + '\'' +
                ", P_Ticket='" + P_Ticket + '\'' +
                ", P_TicketNum='" + P_TicketNum + '\'' +
                ", P_GetMoneyState='" + P_GetMoneyState + '\'' +
                ", P_GetMoneyValue='" + P_GetMoneyValue + '\'' +
                ", P_Remark='" + P_Remark + '\'' +
                '}';
    }

    public int getO_IDS() {
        return O_IDS;
    }

    public void setO_IDS(int o_IDS) {
        O_IDS = o_IDS;
    }

    public String getO_ID() {
        return O_ID;
    }

    public void setO_ID(String o_ID) {
        O_ID = o_ID;
    }

    public String getO_UTel() {
        return O_UTel;
    }

    public void setO_UTel(String o_UTel) {
        O_UTel = o_UTel;
    }

    public String getO_CarTel() {
        return O_CarTel;
    }

    public void setO_CarTel(String o_CarTel) {
        O_CarTel = o_CarTel;
    }

    public String getO_CarName() {
        return O_CarName;
    }

    public void setO_CarName(String o_CarName) {
        O_CarName = o_CarName;
    }

    public String getO_WPart() {
        return O_WPart;
    }

    public void setO_WPart(String o_WPart) {
        O_WPart = o_WPart;
    }

    public int getO_CTID() {
        return O_CTID;
    }

    public void setO_CTID(int o_CTID) {
        O_CTID = o_CTID;
    }

    public String getO_UName() {
        return O_UName;
    }

    public void setO_UName(String o_UName) {
        O_UName = o_UName;
    }

    public String getO_CarImage() {
        return O_CarImage;
    }

    public void setO_CarImage(String o_CarImage) {
        O_CarImage = o_CarImage;
    }

    public String getO_CarColor() {
        return O_CarColor;
    }

    public void setO_CarColor(String o_CarColor) {
        O_CarColor = o_CarColor;
    }

    public String getO_PlateNumber() {
        return O_PlateNumber;
    }

    public void setO_PlateNumber(String o_PlateNumber) {
        O_PlateNumber = o_PlateNumber;
    }

    public int getO_TypeID() {
        return O_TypeID;
    }

    public void setO_TypeID(int o_TypeID) {
        O_TypeID = o_TypeID;
    }

    public int getO_Price() {
        return O_Price;
    }

    public void setO_Price(int o_Price) {
        O_Price = o_Price;
    }

    public String getO_Adress() {
        return O_Adress;
    }

    public void setO_Adress(String o_Adress) {
        O_Adress = o_Adress;
    }

    public String getO_WriteAdress() {
        return O_WriteAdress;
    }

    public void setO_WriteAdress(String o_WriteAdress) {
        O_WriteAdress = o_WriteAdress;
    }

    public float getO_Lng() {
        return O_Lng;
    }

    public void setO_Lng(float o_Lng) {
        O_Lng = o_Lng;
    }

    public float getO_Lat() {
        return O_Lat;
    }

    public void setO_Lat(float o_Lat) {
        O_Lat = o_Lat;
    }

    public String getO_Time() {
        return O_Time;
    }

    public void setO_Time(String o_Time) {
        O_Time = o_Time;
    }

    public String getO_TimeEnd() {
        return O_TimeEnd;
    }

    public void setO_TimeEnd(String o_TimeEnd) {
        O_TimeEnd = o_TimeEnd;
    }

    public String getO_StartTime() {
        return O_StartTime;
    }

    public void setO_StartTime(String o_StartTime) {
        O_StartTime = o_StartTime;
    }

    public int getO_IsPhone() {
        return O_IsPhone;
    }

    public void setO_IsPhone(int o_IsPhone) {
        O_IsPhone = o_IsPhone;
    }

    public int getO_EvaluateStar() {
        return O_EvaluateStar;
    }

    public void setO_EvaluateStar(int o_EvaluateStar) {
        O_EvaluateStar = o_EvaluateStar;
    }

    public String getO_EvaluateType() {
        return O_EvaluateType;
    }

    public void setO_EvaluateType(String o_EvaluateType) {
        O_EvaluateType = o_EvaluateType;
    }

    public String getO_EvaluateImage() {
        return O_EvaluateImage;
    }

    public void setO_EvaluateImage(String o_EvaluateImage) {
        O_EvaluateImage = o_EvaluateImage;
    }

    public int getO_EvaluateAnonymous() {
        return O_EvaluateAnonymous;
    }

    public void setO_EvaluateAnonymous(int o_EvaluateAnonymous) {
        O_EvaluateAnonymous = o_EvaluateAnonymous;
    }

    public int getO_EvaluateReward() {
        return O_EvaluateReward;
    }

    public void setO_EvaluateReward(int o_EvaluateReward) {
        O_EvaluateReward = o_EvaluateReward;
    }

    public int getO_IsEvaluate() {
        return O_IsEvaluate;
    }

    public void setO_IsEvaluate(int o_IsEvaluate) {
        O_IsEvaluate = o_IsEvaluate;
    }

    public String getO_Evaluate() {
        return O_Evaluate;
    }

    public void setO_Evaluate(String o_Evaluate) {
        O_Evaluate = o_Evaluate;
    }

    public int getO_Money() {
        return O_Money;
    }

    public void setO_Money(int o_Money) {
        O_Money = o_Money;
    }

    public int getO_TID() {
        return O_TID;
    }

    public void setO_TID(int o_TID) {
        O_TID = o_TID;
    }

    public int getO_Ticket() {
        return O_Ticket;
    }

    public void setO_Ticket(int o_Ticket) {
        O_Ticket = o_Ticket;
    }

    public int getO_TType() {
        return O_TType;
    }

    public void setO_TType(int o_TType) {
        O_TType = o_TType;
    }

    public int getO_TMoney() {
        return O_TMoney;
    }

    public void setO_TMoney(int o_TMoney) {
        O_TMoney = o_TMoney;
    }

    public String getO_TTime() {
        return O_TTime;
    }

    public void setO_TTime(String o_TTime) {
        O_TTime = o_TTime;
    }

    public int getO_ISCancel() {
        return O_ISCancel;
    }

    public void setO_ISCancel(int o_ISCancel) {
        O_ISCancel = o_ISCancel;
    }

    public String getO_ISCancelValue() {
        return O_ISCancelValue;
    }

    public void setO_ISCancelValue(String o_ISCancelValue) {
        O_ISCancelValue = o_ISCancelValue;
    }

    public int getO_ISRefund() {
        return O_ISRefund;
    }

    public void setO_ISRefund(int o_ISRefund) {
        O_ISRefund = o_ISRefund;
    }

    public int getO_RefundMoney() {
        return O_RefundMoney;
    }

    public void setO_RefundMoney(int o_RefundMoney) {
        O_RefundMoney = o_RefundMoney;
    }

    public int getO_WorkerID() {
        return O_WorkerID;
    }

    public void setO_WorkerID(int o_WorkerID) {
        O_WorkerID = o_WorkerID;
    }

    public String getO_BeforePic() {
        return O_BeforePic;
    }

    public void setO_BeforePic(String o_BeforePic) {
        O_BeforePic = o_BeforePic;
    }

    public String getO_AfterPic() {
        return O_AfterPic;
    }

    public void setO_AfterPic(String o_AfterPic) {
        O_AfterPic = o_AfterPic;
    }

    public String getO_WorkerName() {
        return O_WorkerName;
    }

    public void setO_WorkerName(String o_WorkerName) {
        O_WorkerName = o_WorkerName;
    }

    public String getO_WorkerImage() {
        return O_WorkerImage;
    }

    public void setO_WorkerImage(String o_WorkerImage) {
        O_WorkerImage = o_WorkerImage;
    }

    public String getO_WorkerTel() {
        return O_WorkerTel;
    }

    public void setO_WorkerTel(String o_WorkerTel) {
        O_WorkerTel = o_WorkerTel;
    }

    public String getO_Province() {
        return O_Province;
    }

    public void setO_Province(String o_Province) {
        O_Province = o_Province;
    }

    public String getO_City() {
        return O_City;
    }

    public void setO_City(String o_City) {
        O_City = o_City;
    }

    public String getO_County() {
        return O_County;
    }

    public void setO_County(String o_County) {
        O_County = o_County;
    }

    public String getO_BrandName() {
        return O_BrandName;
    }

    public void setO_BrandName(String o_BrandName) {
        O_BrandName = o_BrandName;
    }

    public String getO_BrandTypeName() {
        return O_BrandTypeName;
    }

    public void setO_BrandTypeName(String o_BrandTypeName) {
        O_BrandTypeName = o_BrandTypeName;
    }

    public String getO_WashTime() {
        return O_WashTime;
    }

    public void setO_WashTime(String o_WashTime) {
        O_WashTime = o_WashTime;
    }

    public int getO_WashType() {
        return O_WashType;
    }

    public void setO_WashType(int o_WashType) {
        O_WashType = o_WashType;
    }

    public String getO_Remark() {
        return O_Remark;
    }

    public void setO_Remark(String o_Remark) {
        O_Remark = o_Remark;
    }

    public String getO_ScratchImage1() {
        return O_ScratchImage1;
    }

    public void setO_ScratchImage1(String o_ScratchImage1) {
        O_ScratchImage1 = o_ScratchImage1;
    }

    public String getO_ScratchImage2() {
        return O_ScratchImage2;
    }

    public void setO_ScratchImage2(String o_ScratchImage2) {
        O_ScratchImage2 = o_ScratchImage2;
    }

    public String getO_ScratchImage3() {
        return O_ScratchImage3;
    }

    public void setO_ScratchImage3(String o_ScratchImage3) {
        O_ScratchImage3 = o_ScratchImage3;
    }

    public String getO_ScratchImage4() {
        return O_ScratchImage4;
    }

    public void setO_ScratchImage4(String o_ScratchImage4) {
        O_ScratchImage4 = o_ScratchImage4;
    }

    public String getO_ScratchImage5() {
        return O_ScratchImage5;
    }

    public void setO_ScratchImage5(String o_ScratchImage5) {
        O_ScratchImage5 = o_ScratchImage5;
    }

    public String getO_ScratchImage6() {
        return O_ScratchImage6;
    }

    public void setO_ScratchImage6(String o_ScratchImage6) {
        O_ScratchImage6 = o_ScratchImage6;
    }

    public int getP_ID() {
        return P_ID;
    }

    public void setP_ID(int p_ID) {
        P_ID = p_ID;
    }

    public int getP_TypeID() {
        return P_TypeID;
    }

    public void setP_TypeID(int p_TypeID) {
        P_TypeID = p_TypeID;
    }

    public String getP_PayUTel() {
        return P_PayUTel;
    }

    public void setP_PayUTel(String p_PayUTel) {
        P_PayUTel = p_PayUTel;
    }

    public String getP_PayUserType() {
        return P_PayUserType;
    }

    public void setP_PayUserType(String p_PayUserType) {
        P_PayUserType = p_PayUserType;
    }

    public String getP_PayPrice() {
        return P_PayPrice;
    }

    public void setP_PayPrice(String p_PayPrice) {
        P_PayPrice = p_PayPrice;
    }

    public String getP_PayPriceAdd() {
        return P_PayPriceAdd;
    }

    public void setP_PayPriceAdd(String p_PayPriceAdd) {
        P_PayPriceAdd = p_PayPriceAdd;
    }

    public String getP_PayMoney() {
        return P_PayMoney;
    }

    public void setP_PayMoney(String p_PayMoney) {
        P_PayMoney = p_PayMoney;
    }

    public String getP_TakeUTel() {
        return P_TakeUTel;
    }

    public void setP_TakeUTel(String p_TakeUTel) {
        P_TakeUTel = p_TakeUTel;
    }

    public String getP_TakeUserType() {
        return P_TakeUserType;
    }

    public void setP_TakeUserType(String p_TakeUserType) {
        P_TakeUserType = p_TakeUserType;
    }

    public String getP_TakePrice() {
        return P_TakePrice;
    }

    public void setP_TakePrice(String p_TakePrice) {
        P_TakePrice = p_TakePrice;
    }

    public String getP_TakePriceAdd() {
        return P_TakePriceAdd;
    }

    public void setP_TakePriceAdd(String p_TakePriceAdd) {
        P_TakePriceAdd = p_TakePriceAdd;
    }

    public String getP_TakeMoney() {
        return P_TakeMoney;
    }

    public void setP_TakeMoney(String p_TakeMoney) {
        P_TakeMoney = p_TakeMoney;
    }

    public String getP_Time() {
        return P_Time;
    }

    public void setP_Time(String p_Time) {
        P_Time = p_Time;
    }

    public String getP_Type() {
        return P_Type;
    }

    public void setP_Type(String p_Type) {
        P_Type = p_Type;
    }

    public String getP_Ticket() {
        return P_Ticket;
    }

    public void setP_Ticket(String p_Ticket) {
        P_Ticket = p_Ticket;
    }

    public String getP_TicketNum() {
        return P_TicketNum;
    }

    public void setP_TicketNum(String p_TicketNum) {
        P_TicketNum = p_TicketNum;
    }

    public String getP_GetMoneyState() {
        return P_GetMoneyState;
    }

    public void setP_GetMoneyState(String p_GetMoneyState) {
        P_GetMoneyState = p_GetMoneyState;
    }

    public String getP_GetMoneyValue() {
        return P_GetMoneyValue;
    }

    public void setP_GetMoneyValue(String p_GetMoneyValue) {
        P_GetMoneyValue = p_GetMoneyValue;
    }

    public String getP_Remark() {
        return P_Remark;
    }

    public void setP_Remark(String p_Remark) {
        P_Remark = p_Remark;
    }
}
