package com.example.administrator.ydxcfwpt.Contast;


import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.TuiKuan;
import com.example.administrator.ydxcfwpt.Bean.WChangyongdizhi;
import com.example.administrator.ydxcfwpt.Bean.Worker;

/**
 * Created by Administrator on 2017/8/5.
 */

public class Contast {


    public static double Lat;
    public static double Lon;
    public static Worker worker = new Worker();
    public static int ZhuangTai = 1;
    public static String Domain = "https://api.mabangxiche.com/";//正式环境
//  public static String Domain="192.168.0.166:12124/";//测试环境
    public static String Province;
    public static String City;
    public static String District;
    public static WChangyongdizhi wChangyongdizhi=new WChangyongdizhi();
    public static Dingdan dingdan=new Dingdan();
    public static TuiKuan tuikuan=new TuiKuan();
    public static final String KEYS = "ald25e4fe86d4gv4bthj419t6yu4j6w56wty9";

}
