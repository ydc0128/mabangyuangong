package com.example.administrator.ydxcfwpt.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.administrator.ydxcfwpt.Adapter.DangqianDingdanAdapter;
import com.example.administrator.ydxcfwpt.Adapter.DialogListViewAdapter;
import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.Worker;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.Receiver.MyReceiver;
import com.example.administrator.ydxcfwpt.Service.MyService;
import com.example.administrator.ydxcfwpt.Utils.APPUtil;
import com.example.administrator.ydxcfwpt.Utils.AudioUtils;
import com.example.administrator.ydxcfwpt.Utils.ContextUtil;
import com.example.administrator.ydxcfwpt.Utils.Logger;
import com.example.administrator.ydxcfwpt.Utils.NetworkUtils;
import com.example.administrator.ydxcfwpt.Utils.NoFastClickUtils;
import com.example.administrator.ydxcfwpt.Utils.StateUtils;
import com.example.administrator.ydxcfwpt.Utils.TtsSettings;
import com.example.administrator.ydxcfwpt.Utils.YuanGongZhuangtai;
import com.example.administrator.ydxcfwpt.View.InstrumentView;
import com.example.administrator.ydxcfwpt.View.SyInstrumentView;
import com.iflytek.cloud.SpeechEvent;
import com.squareup.picasso.Picasso;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RunnableFuture;

import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.administrator.ydxcfwpt.Contast.Contast.dingdan;
import static com.example.administrator.ydxcfwpt.Contast.Contast.worker;
import static java.util.Calendar.SECOND;

//主页
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private String url = Contast.Domain + "api/WorkerNowOrder.ashx?";
    private String url_jiedan = Contast.Domain + "api/OrderState.ashx";
    private String url_zhanchu = Contast.Domain + "api/OrderWorkerDelete.ashx";
    private static final String TAG = "MainActivity";
    private String url_jine = Contast.Domain + "api/WorkerAdress.ashx?";
    private static String url_setState = "http://api.mabangxiche.com/api/WorkerStateNow.ashx";
    private boolean isFirstLocation = true;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView tv_weizhi;
    private double lat;
    private double lon;
    private String dangqianweizhi;
    private static String mDay;
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，
    // 原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明
    public BDLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;

    private InstrumentView iv_shijian;
    private SyInstrumentView siv_shouyi;
    private CircleImageView iv_touxiang;
    private long shijiancha;

    private Button btn_dangqianzhuangtai;
//    private Button btn_qiehuanzhuangtai;
    private CircleImageView iv_cehua_touxiang;
    private TextView tv_cehua_name;
    private TextView tv_cehua_renzheng;
    private TextView tv_cehua_ziliao;
    private TextView tv_cehua_qianbao;
    private TextView tv_cehua_dingdan;
    private TextView tv_cehua_shezhi;
    private TextView tv_cehua_kefu;
    private Button btn_shuaxin;
    private TextView wcsl;
    private ImageView lv_ch_huiyuan;
    private List<String> zhuangtaiList;
    private ImageView iv_main_msg;
    private ListView listView, list_yuyue;
    private List<Dingdan> dingdanList;
    private DangqianDingdanAdapter adapter;
    private int zhuangtai;
    private String phone = "02987365853";
    private MainActivity activity;
    private String time;
    private RelativeLayout ll_fuwushijian;
    private int DELYED= 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=5a77c31c");
        activity = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(MainActivity.this);
        Intent intent = getIntent();
        zhuangtai = intent.getIntExtra("zhuangtai", -1);
        initViews();
        initListView();
        //获取侧滑对象内容
        initCehuaViews();
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //配置定位参数
        initLocation();
        //开始定位
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=5a77c351");
        mLocationClient.start();
        refelash();
        jine();
//        setPro();
//        handler.postDelayed(runnable, DELYED); //每隔1s执行
    }

    /**
     * 自动刷新
     */
//    Handler handler = new Handler();
//    Runnable runnable = new Runnable() {
//
//        @Override
//        public void run() {
//            // handler自带方法实现定时器
//            try {
//                handler.postDelayed(this, DELYED);
//              refelash();
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                System.out.println("exception...");
//            }
//        }
//    };

    //    private void shijian(){
