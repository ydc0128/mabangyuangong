package com.example.administrator.ydxcfwpt.Activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.administrator.ydxcfwpt.Adapter.DialogListViewAdapter;
import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.Location;
import com.example.administrator.ydxcfwpt.Bean.Worker;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.Utils.ActivityUtils;
import com.example.administrator.ydxcfwpt.Utils.AudioUtils;
import com.example.administrator.ydxcfwpt.Utils.ContextUtil;
import com.example.administrator.ydxcfwpt.Utils.ImageUtils;
import com.example.administrator.ydxcfwpt.Utils.StateUtils;
import com.example.administrator.ydxcfwpt.Utils.YuanGongZhuangtai;
import com.example.administrator.ydxcfwpt.View.NativeDialog;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.example.administrator.ydxcfwpt.R.id.btn_work_chufa;
import static com.example.administrator.ydxcfwpt.R.id.btn_work_fuwuwancheng;
//import static com.example.administrator.ydxcfwpt.R.id.btn_work_jianchawancheng;
//import static com.example.administrator.ydxcfwpt.R.id.btn_work_jixugongzuo;
import static com.example.administrator.ydxcfwpt.R.id.btn_work_kaishifuwu;
//import static com.example.administrator.ydxcfwpt.R.id.btn_work_quxiuxi;
import static com.example.administrator.ydxcfwpt.R.id.btn_work_woyidaoda;
import static com.example.administrator.ydxcfwpt.R.id.iv_work_back;
import static com.example.administrator.ydxcfwpt.R.id.iv_work_car;
//import static com.example.administrator.ydxcfwpt.R.id.iv_work_cemian1;
//import static com.example.administrator.ydxcfwpt.R.id.iv_work_cemian2;
import static com.example.administrator.ydxcfwpt.R.id.iv_work_daohang;
//import static com.example.administrator.ydxcfwpt.R.id.iv_work_fanmian;
//import static com.example.administrator.ydxcfwpt.R.id.iv_work_qita1;
//import static com.example.administrator.ydxcfwpt.R.id.iv_work_qita2;
import static com.example.administrator.ydxcfwpt.R.id.iv_work_tel;
import static com.example.administrator.ydxcfwpt.R.id.iv_work_xichehou;
import static com.example.administrator.ydxcfwpt.R.id.iv_work_xicheqian;
//import static com.example.administrator.ydxcfwpt.R.id.iv_work_zhengmian;
import static com.example.administrator.ydxcfwpt.R.id.pv_work_max;
import static com.example.administrator.ydxcfwpt.R.id.tv_work_beizhu;
import static com.example.administrator.ydxcfwpt.R.id.tv_work_chepaihao;
import static com.example.administrator.ydxcfwpt.R.id.tv_work_dingdanhao;
import static com.example.administrator.ydxcfwpt.R.id.tv_work_dizhi;
import static com.example.administrator.ydxcfwpt.R.id.tv_work_jutidizhi;
import static com.example.administrator.ydxcfwpt.R.id.tv_work_name;
import static com.example.administrator.ydxcfwpt.R.id.tv_work_phone;
import static com.example.administrator.ydxcfwpt.R.id.tv_work_title;


public class WorkActivity extends BaseActivity implements View.OnClickListener {
    //工作页面
    private String url_jiedan = Contast.Domain + "api/OrderState.ashx";
    private static final String TAG = "WorkActivity";
    private String url = Contast.Domain + "api/OrderState.ashx";
    private String url_out = Contast.Domain + "api/WorkerStateNow.ashx?";
    //    private String url_checked = Contast.Domain + "api/OrderScratch.ashx?";
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private List<String> photoList;
    private WorkActivity activity;
    private ImageView iv_back;
    private TextView tv_title;

    private ImageView iv_daohang;
    private ImageView iv_car;
    private ImageView iv_phone;
    private TextView tv_chepaihao;
    private TextView tv_dingdanhao;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_dizhi;
    private TextView tv_jutidizhi;
    private PhotoView pv_max;
    private TextView tv_beizhu;

    private ImageView iv_xicheqian;
    private ImageView iv_xichehou;
    //    private ImageView iv_zhengmian;
//    private ImageView iv_fanmian;
//    private ImageView iv_cemian1;
//    private ImageView iv_cemian2;
//    private ImageView iv_qita1;
//    private ImageView iv_qita2;
    private List<Dingdan> dingdanList;
    private Button btn_woyidaoda;
    //    private Button btn_jianchawancheng;
    private Button btn_kaishifuwu;
    private Button btn_fuwuwancheng;
    //    private Button btn_jixugongzuo;
//    private Button btn_xiuxiyihui;
    private Button btn_chufa;


    double carLat;
    double carLon;
    Location loc_car = new Location(carLat, carLon);
    private boolean isFirstLocation = true;
    public LocationClient mLocationClient = null;

    public BDLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaiduMap;
    private MapView mMapView;

    private double lat;
    private double lon;
    Location loc_start;
    private int zoom;
    private List<String> phone;

    private File finalFile;
    //    private File zhengmianFile;
//    private File fanmianFile;
//    private File cemian1File;
//    private File cemian2File;
//    private File qita1File;
//    private File qita2File;
    private Uri imageUri;
    private int from = -1;


    private Timer timer;
    private int time = 60 * 15;//15分钟
    private Dingdan dingdan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        //使用百度地图的任何功能都需要先初始化这段代码  最好放在全局中进行初始化
        //百度地图+定位+marker比较简单 我就不放到全局去了
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_work);

        Intent intent = getIntent();
        dingdan = (Dingdan) intent.getSerializableExtra("dingdan");
        Log.i(TAG, "onCreate: zhuangtai = " + dingdan.getO_TypeID());
        carLat = dingdan.getO_Lat();
        carLon = dingdan.getO_Lng();

        mMapView = (MapView) findViewById(R.id.mv_work_ditu);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setAllGesturesEnabled(false);   //关闭一切手势操作
        settings.setOverlookingGesturesEnabled(false);//屏蔽双指下拉时变成3D地图
        settings.setRotateGesturesEnabled(false);//屏蔽旋转
        settings.setZoomGesturesEnabled(true);//获取是否允许缩放手势返回:是否允许缩放手势
        initViews();
        setPic();
        setViews();
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //配置定位参数
        initLocation();
        //开始定位
        mLocationClient.start();
