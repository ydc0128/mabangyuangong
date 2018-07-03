package com.example.administrator.ydxcfwpt.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.WChangyongdizhi;
import com.example.administrator.ydxcfwpt.Bean.Worker;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DituActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "DituActivity";

    private String url_jia = Contast.Domain + "api/AdressOftenHomeWorker.ashx";
    private String url_gongsi = Contast.Domain + "api/AdressOftenOfficeWorker.ashx";
    private String url_changyong = Contast.Domain + "api/AdressOftenUpdateWorker.ashx";
    private String url_dizhi3=Contast.Domain+"api/AdressOftenAddWorker.ashx";
    private MapView mMapView;
    private TextView cheliangweizhi;
    private TextView baocun;
    private EditText buchongweizhi;
    private ImageButton ib_shuaxin;
    private ImageView iv_back;
    private boolean isFirstLocation = true;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaiduMap;
    private String dangqianweizhi;
    private double lat;
    private double lon;
    private double finallat;
    private double finallon;
    private GeoCoder mSearch;
    private int from;
    private WChangyongdizhi dizhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        //使用百度地图的任何功能都需要先初始化这段代码  最好放在全局中进行初始化
        //百度地图+定位+marker比较简单 我就不放到全局去了
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_ditu);
        Intent intent = getIntent();
        from = intent.getIntExtra("from", -1);
        dizhi = (WChangyongdizhi) intent.getSerializableExtra("dizhi");
        //获取控件
        initViews();
        mBaiduMap = mMapView.getMap();
        //获取POI搜索对象
        mSearch = GeoCoder.newInstance();
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        //创建地理编码检索监听者
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                }
                //获取地理编码结果
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(DituActivity.this, "抱歉，未能找到结果",
                            Toast.LENGTH_LONG).show();
                }
                cheliangweizhi.setText(result.getSematicDescription());
//                Toast.makeText(MainActivity.this,result.getAddressDetail().district,Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this,result.getSematicDescription(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this,result.getBusinessCircle(),Toast.LENGTH_SHORT).show();
            }
        };
        //设置地理编码检索监听者
        mSearch.setOnGetGeoCodeResultListener(listener);

        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //配置定位参数
        initLocation();
        //开始定位
        mLocationClient.start();
        //监听地图中心点位置
        initListener();
    }

    private void initViews() {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bdmapView_ditu);
        //记录当前Maker标记位置的地理信息
        cheliangweizhi = (TextView) findViewById(R.id.tv_ditu_cheliangweizhi);
        baocun = (TextView) findViewById(R.id.tv_ditu_baocun);
        iv_back = (ImageView) findViewById(R.id.iv_ditu_back);
        //获取控件
        ib_shuaxin = (ImageButton) findViewById(R.id.ib_ditu_shuaxin);
        ib_shuaxin.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        baocun.setOnClickListener(this);
    }

    /**
     * 地图状态改变
     */
    private void initListener() {
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                updateMapState(status);
            }

            @Override
            public void onMapStatusChange(MapStatus status) {
            }
        });
    }

    private void updateMapState(MapStatus status) {
        LatLng mCenterLatLng = status.target;
        /**获取经纬度*/
        finallat = mCenterLatLng.latitude;
        finallon = mCenterLatLng.longitude;
        LatLng point = new LatLng(finallat, finallon);
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
    }


    /**
     * 添加marker
     */
    private void setMarker() {
        Log.v("pcw", "setMarker : lat : " + lat + " lon : " + lon);
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.bullet_blue);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    /**
     * 设置中心点
     */
    private void setUserMapCenter() {
        Log.v("pcw", "setUserMapCenter : lat : " + lat + " lon : " + lon);
        LatLng cenpt = new LatLng(lat, lon);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(13).build()));

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
//            lat = location.getLatitude();
//            lon = location.getLongitude();
            if (from == 1) {
                if (Contast.worker.getW_HomeLat() == 0.0) {
                    lat = location.getLatitude();
                } else {
                    lat = Contast.worker.getW_HomeLat();
                }
                if (Contast.worker.getW_HomeLng() == 0.0) {
                    lon = location.getLongitude();
                } else {
                    lon = Contast.worker.getW_HomeLng();
                }
            } else if (from == 2) {
                if (Contast.worker.getW_OfficeLat() == 0.0) {
                    lat = location.getLatitude();
                } else {
                    lat = Contast.worker.getW_OfficeLat();
                }
                if (Contast.worker.getW_OfficeLng() == 0.0) {
                    lon = location.getLongitude();
                } else {
                    lon = Contast.worker.getW_OfficeLng();
                }
            } else if (from == 3) {
                if (Contast.wChangyongdizhi.getAW_Lat() == 0.0) {
                    lat = location.getLatitude();
                } else {
                    lat = Contast.wChangyongdizhi.getAW_Lat();
                }
                if (Contast.wChangyongdizhi.getAW_Lng() == 0.0) {
                    lon = location.getLongitude();
                } else {
                    lon = Contast.wChangyongdizhi.getAW_Lng();
                }
//                lat = dizhi.getAW_Lat();
//                lon = dizhi.getAW_Lng();
            }