//        Long now = System.currentTimeMillis();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        time = sdf.format(new Date(now));
//        final Calendar c = Calendar.getInstance();
//        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
//        String yuyueshijian=Contast.dingdan.getO_WashTime();
//        String weihao = yuyueshijian.substring(0,2);
//        String shijian1=yuyueshijian.substring(yuyueshijian.length()-5,yuyueshijian.length());
//        if (weihao.equals("今天")){
//            riqi=mDay;
//        }else if (weihao.equals("明天")){
//          riqi=mDay+1;
//        }
//        String shijian=riqi+shijian1;
//        int shijiancha=Integer.parseInt(shijian)-Integer.parseInt(time);
//        if (shijian.equals(time)){
//            bt_fuwu.setClickable(false);
//            bt_fuwu.setBackgroundResource(R.drawable.btn_hui_bg);
//            bt_fuwu.setTextColor(Color.GRAY);
//            tv_fuwushijian.setText(shijiancha);
//        }else if (shijian.equals(time+1000*30*60)){
//            bt_fuwu.setClickable(true);
//            bt_fuwu.setBackgroundResource(R.drawable.btn_blue_bg);
//            bt_fuwu.setTextColor(Color.parseColor("#ff33b5e5"));
//        }
//    }
//    private  void yuyue(){
//        if (Contast.dingdan.getO_TypeID()==9){
//            bt_jiedan.setVisibility(View.GONE);
//            bt_fuwu.setVisibility(View.VISIBLE);
//        }else if (Contast.dingdan.getO_TypeID()==1){
//            bt_fuwu.setVisibility(View.GONE);
//            bt_jiedan.setVisibility(View.VISIBLE);
//        }
//        bt_fuwu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//             Intent intent=new Intent(MainActivity.this,WorkActivity.class);
//             startActivity(intent);
//            }
//        });
//        bt_jiedan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FormBody.Builder params10 = new FormBody.Builder();
//                params10.add("W_Tel", Contast.worker.getW_Tel());
//                params10.add("W_IMEI", Contast.worker.getW_IMEI());
//                params10.add("W_WSID", "11");
//                params10.add("keys", Contast.KEYS);
//                OkHttpClient client10 = new OkHttpClient();
//                Request request10 = new Request.Builder()
//                        .url(url)
//                        .post(params10.build())
//                        .build();
//
//                okhttp3.Call call10 = client10.newCall(request10);
//                call10.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(okhttp3.Call call, IOException e) {
//                        //响应失败
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(MainActivity.this, "网络连接异样，请稍后重试...", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                    @Override
//                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
//                        //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
//                        String string = response.body().string();
//                        if (response.code() != HttpURLConnection.HTTP_OK) {
//                            Toast.makeText(MainActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.i(TAG, "onResponse: json=" + string);
//                            if (!TextUtils.isEmpty(string)) {
//                                if (string.contains("ErrorStr")) {
//                                    final Error error = JSON.parseObject(string, Error.class);
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(MainActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                } else {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if (Contast.dingdan.getO_TypeID()==9){
//                                                Toast.makeText(MainActivity.this, "接单成功", Toast.LENGTH_SHORT).show();
//                                            }else {
//                                                Toast.makeText(MainActivity.this, "接单失败", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//                                }
//                            } else {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(MainActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        }
//                    }
//                });
//            }
//        });
//    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.lv_main_listview);
        dingdanList = new ArrayList<>();
        adapter = new DangqianDingdanAdapter(this, dingdanList);
        for (Dingdan item : dingdanList) {
            int type = item.getO_TypeID();
            if (type == 2 || type == 3 || type == 8 || type == 7) {
                if (dingdan.getO_WashType() == 1) {
                    listView.setSelection(1);
                }
                dingdanList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dingdan dingdan = (Dingdan) adapter.getItem(position);
                if (dingdan.getO_TypeID() == 1) {
                    Toast.makeText(activity, "您还没有接单，请点击接单按钮", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, WorkActivity.class);
                    intent.putExtra("dingdan", dingdan);
                    Log.e("dingdan", dingdan.toString());
                    startActivity(intent);
                }
            }
        });
    }
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    Dingdan dingdan = (Dingdan) adapter.getItem(position);
//                    SharedPreferences sp = getSharedPreferences("Dingdan", MODE_PRIVATE);
////                    String dingdanhao = sp.getString("dingdanhao", "");
////                    if (dingdan.getO_ID().equals(dingdanhao)) {
////                        Intent intent = new Intent(MainActivity.this, WorkActivity.class);
////                        intent.putExtra("dingdan", dingdan);
////                        startActivity(intent);
//                        Log.i("123", "onItemClick: position=" + position);
//                        dingdan = (Dingdan) adapter.getItem(position - 1);
//                        Intent intent = new Intent(MainActivity.this, DingdanXiangqingActivity.class);
//                        intent.putExtra("dingdan", dingdan);
//                        startActivity(intent);
////                    } else {
////                        SharedPreferences.Editor edit = sp.edit();
////                        edit.putString("dingdanhao", dingdan.getO_ID());
////                        edit.commit();
////                        kaishifuwu(dingdan);
////                    }
////                } else {
////                    Toast.makeText(MainActivity.this, "请按照订单顺序完成服务", Toast.LENGTH_SHORT).show();
//               }
//            }
//        });
//        listView.setVisibility(View.VISIBLE);
//    }
//    private void kaishifuwu(final Dingdan dingdan) {
//        FormBody.Builder params = new FormBody.Builder();
//        params.add("O_ID", dingdan.getO_ID());
//        params.add("W_Tel", Contast.worker.getW_Tel());
//        params.add("W_IMEI", Contast.worker.getW_IMEI());
//        params.add("W_WSID", "5");
//        params.add("data", "");
//        params.add("keys", Contast.KEYS);
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(url_state)
//                .post(params.build())
//                .build();
//        okhttp3.Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, "网络连接异样，请稍后再试", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String string = response.body().string();
//                Log.e("qq", "qq" + string);
//                if (response.code() != HttpURLConnection.HTTP_OK) {
//                    Toast.makeText(MainActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (!TextUtils.isEmpty(string)) {
//                        if (string.contains("ErrorStr")) {
//                            if (dingdan.getO_ISCancel() == 0) {
//                                final Error error = JSON.parseObject(string, Error.class);
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        setPro();
//                                        refelash();
//                                        Toast.makeText(MainActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            } else {
//                                Toast.makeText(MainActivity.this, "该订单已取消", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            final Dingdan dingdan1 = JSON.parseObject(string, Dingdan.class);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Intent intent = new Intent(MainActivity.this, WorkActivity.class);
//                                    intent.putExtra("dingdan", dingdan1);
//                                    startActivity(intent);
//                                }
//                            });
//                        }
//                    } else {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(MainActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }
//            }
//        });
//    }
    @SuppressLint("WrongViewCast")
    private void initCehuaViews() {
//       ll_fuwushijian=(RelativeLayout)findViewById(R.id.ll_fuwushijian);
//       tv_fuwushijian=(TextView)findViewById(R.id.tv_fuwushijian);
        drawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        iv_cehua_touxiang = (CircleImageView) findViewById(R.id.iv_cehua_touxiang);
        tv_cehua_name = (TextView) findViewById(R.id.tv_cehua_name);
        tv_cehua_renzheng = (TextView) findViewById(R.id.tv_cehua_renzheng);
        tv_cehua_ziliao = (TextView) findViewById(R.id.tv_cehua_ziliao);
        tv_cehua_qianbao = (TextView) findViewById(R.id.tv_cehua_qianbao);
        tv_cehua_dingdan = (TextView) findViewById(R.id.tv_cehua_dingdan);
        tv_cehua_shezhi = (TextView) findViewById(R.id.tv_cehua_shezhi);
        tv_cehua_kefu = (TextView) findViewById(R.id.tv_cehua_kefu);
        wcsl=(TextView)findViewById(R.id.tv_wanchengshuliang);
//      list_yuyue=(ListView)findViewById(R.id.lv_main_yuyuelistview);
        Picasso.with(this).load(R.drawable.morentouxiang).into(iv_cehua_touxiang);
        iv_cehua_touxiang.setOnClickListener(this);
        tv_cehua_name.setOnClickListener(this);
        tv_cehua_renzheng.setOnClickListener(this);
        tv_cehua_ziliao.setOnClickListener(this);
        tv_cehua_qianbao.setOnClickListener(this);
        tv_cehua_dingdan.setOnClickListener(this);
        tv_cehua_shezhi.setOnClickListener(this);
        tv_cehua_kefu.setOnClickListener(this);
//      wcsl.setText("今日完成："+Contast.worker.getW_OrderEndCount()+"单");
        tv_cehua_kefu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(activity).setTitle("您确认拨打电话吗？")
//                      .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("返回", null)
                        .show();
            }
        });