//        kaishifuwu(dingdan);

    }

    private void setPic() {
        if (!TextUtils.isEmpty(dingdan.getO_CarImage())) {
            String url = Contast.Domain + dingdan.getO_CarImage();
            Picasso.with(this).load(url).into(iv_car);
        } else {
            Picasso.with(this).load(R.drawable.paizhao).into(iv_car);
        }
        if (!TextUtils.isEmpty(dingdan.getO_BeforePic())) {
            String url = Contast.Domain + dingdan.getO_BeforePic();
            Picasso.with(this).load(url).into(iv_xicheqian);
        } else {
            Picasso.with(this).load(R.drawable.paizhao).into(iv_xicheqian);
        }
        if (!TextUtils.isEmpty(dingdan.getO_AfterPic())) {
            String url = Contast.Domain + dingdan.getO_AfterPic();
            Picasso.with(this).load(url).into(iv_xichehou);
        } else {
            Picasso.with(this).load(R.drawable.paizhao).into(iv_xichehou);
        }
//        if(!TextUtils.isEmpty(dingdan.getO_ScratchImage1())){
//            String url = Contast.Domain+dingdan.getO_ScratchImage1();
//            Picasso.with(this).load(url).into(iv_zhengmian);
//        }else{
//            Picasso.with(this).load(R.drawable.addcar).into(iv_zhengmian);
//        }
//        if(!TextUtils.isEmpty(dingdan.getO_ScratchImage2())){
//            String url = Contast.Domain+dingdan.getO_ScratchImage2();
//            Picasso.with(this).load(url).into(iv_fanmian);
//        }else{
//            Picasso.with(this).load(R.drawable.addcar).into(iv_fanmian);
//        }
//        if(!TextUtils.isEmpty(dingdan.getO_ScratchImage3())){
//            String url = Contast.Domain+dingdan.getO_ScratchImage3();
//            Picasso.with(this).load(url).into(iv_cemian1);
//        }else{
//            Picasso.with(this).load(R.drawable.addcar).into(iv_cemian1);
//        }
//        if(!TextUtils.isEmpty(dingdan.getO_ScratchImage4())){
//            String url = Contast.Domain+dingdan.getO_ScratchImage4();
//            Picasso.with(this).load(url).into(iv_cemian2);
//        }else{
//            Picasso.with(this).load(R.drawable.addcar).into(iv_cemian2);
//        }
//        if(!TextUtils.isEmpty(dingdan.getO_ScratchImage5())){
//            String url = Contast.Domain+dingdan.getO_ScratchImage5();
//            Picasso.with(this).load(url).into(iv_qita1);
//        }else{
//            Picasso.with(this).load(R.drawable.addcar).into(iv_qita1);
//        }
//        if(!TextUtils.isEmpty(dingdan.getO_ScratchImage1())){
//            String url = Contast.Domain+dingdan.getO_ScratchImage6();
//            Picasso.with(this).load(url).into(iv_qita2);
//        }else{
//            Picasso.with(this).load(R.drawable.addcar).into(iv_qita2);
//        }
    }

    private void initViews() {
        timer = new Timer();
        phone = new ArrayList<>();
        phone.add(dingdan.getO_UTel());
        photoList = new ArrayList<>();
        photoList.add("拍照");
//        photoList.add("从相册中选择");
        iv_back = (ImageView) findViewById(iv_work_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title = (TextView) findViewById(tv_work_title);
        iv_car = (ImageView) findViewById(iv_work_car);
        pv_max = (PhotoView) findViewById(pv_work_max);
        tv_chepaihao = (TextView) findViewById(tv_work_chepaihao);
        tv_dingdanhao = (TextView) findViewById(tv_work_dingdanhao);
        tv_name = (TextView) findViewById(tv_work_name);
        tv_phone = (TextView) findViewById(tv_work_phone);
        tv_dizhi = (TextView) findViewById(tv_work_dizhi);
        tv_beizhu = (TextView) findViewById(tv_work_beizhu);
        tv_jutidizhi = (TextView) findViewById(tv_work_jutidizhi);
        iv_phone = (ImageView) findViewById(R.id.iv_work_tel);
        iv_daohang = (ImageView) findViewById(R.id.iv_work_daohang);
        btn_woyidaoda = (Button) findViewById(R.id.btn_work_woyidaoda);
//        btn_jianchawancheng = (Button) findViewById(R.id.btn_work_jianchawancheng);
        btn_kaishifuwu = (Button) findViewById(R.id.btn_work_kaishifuwu);
        btn_fuwuwancheng = (Button) findViewById(R.id.btn_work_fuwuwancheng);
//        btn_jixugongzuo = (Button) findViewById(R.id.btn_work_jixugongzuo);
//        btn_xiuxiyihui = (Button) findViewById(R.id.btn_work_quxiuxi);
        btn_chufa = (Button) findViewById(R.id.btn_work_chufa);
        iv_xicheqian = (ImageView) findViewById(iv_work_xicheqian);
        iv_xichehou = (ImageView) findViewById(iv_work_xichehou);
//        iv_zhengmian = (ImageView) findViewById(iv_work_zhengmian);
//        iv_fanmian = (ImageView) findViewById(iv_work_fanmian);
//        iv_cemian1 = (ImageView) findViewById(iv_work_cemian1);
//        iv_cemian2 = (ImageView) findViewById(iv_work_cemian2);
//        iv_qita1 = (ImageView) findViewById(iv_work_qita1);
//        iv_qita2 = (ImageView) findViewById(iv_work_qita2);

        pv_max.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                pv_max.setVisibility(View.GONE);
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });
        iv_xicheqian.setOnClickListener(this);
        iv_xichehou.setOnClickListener(this);
//        iv_zhengmian.setOnClickListener(this);
//        iv_fanmian.setOnClickListener(this);
//        iv_cemian1.setOnClickListener(this);
//        iv_cemian2.setOnClickListener(this);
//        iv_qita1.setOnClickListener(this);
//        iv_qita2.setOnClickListener(this);
//        iv_back.setOnClickListener(this);
        iv_car.setOnClickListener(this);
        iv_phone.setOnClickListener(this);
        iv_daohang.setOnClickListener(this);
        btn_woyidaoda.setOnClickListener(this);
//        btn_jianchawancheng.setOnClickListener(this);
        btn_kaishifuwu.setOnClickListener(this);
        btn_fuwuwancheng.setOnClickListener(this);
//        btn_jixugongzuo.setOnClickListener(this);
//        btn_xiuxiyihui.setOnClickListener(this);
        btn_chufa.setOnClickListener(this);
        btn_woyidaoda.setClickable(false);
//      btn_jianchawancheng.setClickable(false);
        btn_kaishifuwu.setClickable(false);
        btn_fuwuwancheng.setClickable(false);