//            lat = location.getLatitude();
//            lon = location.getLongitude();

            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (isFirstLocation) {
                Log.v("pcw", "lat : " + lat + " lon : " + lon);
                Log.i("BaiduLocationApiDem", sb.toString());
                isFirstLocation = false;
                setMarker();
                setUserMapCenter();
                new AsyncTask<Void, Void, String[]>() {
                    @Override
                    protected String[] doInBackground(Void... params) {
                        if (from == 1) {
                            return new String[]{Contast.worker.getW_Home(), Contast.worker.getW_HomeWrite()};
                        } else if (from == 2) {
                            return new String[]{Contast.worker.getW_Office(), Contast.worker.getW_OfficeWrite()};
                        } else if (from == 3) {
                            return new String[]{Contast.wChangyongdizhi.getAW_Adress(), Contast.wChangyongdizhi.getAW_AdressWrite()};
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String[] s) {
                        if (s != null) {
                            cheliangweizhi.setText(s[0]);

                        }
                    }
                }.execute();
            }
        }


        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    /**
     * 必须要实现
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mSearch.destroy();
    }

    /**
     * 必须要实现
     */
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    /**
     * 必须要实现
     */
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_ditu_back:
                finish();
                break;
            case R.id.ib_ditu_shuaxin:
                setUserMapCenter();
                setMarker();
                cheliangweizhi.setText(dangqianweizhi);
                break;
            case R.id.tv_ditu_baocun:
                if (from == 1) {
                    final ProgressDialog pd = new ProgressDialog(DituActivity.this);
                    pd.setMessage("拼命加载中...");
                    pd.show();
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("W_Tel", Contast.worker.getW_Tel());
                    params.add("W_Home", cheliangweizhi.getText().toString());
                    params.add("W_HomeWrite","");
                    params.add("W_HomeLng", "" + finallon);
                    params.add("W_HomeLat", "" + finallat);
                    params.add("keys", Contast.KEYS);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url_jia)
                            .post(params.build())
                            .build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            pd.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DituActivity.this, "网络连接异常，请稍后再试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            pd.dismiss();
                            if (response.code() != HttpURLConnection.HTTP_OK) {
                                Toast.makeText(DituActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                            } else {
                                String string = response.body().string();
                                Log.i(TAG, "onResponse: json=" + string);
                                if (!TextUtils.isEmpty(string)) {
                                    if (string.contains("ErrorStr")) {
                                        final Error error = JSON.parseObject(string, Error.class);
                                        Log.i(TAG, "onResponse:" + error.getErrorStr());
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(DituActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Contast.worker = JSON.parseObject(string, Worker.class);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(DituActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DituActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    });
                } else if (from == 2) {
                    final ProgressDialog pd = new ProgressDialog(DituActivity.this);
                    pd.setMessage("拼命加载中...");
                    pd.show();
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("W_Tel", Contast.worker.getW_Tel()+"");
                    params.add("W_Office", cheliangweizhi.getText().toString());
                    params.add("W_OfficeWrite","");
                    params.add("W_OfficeLng", "" + finallon);
                    params.add("W_OfficeLat", "" + finallat);
                    params.add("keys", Contast.KEYS);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url_gongsi)
                            .post(params.build())
                            .build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            pd.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DituActivity.this, "网络连接异常，请稍后再试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            pd.dismiss();
                            String string = response.body().string();
                            if (response.code() != HttpURLConnection.HTTP_OK) {
                                Toast.makeText(DituActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i(TAG, "onResponse: json=" + string);
                                if (!TextUtils.isEmpty(string)) {
                                    if (string.contains("ErrorStr")) {
                                        final Error error = JSON.parseObject(string, Error.class);
                                        Log.i(TAG, "onResponse:" + error.getErrorStr());
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(DituActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Contast.worker = JSON.parseObject(string, Worker.class);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(DituActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DituActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    });
                } else if (from == 3) {
                    final ProgressDialog pd = new ProgressDialog(DituActivity.this);
                    pd.setMessage("拼命加载中...");
                    pd.show();
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("AW_WTel", Contast.worker.getW_Tel());
                    params.add("AW_Adress", cheliangweizhi.getText().toString());
                    params.add("AW_AdressWrite", "");
                    params.add("AW_Lng", "" + finallon);
                    params.add("AW_Lat", "" + finallat);
                    params.add("keys", Contast.KEYS);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url_dizhi3)
                            .post(params.build())
                            .build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            pd.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DituActivity.this, "网络连接异常，请稍后再试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            pd.dismiss();
                            String string = response.body().string();
                            if (response.code() != HttpURLConnection.HTTP_OK) {
                                Toast.makeText(DituActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i(TAG, "onResponse: json=" + string);
                                if (!TextUtils.isEmpty(string)) {
                                    if (string.contains("ErrorStr")) {
                                        final Error error = JSON.parseObject(string, Error.class);
                                        Log.i(TAG, "onResponse:" + error.getErrorStr());
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(DituActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else if (string.contains("OkStr")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(DituActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DituActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
                break;
        }
    }
}