//        showDrawerLayout();
//                Intent intent=new Intent(MainActivity.this,G.class);
//                startActivity(intent);
//            }
//       });
    }
    private void showDrawerLayout() {

        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
        } else {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    private void setDraweLayout() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                if (checkLogin()) {
                    if (Contast.worker.getW_IsFormal() == 0) {
                        tv_cehua_renzheng.setText("未认证");
                    } else if (Contast.worker.getW_IsFormal() == 1) {
                        if (Contast.worker.getW_Type() == 1) {
                            tv_cehua_renzheng.setText("公司直营(已认证)");
                            lv_ch_huiyuan.setVisibility(View.VISIBLE);
                        } else if (Contast.worker.getW_Type() == 2) {
                            tv_cehua_renzheng.setText("个人加盟(已认证)");
                            lv_ch_huiyuan.setVisibility(View.VISIBLE);
                        }
                    }
                    tv_cehua_name.setVisibility(View.VISIBLE);
                    tv_cehua_name.setText(Contast.worker.getW_Name());
                    if (TextUtils.isEmpty(Contast.worker.getW_Image())) {
                        Picasso.with(MainActivity.this).load(R.drawable.morentouxiang).into(iv_cehua_touxiang);
                    } else {
                        Uri image = Uri.parse(Contast.Domain + Contast.worker.getW_Image());
                        Picasso.with(MainActivity.this).load(image)
                                .placeholder(R.drawable.touxiang)
                                .error(R.drawable.touxiang)
                                .into(iv_cehua_touxiang);
                    }
                } else {
                    tv_cehua_name.setVisibility(View.GONE);
                    tv_cehua_renzheng.setText("登录/注册");
                }
            }
        };
        drawerLayout.setDrawerListener(toggle);
    }
    /**
     * 配置定位参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private boolean checkLogin() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        return isLogin;
    }

    //    private boolean checkYanZheng() {
//        if (checkLogin()) {
//            if (Contast.worker.getW_IsFormal() == 0) {
//                tv_cehua_renzheng.setText("未认证");
//                return false;
//            } else if (Contast.worker.getW_IsFormal() == 1) {
//                if (Contast.worker.getW_Type()==1) {
//                    tv_cehua_renzheng.setText("公司直营(已认证)");
//                }else if (Contast.worker.getW_Type()==2){
//                    tv_cehua_renzheng.setText("个人加盟(已认证)");
//                }
//                return true;
//            }
//        }
//            return false;
//        }
    private void startLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_main_touxiang:
                drawerLayout.openDrawer(Gravity.LEFT);//侧滑打开
                break;
//            case R.id.iv_main_msg:
//                Intent intent4 = new Intent(MainActivity.this, MassageActivity.class);
//                startActivity(intent4);
//                break;
            case R.id.btn_main_shuaxin:
                if (checkLogin()) {
                    refelash();
                    setPro();
                } else {
                    //如果没有登录，直接跳转登录界面
                    startLogin();
                }
                break;
            case R.id.btn_main_dangqianzhuangtai:
                if (checkLogin()) {
                    View bottomView = View.inflate(MainActivity.this, R.layout.item_listview_dialog, null);//填充ListView布局
                    ListView listView2 = (ListView) bottomView.findViewById(R.id.lv_item_listview_dialog);//初始化ListView控件
                    listView2.setAdapter(new DialogListViewAdapter(MainActivity.this, zhuangtaiList));//ListView设置适配器
                    final AlertDialog dialog2 = new AlertDialog.Builder(MainActivity.this)
                            .setView(bottomView)//在这里把写好的这个listview的布局加载dialog中
                            .create();
                    dialog2.show();
                    listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {//响应listview中的item的点击事件
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                long arg3) {
                            TextView tv = (TextView) arg1.findViewById(R.id.tv_item_listview_dialog_text);//取得每条item中的textview控件
                            String zhuangtai = tv.getText().toString();
                            //TODO 根据不同的状态向服务器发送不同请求
//                            if (zhuangtai.equals("临时小休")) {
//                                btn_dangqianzhuangtai.setText("小休中");
//                                YuanGongZhuangtai.setState(MainActivity.this, 3);
//                                Intent intent = new Intent(MainActivity.this, MyService.class);
//                                stopService(intent);
//                            } else
                                if (zhuangtai.equals("下班回家")) {
                                btn_dangqianzhuangtai.setText("下线中");
                                YuanGongZhuangtai.setState(MainActivity.this, 1);
                                    SharedPreferences sp =ContextUtil.getInstance().getSharedPreferences("Login", MODE_PRIVATE);
                                    String tel= sp.getString("W_Tel","");
                                    String imei=sp.getString("W_IMEI","");
                                    FormBody.Builder params = new FormBody.Builder();
                                    params.add("W_Tel", tel);
                                    params.add("W_IMEI", imei);
                                    params.add("W_WSID", "" + 1);
                                    params.add("keys", Contast.KEYS);
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url(url_setState)
                                            .post(params.build())
                                            .build();

                                    okhttp3.Call call = client.newCall(request);
                                    call.enqueue(new Callback() {
                                        @Override
                                        public void onFailure(okhttp3.Call call, IOException e) {
                                            //响应失败
                                            activity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        @Override
                                        public void onResponse(okhttp3.Call call, Response response) throws IOException {
                                            //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                                            String string = response.body().string();
                                            Log.i(TAG, "onResponse: json=" + string);
                                            if (!TextUtils.isEmpty(string)) {
                                                if (string.contains("ErrorStr")) {
                                                    final Error error = JSON.parseObject(string, Error.class);
                                                    activity.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
//                                        Toast.makeText(activity, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else {
                                                    Worker worker = JSON.parseObject(string, Worker.class);
                                                    Contast.worker = worker;
                                                    Log.i(TAG, "onResponse: " + worker.toString());
                                                }
                                            } else {
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                        }
                                    });
                                Intent intent = new Intent(MainActivity.this, MyService.class);
                                stopService(intent);
//                            } else if (zhuangtai.equals("设备报修")) {
//                                btn_dangqianzhuangtai.setText("维修中");
//                                YuanGongZhuangtai.setState(MainActivity.this, 9);
//                                Intent intent = new Intent(MainActivity.this, MyService.class);
//                                stopService(intent);
                            } else if (zhuangtai.equals("我要上线")) {
                                btn_dangqianzhuangtai.setText("上线中");
                                YuanGongZhuangtai.setState(MainActivity.this, 2);
                                    SharedPreferences sp =ContextUtil.getInstance().getSharedPreferences("Login", MODE_PRIVATE);
                                    String tel= sp.getString("W_Tel","");
                                    String imei=sp.getString("W_IMEI","");
                                    FormBody.Builder params = new FormBody.Builder();
                                    params.add("W_Tel", tel);
                                    params.add("W_IMEI", imei);
                                    params.add("W_WSID", "" + 2);
                                    params.add("keys", Contast.KEYS);
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url(url_setState)
                                            .post(params.build())
                                            .build();

                                    okhttp3.Call call = client.newCall(request);
                                    call.enqueue(new Callback() {
                                        @Override
                                        public void onFailure(okhttp3.Call call, IOException e) {
                                            //响应失败
                                            activity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        @Override
                                        public void onResponse(okhttp3.Call call, Response response) throws IOException {
                                            //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                                            String string = response.body().string();
                                            Log.i(TAG, "onResponse: json=" + string);
                                            if (!TextUtils.isEmpty(string)) {
                                                if (string.contains("ErrorStr")) {
                                                    final Error error = JSON.parseObject(string, Error.class);
                                                    activity.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
//                                        Toast.makeText(activity, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else {
                                                    Worker worker = JSON.parseObject(string, Worker.class);
                                                    Contast.worker = worker;
                                                    Log.i(TAG, "onResponse: " + worker.toString());
                                                }
                                            } else {
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                        }
                                    });
                                if (!APPUtil.isServiceWork(MainActivity.this, "com.example.administrator.ydxcfwpt.Service.MyService")) {
                                    Intent intent = new Intent(MainActivity.this, MyService.class);
                                    startService(intent);
                                }
                            }
                            dialog2.dismiss();
                        }
                    });
                } else {
                    //如果没有登录，直接跳转登录界面
                    startLogin();
                    btn_dangqianzhuangtai.setText("下线中");
                }
                break;
//            case R.id.btn_main_qiehuanzhuangtai:
//                if(checkLogin()){
//                    View bottomView = View.inflate(MainActivity.this, R.layout.item_listview_dialog, null);//填充ListView布局
//                    ListView listView2 = (ListView) bottomView.findViewById(R.id.lv_item_listview_dialog);//初始化ListView控件
//                    listView2.setAdapter(new DialogListViewAdapter(MainActivity.this, zhuangtaiList));//ListView设置适配器
//
//                    final AlertDialog dialog2 = new AlertDialog.Builder(MainActivity.this)
//                            .setView(bottomView)//在这里把写好的这个listview的布局加载dialog中
//                            .create();
//                    dialog2.show();
//                    listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {//响应listview中的item的点击事件
//                        @Override
//                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                                long arg3) {
//                            TextView tv = (TextView) arg1.findViewById(R.id.tv_item_listview_dialog_text);//取得每条item中的textview控件
//                            String zhuangtai = tv.getText().toString();
//                            //TODO 根据不同的状态向服务器发送不同请求
//                            if(zhuangtai.equals("临时小休")){
//                                btn_dangqianzhuangtai.setText("小休中");
//                                StateUtils.setState(MainActivity.this,3);
//                                Intent intent = new Intent(MainActivity.this,MyService.class);
//                                stopService(intent);
//                            }else if(zhuangtai.equals("下班回家")){
//                                btn_dangqianzhuangtai.setText("下线中");
//                                StateUtils.setState(MainActivity.this,4);
//                                Intent intent = new Intent(MainActivity.this,MyService.class);
//                                stopService(intent);
//                            }else if(zhuangtai.equals("设备报修")){
//                                btn_dangqianzhuangtai.setText("维修中");
//                                StateUtils.setState(MainActivity.this,9);
//                                Intent intent = new Intent(MainActivity.this,MyService.class);
//                                stopService(intent);
//                            }else if(zhuangtai.equals("我要上线")){
//                                btn_dangqianzhuangtai.setText("上线中");
//                                StateUtils.setState(MainActivity.this,2);
//                                if(!APPUtil.isServiceWork(MainActivity.this,"com.example.administrator.ydxcfwpt.Service.MyService")){
//                                    Intent intent = new Intent(MainActivity.this,MyService.class);
//                                    startService(intent);
//                                }
//                            }
//                            dialog2.dismiss();
//                        }
//                    });
//
//                }else{
//                    //如果没有登录，直接跳转登录界面
//                    startLogin();
//                }
//                break;
            case R.id.iv_cehua_touxiang:
                if (checkLogin()) {
//                    if (checkYanZheng()) {
                    Intent intent = new Intent(MainActivity.this, MyZiliaoActivity.class);
                    startActivity(intent);
//                    }else {
//                    }
                } else {
                    //如果没有登录，直接跳转登录界面
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    startLogin();
                }
                break;
            case R.id.tv_cehua_name:
                if (checkLogin()) {
//                    if (checkYanZheng()){
                    Intent intent = new Intent(MainActivity.this, MyZiliaoActivity.class);
                    startActivity(intent);
//                    }else {
//
//                    }
                } else {
                    //如果没有登录，直接跳转登录界面
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    startLogin();
                }
                break;
            case R.id.tv_cehua_renzheng:
                if (checkLogin()) {

                } else {
                    //如果没有登录，直接跳转登录界面
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    startLogin();
                }
                break;
            case R.id.tv_cehua_ziliao:
                if (checkLogin()) {
                    Intent intent = new Intent(MainActivity.this, MyZiliaoActivity.class);
                    startActivity(intent);
                } else {
                    //如果没有登录，直接跳转登录界面
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    startLogin();
                }
                break;
            case R.id.tv_cehua_qianbao:
                if (checkLogin()) {
                    Intent intent = new Intent(MainActivity.this, MyMoneyActivity.class);
                    startActivity(intent);
                } else {
                    //如果没有登录，直接跳转登录界面
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    startLogin();
                }
                break;
            case R.id.tv_cehua_dingdan:
                if (checkLogin()) {
                    Intent intent = new Intent(MainActivity.this, MyDingdanActivity.class);
                    startActivity(intent);
                } else {
                    //如果没有登录，直接跳转登录界面
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    startLogin();
                }
                break;
            case R.id.tv_cehua_shezhi:
                if (checkLogin()) {
                    Intent intent = new Intent(MainActivity.this, MyshezhiActivity.class);
                    startActivity(intent);
                } else {
                    //如果没有登录，直接跳转登录界面
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    startLogin();
                }
                break;
            case R.id.tv_cehua_kefu:
                if (checkLogin()) {
                } else {
                    //如果没有登录，直接跳转登录界面
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    startLogin();
                }
                break;
        }
    }

    /**
     * 实现定位监听 位置一旦有所改变就会调用这个方法
     * 可以在这个方法里面获取到定位之后获取到的一系列数据
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation location) {
            //获取定位结果
            location.getTime();    //获取定位时间
            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            location.getLocType();    //获取定位类型
            location.getLatitude();    //获取纬度信息
            location.getLongitude();    //获取经度信息
            location.getRadius();    //获取定位精准度
            location.getAddrStr();    //获取地址信息
            location.getCountry();    //获取国家信息
            location.getCountryCode();    //获取国家码
            location.getCity();    //获取城市信息
            location.getCityCode();    //获取城市码
            location.getDistrict();    //获取区县信息
            location.getStreet();    //获取街道信息
            location.getStreetNumber();    //获取街道码
            location.getLocationDescribe();    //获取当前位置描述信息
            location.getPoiList();    //获取当前位置周边POI信息
            location.getBuildingID();    //室内精准定位下，获取楼宇ID
            location.getBuildingName();    //室内精准定位下，获取楼宇名称
            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nCityCode : ");
                sb.append(location.getCityCode());
                sb.append("\nCity : ");
                sb.append(location.getCity());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\ndistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nprovince : ");
                sb.append(location.getProvince());
                Contast.Province = location.getProvince();
                Contast.City = location.getCity();
                Contast.District = location.getDistrict();
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            lat = location.getLatitude();
            lon = location.getLongitude();
            Contast.Lat = lat;
            Contast.Lon = lon;
            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (isFirstLocation) {
                Log.v("pcw", "lat : " + lat + " lon : " + lon);
                Log.i("BaiduLocationApiDem", sb.toString());
                isFirstLocation = false;
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        return location.getLocationDescribe();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        dangqianweizhi = s;
                        Log.i(TAG, "onPostExecute: " + dangqianweizhi);
                        tv_weizhi.setText(dangqianweizhi);
                    }
                }.execute();
//                setMarker();
//                setUserMapCenter();
            }
//            Log.v("pcw", "lat : " + lat + " lon : " + lon);
//            Log.i("BaiduLocationApiDem", sb.toString());
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return location.getLocationDescribe();
                }

                @Override
                protected void onPostExecute(String s) {
                    dangqianweizhi = s;
//                    Log.i(TAG, "onPostExecute: " + dangqianweizhi);
                    tv_weizhi.setText(dangqianweizhi);
                }
            }.execute();
        }
//        @Override
//        public void onConnectHotSpotMessage(String s, int i) {
//        }
    }

    private void initViews() {
        zhuangtaiList = new ArrayList<>();
//        zhuangtaiList.add("临时小休");
        zhuangtaiList.add("下班回家");
        zhuangtaiList.add("我要上线");
//        zhuangtaiList.add("设备报修");
        iv_shijian = (InstrumentView) findViewById(R.id.iv_main_shichang);
        siv_shouyi = (SyInstrumentView) findViewById(R.id.iv_main_shouyi);
        tv_weizhi = (TextView) findViewById(R.id.tv_main_weizhi);
        btn_shuaxin = (Button) findViewById(R.id.btn_main_shuaxin);
        lv_ch_huiyuan = (ImageView) findViewById(R.id.iv_ch_huiyuandengji);
        iv_touxiang = (CircleImageView) findViewById(R.id.iv_main_touxiang);
        btn_dangqianzhuangtai = (Button) findViewById(R.id.btn_main_dangqianzhuangtai);
//      btn_qiehuanzhuangtai = (Button) findViewById(R.id.btn_main_qiehuanzhuangtai);
        iv_touxiang.setOnClickListener(this);
        btn_shuaxin.setOnClickListener(this);
        btn_dangqianzhuangtai.setOnClickListener(this);

//      btn_qiehuanzhuangtai.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDraweLayout();
        if (checkLogin()) {
            if (Contast.worker.getW_WSID() == 1) {
                btn_dangqianzhuangtai.setText("下线中");
            } else if (Contast.worker.getW_WSID() == 2 || Contast.worker.getW_WSID() == 5 || Contast.worker.getW_WSID() == 6 ||
                    Contast.worker.getW_WSID() == 7 || Contast.worker.getW_WSID() == 8 || Contast.worker.getW_WSID() == 10) {
                btn_dangqianzhuangtai.setText("上线中");

//            } else if (Contast.worker.getW_WSID() == 3) {
//                btn_dangqianzhuangtai.setText("小休中");
//            } else if (Contast.worker.getW_WSID() == 9) {
//                btn_dangqianzhuangtai.setText("维修中");
            }
            if (TextUtils.isEmpty(Contast.worker.getW_Image())) {
                Picasso.with(MainActivity.this).load(R.drawable.morentouxiang).into(iv_touxiang);
            } else {
                Uri image = Uri.parse(Contast.Domain + Contast.worker.getW_Image());
                Picasso.with(MainActivity.this).load(image)
                        .placeholder(R.drawable.touxiang)
                        .error(R.drawable.touxiang)
                        .into(iv_touxiang);
            }
            refelash();
            setPro();
            jine();
            if (zhuangtai == 1) {
                YuanGongZhuangtai.setState(MainActivity.this, 2);
                SharedPreferences sp =ContextUtil.getInstance().getSharedPreferences("Login", MODE_PRIVATE);
                String tel= sp.getString("W_Tel","");
                String imei=sp.getString("W_IMEI","");
                FormBody.Builder params = new FormBody.Builder();
                params.add("W_Tel", tel);
                params.add("W_IMEI", imei);
                params.add("W_WSID", "" + 2);
                params.add("keys", Contast.KEYS);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url_setState)
                        .post(params.build())
                        .build();

                okhttp3.Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        //响应失败
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                        String string = response.body().string();
                        Log.i(TAG, "onResponse: json=" + string);
                        if (!TextUtils.isEmpty(string)) {
                            if (string.contains("ErrorStr")) {
                                final Error error = JSON.parseObject(string, Error.class);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        Toast.makeText(activity, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Worker worker = JSON.parseObject(string, Worker.class);
                                Contast.worker = worker;
                                Log.i(TAG, "onResponse: " + worker.toString());
                            }
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
                btn_dangqianzhuangtai.setText("上线中");
            } else if (zhuangtai == 2) {
//              YuanGongZhuangtai.setState(MainActivity.this, 3);
//              btn_dangqianzhuangtai.setText("小休中");
                YuanGongZhuangtai.setState(MainActivity.this, 1);
                SharedPreferences sp =ContextUtil.getInstance().getSharedPreferences("Login", MODE_PRIVATE);
                String tel= sp.getString("W_Tel","");
                String imei=sp.getString("W_IMEI","");
                FormBody.Builder params = new FormBody.Builder();
                params.add("W_Tel", tel);
                params.add("W_IMEI", imei);
                params.add("W_WSID", "" + 1);
                params.add("keys", Contast.KEYS);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url_setState)
                        .post(params.build())
                        .build();

                okhttp3.Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        //响应失败
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                        String string = response.body().string();
                        Log.i(TAG, "onResponse: json=" + string);
                        if (!TextUtils.isEmpty(string)) {
                            if (string.contains("ErrorStr")) {
                                final Error error = JSON.parseObject(string, Error.class);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        Toast.makeText(activity, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Worker worker = JSON.parseObject(string, Worker.class);
                                Contast.worker = worker;
                                Log.i(TAG, "onResponse: " + worker.toString());
                            }
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
                btn_dangqianzhuangtai.setText("下班回家");
            }
            if (!APPUtil.isServiceWork(this, "com.example.administrator.ydxcfwpt.Service.MyService")) {
                Intent intent2 = new Intent(this, MyService.class);
                startService(intent2);
            }
        } else {
            btn_dangqianzhuangtai.setText("未登录");
            Picasso.with(MainActivity.this).load(R.drawable.morentouxiang).into(iv_touxiang);
        }
    }

    private void refelash() {
//        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
//        pd.setMessage("拼命加载中...");
//        pd.show();
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String tel = sp.getString("W_Tel", "");
        String imei = sp.getString("W_IMEI", "");
        FormBody.Builder params = new FormBody.Builder();
        params.add("keys", Contast.KEYS);
        params.add("O_WorkerTel", tel);
        params.add("W_IMEI", imei);
        params.add("month", "");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(MainActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
//                pd.dismiss();
                final String string = response.body().string();
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    Toast.makeText(MainActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                } else {
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            final Error error = JSON.parseObject(string, Error.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dingdanList.clear();
                                    List<Dingdan> dingdens = new ArrayList<Dingdan>();
                                    dingdens = JSON.parseArray(string, Dingdan.class);
                                    for (Dingdan dingdan : dingdens) {
                                        if (dingdan.getO_ISCancel() == 0) {
                                            dingdanList.add(dingdan);
                                            jine();
                                            SharedPreferences sp = getSharedPreferences("dingdan_zhuangtai", MODE_PRIVATE);
                                            SharedPreferences.Editor edit = sp.edit();
                                            edit.putString("dd", dingdanList.toString());
                                            edit.commit();
                                            //jiedan(position);//自动接单
                                            Log.e("订单：", dingdanList.toString() + "");
                                        }
                                    }
                                    adapter.setData(dingdanList);
                                    Log.i(TAG, dingdanList.toString());
                                    setPro();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(MainActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private void setPro() {
        int time = Contast.worker.getW_OnlineTodayTime();
        Log.i(TAG, "setPro: time=" + (time / 6));
        iv_shijian.setProgress(time / 6);
        siv_shouyi.setProgress(Contast.worker.getW_TodayMoney());

    }

    /**
     * 捕捉back
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                new android.app.AlertDialog.Builder(this).setTitle("确认退出吗？")
//                         .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!checkLogin()) {
                                    MainActivity.this.finish();
                                } else {
                                    YuanGongZhuangtai.setState(MainActivity.this, 4);
                                    MainActivity.this.finish();
                                }
                            }
                        })
                        .setNegativeButton("返回", null)
                        .show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jiedan(int position) {
//        SharedPreferences sf = getSharedPreferences("dingdan_zhuangtai", MODE_PRIVATE);
//        String o_id = sf.getString("changhao", "");
        FormBody.Builder params = new FormBody.Builder();
        params.add("O_ID",dingdanList.get(position).getO_ID());
        params.add("W_Tel", Contast.worker.getW_Tel());
        params.add("W_IMEI", Contast.worker.getW_IMEI());
        params.add("O_TypeID", "2");
        params.add("keys", Contast.KEYS);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url_jiedan)
                .post(params.build())
                .build();

        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //响应失败
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                String string = response.body().string();
                Log.i(TAG, "onResponse: json=" + string);
                if (!TextUtils.isEmpty(string)) {
                    if (string.contains("ErrorStr")) {
                        final Error error = JSON.parseObject(string, Error.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                      Toast.makeText(activity, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "已接单", Toast.LENGTH_SHORT).show();
                                refelash();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }


    public void zhuanchu(int position) {
//        SharedPreferences sp = getSharedPreferences("dingdan_zhuangtai", MODE_PRIVATE);
//        String o_id = sp.getString("duanhao", "");
        FormBody.Builder params = new FormBody.Builder();
        params.add("O_ID", dingdanList.get(position).getO_IDS()+"");
        params.add("O_WorkerID", Contast.worker.getW_ID() + "");
        params.add("O_ISCancelValue", "行程改变");
        params.add("keys", Contast.KEYS);
        Log.w("qb", "o_id"  + "O_WorkerID" + Contast.worker.getW_ID() + "");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url_zhanchu)
                .post(params.build())
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "网络连接异样，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("qq", "qq" + string);
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            final Error error = JSON.parseObject(string, Error.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                           Toast.makeText(activity, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "已转出", Toast.LENGTH_SHORT).show();
                                    refelash();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
    public void jine() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String dianhua = sp.getString("W_Tel", "");
        String imei=sp.getString("W_IMEI","");
        FormBody.Builder params = new FormBody.Builder();
        params.add("W_Tel", dianhua);
        params.add("W_IMEI", imei);
        params.add("W_Lng",""+Contast.Lon);
        params.add("W_Lat","" + Contast.Lat);
        params.add("keys", Contast.KEYS);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url_jine)
                .post(params.build())
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "网络连接异样，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            final Error error = JSON.parseObject(string, Error.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                           Toast.makeText(activity, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Worker worker = JSON.parseObject(string, Worker.class);
                                    Contast.worker = worker;
                                    Log.w("今日收益",""+Contast.worker.getW_TodayMoney());
                                    double money = Contast.worker.getW_TodayMoney();
                                    siv_shouyi.setProgress(money);
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