//        btn_jixugongzuo.setClickable(false);
//        btn_xiuxiyihui.setClickable(false);
        iv_xicheqian.setClickable(false);
        iv_xichehou.setClickable(false);
        btn_chufa.setClickable(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case iv_work_back:
                finish();
                break;
            case iv_work_car:
                String url_image = Contast.Domain + dingdan.getO_CarImage();
                Picasso.with(this).load(url_image).into(pv_max);
                pv_max.setVisibility(View.VISIBLE);
                break;
            case iv_work_tel:
                View photoView1 = View.inflate(WorkActivity.this, R.layout.item_listview_dialog, null);//填充ListView布局
                ListView listView1 = (ListView) photoView1.findViewById(R.id.lv_item_listview_dialog);//初始化ListView控件
                listView1.setAdapter(new DialogListViewAdapter(WorkActivity.this, phone));//ListView设置适配器

                final AlertDialog dialog1 = new AlertDialog.Builder(WorkActivity.this)
                        .setView(photoView1)//在这里把写好的这个listview的布局加载dialog中
                        .create();
                dialog1.show();
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv = (TextView) view.findViewById(R.id.tv_item_listview_dialog_text);//取得每条item中的textview控件
                        String dianhua = tv.getText().toString();
                        // 拨号：激活系统的拨号组件
                        Intent intent = new Intent(); // 意图对象：动作 + 数据
                        intent.setAction(Intent.ACTION_DIAL); // 设置动作
                        Uri data = Uri.parse("tel:" + dianhua); // 设置数据
                        intent.setData(data);
                        startActivity(intent); // 激活Activity组件
                    }
                });
                break;
            case iv_work_daohang:
                loc_car=new Location(dingdan.getO_Lat(),dingdan.getO_Lng(),dingdan.getO_Adress());
                NativeDialog dialog = new NativeDialog(WorkActivity.this, loc_start, loc_car);
                Log.w("地图",loc_start+""+loc_car+"");
                dialog.show();
//                String mudi=dingdan.getO_Adress();
//                Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("baidumap://map/direction?origin=我的位置&destination="+mudi+""));
//                intent.setPackage("com.baidu.BaiduMap");
//                startActivity(intent); //启动调用
                break;
            case btn_work_woyidaoda:
//                final ProgressDialog pd = new ProgressDialog(WorkActivity.this);
//                pd.setMessage("拼命加载中...");
//                pd.show();
                if (dingdan.getO_ISCancel() == 0) {
//                   StateUtils.setState(WorkActivity.this, 8);
                    SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                    String tel = sp.getString("W_Tel", "");
                    String imei = sp.getString("W_IMEI", "");
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("O_ID", dingdan.getO_ID());
                    params.add("W_Tel", tel);
                    params.add("W_IMEI", imei);
                    params.add("O_TypeID", "8");
                    params.add("keys", Contast.KEYS);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
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
                                    Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(WorkActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tv_title.setText("已到达目的地");
                                            btn_woyidaoda.setBackgroundResource(R.drawable.btn_hui_bg);
                                            btn_woyidaoda.setTextColor(Color.GRAY);
                                            btn_kaishifuwu.setBackgroundResource(R.drawable.btn_blue_bg);
                                            btn_kaishifuwu.setTextColor(Color.parseColor("#ff33b5e5"));
                                            iv_xicheqian.setClickable(true);
                                            btn_kaishifuwu.setClickable(true);
                                            btn_woyidaoda.setClickable(false);
                                            btn_chufa.setClickable(false);
                                        }
                                    });

                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });
                } else {
                    Toast.makeText(WorkActivity.this, "订单已取消", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(WorkActivity.this, MainActivity.class);
                    startActivity(intent1);
                }
//                if (dingdan.getO_ISCancel() == 0) {
//                    FormBody.Builder params = new FormBody.Builder();
//                    params.add("O_ID", dingdan.getO_ID());
//                    params.add("W_Tel", Contast.worker.getW_Tel());
//                    params.add("W_IMEI", Contast.worker.getW_IMEI());
//                    params.add("W_WSID", "6");
//                    params.add("data", "");
//                    params.add("keys", Contast.KEYS);
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder()
//                            .url(url)
//                            .post(params.build())
//                            .build();
//                    okhttp3.Call call = client.newCall(request);
//                    call.enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            pd.dismiss();
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(WorkActivity.this, "网络连接异样，请稍后再试", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            pd.dismiss();
//                            String string = response.body().string();
//                            if (response.code() != HttpURLConnection.HTTP_OK) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(WorkActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            } else {
//                                if (!TextUtils.isEmpty(string)) {
//                                    if (string.contains("ErrorStr")) {
//                                        final Error error = JSON.parseObject(string, Error.class);
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Toast.makeText(WorkActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    } else {
//                                        dingdan = JSON.parseObject(string, Dingdan.class);
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                if (dingdan.getO_ISCancel() == 1) {
//                                                    Toast.makeText(WorkActivity.this, "该订单已被用户取消", Toast.LENGTH_SHORT).show();
//                                                    finish();
//                                                } else {
//                                                    tv_title.setText("已到达目的地");
//                                                    btn_woyidaoda.setBackgroundResource(R.drawable.btn_hui_bg);
//                                                    btn_woyidaoda.setTextColor(Color.GRAY);
//                                                    btn_kaishifuwu.setBackgroundResource(R.drawable.btn_blue_bg);
//                                                    btn_kaishifuwu.setTextColor(Color.parseColor("#ff33b5e5"));
//                                                    iv_xicheqian.setClickable(true);
//                                                    btn_kaishifuwu.setClickable(true);
//                                                    btn_woyidaoda.setClickable(false);
//                                                }
//                                            }
//                                        });
//                                    }
//                                } else {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            }
//                        }
//                    });
//                } else {
//                    FormBody.Builder params10 = new FormBody.Builder();
//                    params10.add("W_Tel", Contast.worker.getW_Tel());
//                    params10.add("W_IMEI", Contast.worker.getW_IMEI());
//                    params10.add("W_WSID", "2");
//                    params10.add("keys", Contast.KEYS);
//                    OkHttpClient client10 = new OkHttpClient();
//                    Request request10 = new Request.Builder()
//                            .url(url)
//                            .post(params10.build())
//                            .build();
//
//                    okhttp3.Call call10 = client10.newCall(request10);
//                    call10.enqueue(new Callback() {
//                        @Override
//                        public void onFailure(okhttp3.Call call, IOException e) {
//                            //响应失败
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(WorkActivity.this, "网络连接异样，请稍后重试...", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onResponse(okhttp3.Call call, Response response) throws IOException {
//                            //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
//                            String string = response.body().string();
//                            if (response.code() != HttpURLConnection.HTTP_OK) {
//                                Toast.makeText(WorkActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Log.i(TAG, "onResponse: json=" + string);
//                                if (!TextUtils.isEmpty(string)) {
//                                    if (string.contains("ErrorStr")) {
//                                        final Error error = JSON.parseObject(string, Error.class);
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Toast.makeText(WorkActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    } else {
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Toast.makeText(WorkActivity.this, "订单已取消", Toast.LENGTH_SHORT).show();
//                                                Intent intent1 = new Intent(WorkActivity.this, MainActivity.class);
//                                                startActivity(intent1);
//                                            }
//                                        });
//                                    }
//                                } else {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            }
//                        }
//                    });
//
//                }
                break;
//            case btn_work_jianchawancheng:
//                if(zhengmianFile==null||fanmianFile==null||cemian1File==null
//                        ||cemian2File==null||qita1File==null||qita2File==null){
//                    Toast.makeText(WorkActivity.this,"请上传全部车辆图片",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                final ProgressDialog pd7 = new ProgressDialog(WorkActivity.this);
//                pd7.setMessage("拼命加载中...");
//                pd7.show();
//                FormBody.Builder params7 = new FormBody.Builder();
//                String imageStr3 = ImageUtils.getBase64String(zhengmianFile);
//                String imageStr4 = ImageUtils.getBase64String(fanmianFile);
//                String imageStr5 = ImageUtils.getBase64String(cemian1File);
//                String imageStr6 = ImageUtils.getBase64String(cemian2File);
//                String imageStr7 = ImageUtils.getBase64String(qita1File);
//                String imageStr8 = ImageUtils.getBase64String(qita2File);
//                params7.add("O_ID", dingdan.getO_ID());
//                params7.add("W_Tel", Contast.worker.getW_Tel());
//                params7.add("W_IMEI", Contast.worker.getW_IMEI());
//                params7.add("data1", imageStr3);
//                params7.add("data2", imageStr4);
//                params7.add("data3", imageStr5);
//                params7.add("data4", imageStr6);
//                params7.add("data5", imageStr7);
//                params7.add("data6", imageStr8);
//                params7.add("keys", Contast.KEYS);
//                OkHttpClient client7 = new OkHttpClient();
//                Request request7 = new Request.Builder()
//                        .url(url_checked)
//                        .post(params7.build())
//                        .build();
//                okhttp3.Call call7 = client7.newCall(request7);
//                call7.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        pd7.dismiss();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        pd7.dismiss();
//                        String string = response.body().string();
//                        if (!TextUtils.isEmpty(string)) {
//                            if (string.contains("ErrorStr")) {
//                                final Error error = JSON.parseObject(string, Error.class);
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(WorkActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            } else  if (string.equals("ok")){
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        tv_title.setText("检查完成");
//                                        btn_jianchawancheng.setBackgroundResource(R.drawable.btn_hui_bg);
//                                        btn_jianchawancheng.setTextColor(Color.GRAY);
//                                        btn_kaishifuwu.setBackgroundResource(R.drawable.btn_blue_bg);
//                                        btn_kaishifuwu.setTextColor(Color.parseColor("#ff33b5e5"));
//                                        btn_kaishifuwu.setClickable(true);
//                                        iv_xicheqian.setClickable(true);
//                                        iv_zhengmian.setClickable(false);
//                                        iv_fanmian.setClickable(false);
//                                        iv_cemian1.setClickable(false);
//                                        iv_cemian2.setClickable(false);
//                                        iv_qita1.setClickable(false);
//                                        iv_qita2.setClickable(false);
//                                        btn_jianchawancheng.setClickable(false);
//                                    }
//                                });
//                            }
//                        } else {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    }
//                });
//                break;
            case btn_work_kaishifuwu:
                if (finalFile == null) {
                    Toast.makeText(WorkActivity.this, "请先上传车辆照片", Toast.LENGTH_SHORT).show();
                    return;
                }
//                final ProgressDialog pd1 = new ProgressDialog(WorkActivity.this);
//                pd1.setMessage("拼命加载中...");
//                pd1.show();
                if (dingdan.getO_ISCancel() == 1) {
                    finish();
                    Toast.makeText(WorkActivity.this, "订单已取消", Toast.LENGTH_SHORT).show();
                } else {


                    SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                    String tel = sp.getString("W_Tel", "");
                    String imei = sp.getString("W_IMEI", "");
                    FormBody.Builder params = new FormBody.Builder();
                    String imageStr = ImageUtils.getBase64String(finalFile);
                    params.add("O_ID", dingdan.getO_ID());
                    params.add("W_Tel", tel);
                    params.add("W_IMEI", imei);
                    params.add("data", imageStr);
                    params.add("O_TypeID", "3");
                    params.add("keys", Contast.KEYS);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
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
                                    Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(WorkActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tv_title.setText("洗车中");
                                            btn_kaishifuwu.setBackgroundResource(R.drawable.btn_hui_bg);
                                            btn_kaishifuwu.setTextColor(Color.GRAY);
                                            btn_fuwuwancheng.setBackgroundResource(R.drawable.btn_blue_bg);
                                            btn_fuwuwancheng.setTextColor(Color.parseColor("#ff33b5e5"));
                                            btn_fuwuwancheng.setClickable(true);
                                            iv_xicheqian.setClickable(false);
                                            iv_xichehou.setClickable(true);
                                            btn_kaishifuwu.setClickable(false);
                                            btn_woyidaoda.setClickable(false);
                                            finalFile = null;
                                        }
                                    });
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });
                }
//                FormBody.Builder params1 = new FormBody.Builder();
//                String imageStr1 = ImageUtils.getBase64String(finalFile);
//                params1.add("data", imageStr1);
//                params1.add("O_ID", dingdan.getO_ID());
//                params1.add("W_Tel", Contast.worker.getW_Tel());
//                params1.add("W_IMEI", Contast.worker.getW_IMEI());
//                params1.add("W_WSID", "7");
//                params1.add("keys", Contast.KEYS);
//                OkHttpClient client1 = new OkHttpClient();
//                Request request1 = new Request.Builder()
//                        .url(url)
//                        .post(params1.build())
//                        .build();
//                okhttp3.Call call1 = client1.newCall(request1);
//                call1.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        pd1.dismiss();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(WorkActivity.this, "网络连接异样，请稍后再试", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        pd1.dismiss();
//                        String string = response.body().string();
//                        Log.i(TAG, "onResponse: string =" + string);
//                        if (response.code() != HttpURLConnection.HTTP_OK) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(WorkActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        } else {
//                            if (!TextUtils.isEmpty(string)) {
//                                if (string.contains("ErrorStr")) {
//                                    final Error error = JSON.parseObject(string, Error.class);
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(WorkActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//
//                                } else {
////                                Contast.worker = JSON.parseObject(string, Worker.class);
//                                    dingdan = JSON.parseObject(string, Dingdan.class);
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            tv_title.setText("洗车中");
//                                            btn_kaishifuwu.setBackgroundResource(R.drawable.btn_hui_bg);
//                                            btn_kaishifuwu.setTextColor(Color.GRAY);
//                                            btn_fuwuwancheng.setBackgroundResource(R.drawable.btn_blue_bg);
//                                            btn_fuwuwancheng.setTextColor(Color.parseColor("#ff33b5e5"));
//                                            btn_fuwuwancheng.setClickable(true);
//                                            iv_xicheqian.setClickable(false);
//                                            iv_xichehou.setClickable(true);
//                                            btn_kaishifuwu.setClickable(false);
//                                            btn_woyidaoda.setClickable(false);
//                                            finalFile = null;
//                                        }
//                                    });
//                                }
//                            } else {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        }
//                    }
//                });
//                SharedPreferences sp = getSharedPreferences("Work", MODE_PRIVATE);
//                SharedPreferences.Editor edit = sp.edit();
//                edit.putLong("time", System.currentTimeMillis());
//                edit.putInt("W_WSID", 7);
//                edit.commit();
                break;
            case btn_work_fuwuwancheng:
                String str = dingdan.getO_StartTime();
                if (TextUtils.isEmpty(str)) {
                    SharedPreferences sp1 = getSharedPreferences("Work", MODE_PRIVATE);
                    long start_time = sp1.getLong("time", 0);
                    long now_time = System.currentTimeMillis();
                    time = (int) ((now_time - start_time) / 1000);
                } else {
                    String replace = str.replace("T", " ");
                    Log.i(TAG, "onClick: start=" + replace);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        long start_time = sdf.parse(replace).getTime();
                        long now_time = System.currentTimeMillis();
                        time = (int) ((now_time - start_time) / 1000);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
//              if (time < 9) {//测试
                    if (time < 9) {//正式
                    Toast.makeText(WorkActivity.this, "您操作太快了，请认真检查车辆是否清洗干净", Toast.LENGTH_SHORT).show();
                } else {
                    if (dingdan.getO_ISCancel() == 1) {
                        finish();
                        Toast.makeText(WorkActivity.this, "订单已取消", Toast.LENGTH_SHORT).show();
                    } else {
                        fuwuwancheng(dingdan);
                    }
                }
                break;
//            case btn_work_jixugongzuo:
//
//                Intent intent=new Intent(WorkActivity.this,MainActivity.class);
//                intent.putExtra("zhuangtai",1);
//                startActivity(intent);
//                FormBody.Builder params9 = new FormBody.Builder();
//                params9.add("W_Tel", Contast.worker.getW_Tel());
//                params9.add("W_IMEI", Contast.worker.getW_IMEI());
//                params9.add("W_WSID", "2");
//                params9.add("keys", Contast.KEYS);
//                OkHttpClient client9 = new OkHttpClient();
//                Request request9 = new Request.Builder()
//                        .url(url_out)
//                        .post(params9.build())
//                        .build();
//
//                okhttp3.Call call9 = client9.newCall(request9);
//                call9.enqueue(new Callback() {
//
//
//                    @Override
//                    public void onFailure(okhttp3.Call call, IOException e) {
//                        //响应失败
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Continue.dismiss();
//                                Toast.makeText(WorkActivity.this, "网络连接异样，请稍后重试...", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
//                        Continue.dismiss();
//                        //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
//                        String string = response.body().string();
//                        if (response.code() != HttpURLConnection.HTTP_OK) {
//                            Toast.makeText(WorkActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.i(TAG, "onResponse: json=" + string);
//                            if (!TextUtils.isEmpty(string)) {
//                                if (string.contains("ErrorStr")) {
//                                    final Error error = JSON.parseObject(string, Error.class);
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(WorkActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                } else {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Intent intent1 = new Intent(WorkActivity.this, MainActivity.class);
//                                            startActivity(intent1);
//                                        }
//                                    });
//                                }
//                            } else {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        }
//                    }
//                });

//                final ProgressDialog pd3 = new ProgressDialog(WorkActivity.this);
//                pd3.setMessage("拼命加载中...");
//                pd3.show();
//                FormBody.Builder params3 = new FormBody.Builder();
//                params3.add("O_ID", dingdan.getO_ID());
//                params3.add("W_Tel", Contast.worker.getW_Tel());
//                params3.add("W_IMEI", Contast.worker.getW_IMEI());
//                params3.add("W_WSID", "2");
//                params3.add("keys", Contast.KEYS);
//                OkHttpClient client3 = new OkHttpClient();
//                Request request3 = new Request.Builder()
//                        .url(url)
//                        .post(params3.build())
//                        .build();
//                okhttp3.Call call3 = client3.newCall(request3);
//                call3.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        pd3.dismiss();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        pd3.dismiss();
//                        String string = response.body().string();
//                        if (!TextUtils.isEmpty(string)) {
//                            if (string.contains("ErrorStr")) {
//                                final Error error = JSON.parseObject(string, Error.class);
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(WorkActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            } else {
//                                dingdan = JSON.parseObject(string, Dingdan.class);
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        ActivityUtils.finishAll();
//                                        Intent intent = new Intent(WorkActivity.this, MainActivity.class);
//                                        intent.putExtra("zhuangtai", 1);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                });
//                            }
//                        } else {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    }
//                });
//                break;
//            case btn_work_quxiuxi:
////                StateUtils.setState(WorkActivity.this, 3);
////                finish();
//                ActivityUtils.finishAll();
//                Intent intent2 = new Intent(WorkActivity.this, MainActivity.class);
//                intent2.putExtra("zhuangtai", 2);
//                startActivity(intent2);
//                finish();
//                break;
            case iv_work_xicheqian:
                dialogListShow(1);
                break;
            case iv_work_xichehou:
                dialogListShow(2);
                break;
            case btn_work_chufa:
                SharedPreferences sp1 = getSharedPreferences("dingdan_zhuangtai", MODE_PRIVATE);
                String dd = sp1.getString("dd", "");
                if (dd.contains("O_TypeID=3") || dd.contains("O_TypeID=4") || dd.contains("O_TypeID=9") || dd.contains("O_TypeID=8")) {
                    Toast.makeText(WorkActivity.this, "您还有订单没有完成", Toast.LENGTH_SHORT).show();
                } else {
                    if (dingdan.getO_WashType() == 0) {
                        chufa();
                    } else {
                        String yy = dingdan.getO_WashTime();
                        Long yytime = getDateFromStr(yy);
                        long time = System.currentTimeMillis();
                        long minutes = yytime - time;
                        long shijiancha = minutes / 1000 / 60;
                        Log.w("nima", shijiancha + "时间差");
                        if (shijiancha > 30) {
                            Log.w("22222222222222", "4444");
                            long day = minutes / (24 * 60 * 60 * 1000);
                            long hour1 = minutes / (60 * 60 * 1000);
                            long min = ((minutes / (60 * 1000)) - day * 24 * 60 - hour1 * 60);
                            Toast.makeText(WorkActivity.this, "离预约时间还有" + day + "天" + hour1 + "小时" + min + "分钟", Toast.LENGTH_SHORT).show();
                            final String content = "离预约时间还有" + day + "天" + hour1 + "小时" + min + "分钟";
                            AudioUtils.getInstance().init(this);
                            AudioUtils.getInstance().speakText(content);
                        } else {
                            chufa();
                        }
                    }
                }
                break;
//            case iv_work_zhengmian:
//                dialogListShow(3);
//                break;
//            case iv_work_fanmian:
//                dialogListShow(4);
//                break;
//            case iv_work_cemian1:
//                dialogListShow(5);
//                break;
//            case iv_work_cemian2:
//                dialogListShow(6);
//                break;
//            case iv_work_qita1:
//                dialogListShow(7);
//                break;
//            case iv_work_qita2:
//                dialogListShow(8);
//                break;
        }
    }

    private void dialogListShow(final int i) {
        View photoView = View.inflate(WorkActivity.this, R.layout.item_listview_dialog, null);//填充ListView布局
        ListView listView = (ListView) photoView.findViewById(R.id.lv_item_listview_dialog);//初始化ListView控件
        listView.setAdapter(new DialogListViewAdapter(WorkActivity.this, photoList));//ListView设置适配器

        final AlertDialog dialog2 = new AlertDialog.Builder(WorkActivity.this)
                .setView(photoView)//在这里把写好的这个listview的布局加载dialog中
                .create();
        dialog2.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//响应listview中的item的点击事件

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                TextView tv = (TextView) arg1.findViewById(R.id.tv_item_listview_dialog_text);//取得每条item中的textview控件
                String aiche = tv.getText().toString();
                if ("拍照".equals(aiche)) {
                    File outputImage = new File(getExternalCacheDir(),
                            "output_image" + i + ".jpg");
                    try {
                        if (outputImage.exists()) {
                            outputImage.delete();
                        }
                        outputImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        imageUri = FileProvider.getUriForFile(WorkActivity.this,
                                "com.example.administrator.ydxcfwpt.fileprovider", outputImage);
                    } else {
                        imageUri = Uri.fromFile(outputImage);
                    }
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    from = i;
                    startActivityForResult(intent, TAKE_PHOTO);
                } else if ("从相册中选择".equals(aiche)) {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    from = i;
                    startActivityForResult(intent, CHOOSE_PHOTO);
                }
                dialog2.dismiss();

            }
        });
    }

    private void fuwuwancheng(Dingdan dingdan1) {
        if (finalFile == null) {
            Toast.makeText(WorkActivity.this, "请先上传车辆照片", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String tel = sp.getString("W_Tel", "");
        String imei = sp.getString("W_IMEI", "");
        FormBody.Builder params = new FormBody.Builder();
        String imageStr2 = ImageUtils.getBase64String(finalFile);
        params.add("O_ID", dingdan.getO_ID());
        params.add("W_Tel", tel);
        params.add("W_IMEI", imei);
        params.add("data", imageStr2);
        params.add("O_TypeID", "4");
        params.add("keys", Contast.KEYS);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
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
                        Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(WorkActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_title.setText("服务已经完成");
                                btn_fuwuwancheng.setBackgroundResource(R.drawable.btn_hui_bg);
                                btn_fuwuwancheng.setTextColor(Color.GRAY);
//                                btn_jixugongzuo.setBackgroundResource(R.drawable.btn_blue_bg);
//                                btn_jixugongzuo.setTextColor(Color.parseColor("#ff33b5e5"));
//                                btn_xiuxiyihui.setBackgroundResource(R.drawable.btn_blue_bg);
//                                btn_xiuxiyihui.setTextColor(Color.parseColor("#ff33b5e5"));
//                                btn_jixugongzuo.setClickable(true);
//                                btn_xiuxiyihui.setClickable(true);
                                iv_xichehou.setClickable(false);
                                btn_fuwuwancheng.setClickable(false);
                                btn_woyidaoda.setClickable(false);
                                btn_chufa.setClickable(false);
                                Intent intent = new Intent(WorkActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });

                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

//        FormBody.Builder params2 = new FormBody.Builder();
//        String imageStr2 = ImageUtils.getBase64String(finalFile);
//        params2.add("data", imageStr2);
//        params2.add("O_ID", dingdan1.getO_ID());
//        params2.add("W_Tel", Contast.worker.getW_Tel());
//        params2.add("W_IMEI", Contast.worker.getW_IMEI());
//        params2.add("W_WSID", "8");
//        params2.add("keys", Contast.KEYS);
//        OkHttpClient client2 = new OkHttpClient();
//        Request request2 = new Request.Builder()
//                .url(url)
//                .post(params2.build())
////                .header("Content-Type", "application/json")
//                .build();
//        okhttp3.Call call2 = client2.newCall(request2);
//        call2.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                pd2.dismiss();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(WorkActivity.this, "网络连接异样，请稍后再试", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                pd2.dismiss();
//                String string = response.body().string();
//                Log.i(TAG, "onResponse: " + string);
//                if (response.code() != HttpURLConnection.HTTP_OK) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.i("zzzzz", "zzzzzz" + "11111111111111111111111111");
//                            Toast.makeText(WorkActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                } else {
//                    if (!TextUtils.isEmpty(string)) {
//                        if (string.contains("ErrorStr")) {
//                            final Error error = JSON.parseObject(string, Error.class);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(WorkActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }else {
//                            dingdan = JSON.parseObject(string, Dingdan.class);
//                            Log.i("dingdan", dingdan.toString());
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
////                                    SharedPreferences wc = getSharedPreferences("Work", MODE_PRIVATE);
////                                    SharedPreferences.Editor wancheng = wc.edit();
////                                    wancheng.putLong("time", System.currentTimeMillis());
////                                    wancheng.putInt("W_WSID", 8);
////                                    wancheng.commit();
//                                    tv_title.setText("服务已经完成");
//                                    btn_fuwuwancheng.setBackgroundResource(R.drawable.btn_hui_bg);
//                                    btn_fuwuwancheng.setTextColor(Color.GRAY);
//                                    btn_jixugongzuo.setBackgroundResource(R.drawable.btn_blue_bg);
//                                    btn_jixugongzuo.setTextColor(Color.parseColor("#ff33b5e5"));
//                                    btn_xiuxiyihui.setBackgroundResource(R.drawable.btn_blue_bg);
//                                    btn_xiuxiyihui.setTextColor(Color.parseColor("#ff33b5e5"));
//                                    btn_jixugongzuo.setClickable(true);
//                                    btn_xiuxiyihui.setClickable(true);
//                                    iv_xichehou.setClickable(false);
//                                    btn_fuwuwancheng.setClickable(false);
//                                    btn_woyidaoda.setClickable(false);
//                                }
//                            });
//                        }
//                    } else {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    beginCrop(imageUri);
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKatKit(data);
                    } else {
                        handleImageBeforKatKit(data);
                    }
                }
                break;

            case Crop.REQUEST_PICK:
                if (resultCode == RESULT_OK) {
                    beginCrop(data.getData());
                }
                break;
            case Crop.REQUEST_CROP:
                handleCrop(resultCode, data);
        }
    }

    // 将裁剪回来的数据进行处理
    private void handleCrop(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(data);
            try {
                if (from == 1) {
                    iv_xicheqian.setImageBitmap(null);
                    finalFile = new File(ImageUtils.saveBitmap(WorkActivity.this, uri.getPath()));
                    Uri image = Uri.fromFile(finalFile);
                    Picasso.with(this).load(image)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .error(R.drawable.paizhao)
                            .into(iv_xicheqian);
//                    btn_chufa.setBackgroundResource(R.drawable.btn_hui_bg);
//                    btn_chufa.setTextColor(Color.GRAY);
//                    btn_kaishifuwu.setBackgroundResource(R.drawable.btn_hui_bg);
//                    btn_fuwuwancheng.setTextColor(Color.GRAY);
//                    tv_title.setText("已到达");
                    Log.i("image", "finalFile=" + finalFile.getAbsolutePath());
                } else if (from == 2) {
                    iv_xichehou.setImageBitmap(null);
                    finalFile = new File(ImageUtils.saveBitmap(WorkActivity.this, uri.getPath()));
                    Uri image = Uri.fromFile(finalFile);
                    Picasso.with(this).load(image)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .error(R.drawable.paizhao)
                            .into(iv_xichehou);
//                    btn_kaishifuwu.setBackgroundResource(R.drawable.btn_hui_bg);
//                    btn_fuwuwancheng.setTextColor(Color.GRAY);
//                    tv_title.setText("洗车中");
                    Log.i("image", "finalFile=" + finalFile.getAbsolutePath());
                }
//                else if (from == 3) {
//                    iv_zhengmian.setImageBitmap(null);
//                    zhengmianFile = new File(ImageUtils.saveBitmap(WorkActivity.this, uri.getPath()));
//                    Uri image = Uri.fromFile(zhengmianFile);
//                    Picasso.with(this).load(image)
//                            .memoryPolicy(MemoryPolicy.NO_CACHE)
//                            .networkPolicy(NetworkPolicy.NO_CACHE)
//                            .error(R.drawable.addcar)
//                            .into(iv_zhengmian);
//                    Log.i("image", "zhengmianFile=" + zhengmianFile.getAbsolutePath());
//                }else if (from == 4) {
//                    iv_fanmian.setImageBitmap(null);
//                    fanmianFile = new File(ImageUtils.saveBitmap(WorkActivity.this, uri.getPath()));
//                    Uri image = Uri.fromFile(fanmianFile);
//                    Picasso.with(this).load(image)
//                            .memoryPolicy(MemoryPolicy.NO_CACHE)
//                            .networkPolicy(NetworkPolicy.NO_CACHE)
//                            .error(R.drawable.addcar)
//                            .into(iv_fanmian);
//                    Log.i("image", "fanmianFile=" + fanmianFile.getAbsolutePath());
//                }else if (from == 5) {
//                    iv_cemian1.setImageBitmap(null);
//                    cemian1File = new File(ImageUtils.saveBitmap(WorkActivity.this, uri.getPath()));
//                    Uri image = Uri.fromFile(cemian1File);
//                    Picasso.with(this).load(image)
//                            .memoryPolicy(MemoryPolicy.NO_CACHE)
//                            .networkPolicy(NetworkPolicy.NO_CACHE)
//                            .error(R.drawable.addcar)
//                            .into(iv_cemian1);
//                    Log.i("image", "cemian1File=" + cemian1File.getAbsolutePath());
//                }else if (from == 6) {
//                    iv_cemian2.setImageBitmap(null);
//                    cemian2File = new File(ImageUtils.saveBitmap(WorkActivity.this, uri.getPath()));
//                    Uri image = Uri.fromFile(cemian2File);
//                    Picasso.with(this).load(image)
//                            .memoryPolicy(MemoryPolicy.NO_CACHE)
//                            .networkPolicy(NetworkPolicy.NO_CACHE)
//                            .error(R.drawable.addcar)
//                            .into(iv_cemian2);
//                    Log.i("image", "cemian2File=" + cemian2File.getAbsolutePath());
//                }else if (from == 7) {
//                    iv_qita1.setImageBitmap(null);
//                    qita1File = new File(ImageUtils.saveBitmap(WorkActivity.this, uri.getPath()));
//                    Uri image = Uri.fromFile(qita1File);
//                    Picasso.with(this).load(image)
//                            .memoryPolicy(MemoryPolicy.NO_CACHE)
//                            .networkPolicy(NetworkPolicy.NO_CACHE)
//                            .error(R.drawable.addcar)
//                            .into(iv_qita1);
//                    Log.i("image", "qita1File=" + qita1File.getAbsolutePath());
//                }else if (from == 8) {
//                    iv_qita2.setImageBitmap(null);
//                    qita2File = new File(ImageUtils.saveBitmap(WorkActivity.this, uri.getPath()));
//                    Uri image = Uri.fromFile(qita2File);
//                    Picasso.with(this).load(image)
//                            .memoryPolicy(MemoryPolicy.NO_CACHE)
//                            .networkPolicy(NetworkPolicy.NO_CACHE)
//                            .error(R.drawable.addcar)
//                            .into(iv_qita2);
//                    Log.i("image", "qita2File=" + qita2File.getAbsolutePath());
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(WorkActivity.this, Crop.getError(data).getMessage(),
                    Toast.LENGTH_SHORT).show();

        }
    }

    // 开始裁剪
    private void beginCrop(Uri uri) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped.jpg"));
        // start() 方法根据其的需求选择不同的重载方法
        Crop.of(uri, destination).withMaxSize(200, 150).start(WorkActivity.this);
    }


    private void handleImageBeforKatKit(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        disPlayImage(imagePath);
    }

    @TargetApi(19)
    private void handleImageOnKatKit(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(WorkActivity.this, uri)) {
            //  如果是Document类型的uri，则通过Document  Id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse(
                        "content://downloads/public/_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，则直接获取图片路径即可
            imagePath = uri.getPath();
        }
        disPlayImage(imagePath);
    }

    private void disPlayImage(String imagePath) {
        if (imagePath != null) {
            Uri uri = Uri.fromFile(new File(imagePath));
            beginCrop(uri);
        } else {
            Toast.makeText(WorkActivity.this, "图片选取失败", Toast.LENGTH_SHORT).show();
        }
    }


    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取图片的真是路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void setCarMarker(LatLng point) {
        Log.v("pcw", "setMarker : lat : " + lat + " lon : " + lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.cheliangweizhi);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    /**
     * 添加marker
     */
    private void setMarker(LatLng point) {
        Log.v("pcw", "setMarker : lat : " + lat + " lon : " + lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.worker);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
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


    /**
     * 实现定位监听 位置一旦有所改变就会调用这个方法
     * 可以在这个方法里面获取到定位之后获取到的一系列数据
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
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

            double distance = DistanceUtil.getDistance(new LatLng(lat, lon), new LatLng(carLat, carLon));
            zoom = getZoom(distance);

            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (isFirstLocation) {
                Log.i("pcw", "lat : " + lat + " lon : " + lon);
                Log.i("BaiduLocationApiDem", sb.toString());
                Log.i("distance", "distance=" + distance);
                Log.i("zoom", "zoom=" + zoom);
                isFirstLocation = false;
                setMarker(new LatLng(lat, lon));
                setUserMapCenter();
            }
//            Log.i("pcw", "lat : " + lat + " lon : " + lon);
//            Log.i("BaiduLocationApiDem", sb.toString());
//            Log.i("distance", "distance=" + distance);
//            Log.i("zoom", "zoom=" + zoom);
//            new AsyncTask<Void, Void, String>() {
//                @Override
//                protected String doInBackground(Void... params) {
//                    return location.getLocationDescribe();
//                }
//
//                @Override
//                protected void onPostExecute(String s) {
//                    dangqianweizhi = s;
//                    cheliangweizhi.setText(dangqianweizhi);
//                }
//            }.execute();


        }


        /**
         * 设置中心点
         */
        private void setUserMapCenter() {
            Log.v("pcw", "setUserMapCenter : lat : " + lat + " lon : " + lon);
//            LatLng cenpt = new LatLng(lat, lon);
//            LatLng carcenpt = new LatLng(carLat, carLon);
            LatLng cenpt = new LatLng((lat + carLat) / 2, (lon + carLon) / 2);
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(zoom)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(13).build()));

        }

    }


    /**
     * 计算缩放级别
     */
    private int getZoom(double distance) {

        int[] arr = {20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000,
                25000, 50000, 100000, 200000, 500000, 1000000, 2000000};
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] - distance > 0) {
                return 19 - i + 3;
            }
        }
        return 16;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loc_start = new Location(lat, lon);
        setCarMarker(new LatLng(carLat, carLon));

    }

    private void setViews() {
        int type = dingdan.getO_TypeID();
        if (type == 1 || type == 2) {
            btn_chufa.setBackgroundResource(R.drawable.btn_blue_bg);
            btn_chufa.setTextColor(Color.parseColor("#ff33b5e5"));
            btn_chufa.setClickable(true);
            tv_title.setText("已接单");
        } else if (type == 9) {
            btn_woyidaoda.setBackgroundResource(R.drawable.btn_blue_bg);
            btn_woyidaoda.setTextColor(Color.parseColor("#ff33b5e5"));
            btn_woyidaoda.setClickable(true);
            tv_title.setText("赶往途中");
        } else if (type == 8) {
            btn_kaishifuwu.setBackgroundResource(R.drawable.btn_blue_bg);
            btn_kaishifuwu.setTextColor(Color.parseColor("#ff33b5e5"));
            btn_kaishifuwu.setClickable(true);
            iv_xicheqian.setClickable(true);
            tv_title.setText("我已到达");
        } else if (type == 3) {
            btn_fuwuwancheng.setBackgroundResource(R.drawable.btn_blue_bg);
            btn_fuwuwancheng.setTextColor(Color.parseColor("#ff33b5e5"));
            btn_fuwuwancheng.setClickable(true);
            iv_xichehou.setClickable(true);
            tv_title.setText("洗车中");
            if (TextUtils.isEmpty(dingdan.getO_BeforePic())) {
                Picasso.with(WorkActivity.this).load(R.drawable.paizhao).into(iv_xicheqian);
            } else {
                String url = Contast.Domain + dingdan.getO_BeforePic();
                Picasso.with(WorkActivity.this).load(url).into(iv_xicheqian);
            }
        }
//        else if(type==9){
////            btn_jianchawancheng.setBackgroundResource(R.drawable.btn_blue_bg);
////            btn_jianchawancheng.setTextColor(Color.parseColor("#ff33b5e5"));
////            btn_jianchawancheng.setClickable(true);
////            iv_zhengmian.setClickable(true);
////            iv_fanmian.setClickable(true);
////            iv_cemian1.setClickable(true);
////            iv_cemian2.setClickable(true);
////            iv_qita1.setClickable(true);
////            iv_qita2.setClickable(true);
//        }
        if (dingdan.getO_IsPhone() == 1) {
            tv_beizhu.setVisibility(View.VISIBLE);
            tv_beizhu.setText("洗完车记得给车主打电话");
        } else if (dingdan.getO_IsPhone() == 1) {
            tv_beizhu.setVisibility(View.GONE);
        }
        tv_dingdanhao.setText(dingdan.getO_ID());
        tv_name.setText(dingdan.getO_CarName());
        tv_phone.setText(dingdan.getO_CarTel());
        tv_chepaihao.setText(dingdan.getO_PlateNumber());
        tv_dizhi.setText(dingdan.getO_Adress());
        tv_jutidizhi.setText(dingdan.getO_WriteAdress());
    }

    /**
     * 捕捉back
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (pv_max.getVisibility() == View.VISIBLE) {
                pv_max.setVisibility(View.GONE);
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void chufa() {
        FormBody.Builder params = new FormBody.Builder();
        params.add("O_ID", dingdan.getO_ID());
        params.add("W_Tel", Contast.worker.getW_Tel());
        params.add("W_IMEI", Contast.worker.getW_IMEI());
        params.add("O_TypeID", "9");
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
                        Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(WorkActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_title.setText("赶往途中");
                                btn_chufa.setBackgroundResource(R.drawable.btn_hui_bg);
                                btn_chufa.setTextColor(Color.GRAY);
                                btn_woyidaoda.setBackgroundResource(R.drawable.btn_blue_bg);
                                btn_woyidaoda.setTextColor(Color.parseColor("#ff33b5e5"));
                                iv_xicheqian.setClickable(true);
                                btn_kaishifuwu.setClickable(true);
                                btn_woyidaoda.setClickable(true);
                                btn_chufa.setClickable(false);
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WorkActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    public long getDateFromStr(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Long temp = 0L;
        try {
            Date date = (Date) df.parse(dateStr);
            temp = date.getTime();
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return temp;
        }
    }
}