package com.example.administrator.ydxcfwpt.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Adapter.DialogListViewAdapter;
import com.example.administrator.ydxcfwpt.Adapter.FuwuxiangmuAdapter;
import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.Fuwuxiangmu;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.Utils.ActivityUtils;
import com.example.administrator.ydxcfwpt.View.MyListView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


//订单详情
public class DingdanXiangqingActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "DingdanXiangqingActivit";
    private String url = Contast.Domain + "api/OrderDelete.ashx?";
    private String url_state = Contast.Domain + "api/WorkerState.ashx?";
    private ImageView iv_back;
//    private Button btn_quxiao;
    private TextView tv_dingdanhao;
    private TextView tv_shijian;
    private TextView tv_chepaihao;
    private TextView tv_lianxiren;
    private TextView tv_shoujihao;
    private TextView tv_dizhi;
    private TextView tv_hejijine;
    private TextView tv_jibie;

    private MyListView lv_fuwuxiangmu;
    private List<Fuwuxiangmu> fuwuxiangmuList;
    private FuwuxiangmuAdapter adapter;

    private String time,shijian,riqi;
    private Dingdan dingdan;
    private List<String> xichegongdianhua;
    private List<String> quxiaoyuanyin;
    private static String mDay;
    private Button btn_zailaiyidan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dingdan_xiangqing);
        Intent intent = getIntent();
        dingdan = (Dingdan) intent.getSerializableExtra("dingdan");
        initViews();
        initListView();
        setDatas();
    }


    private void initListView() {
        lv_fuwuxiangmu = (MyListView) findViewById(R.id.lv_dingdanxiangqing_fiwuxiangmu);
        fuwuxiangmuList = new ArrayList<>();
        String fuwu = dingdan.getO_WPart();
        if (fuwu.contains("|")) {
            String[] split = fuwu.split("\\|");
            for (int i = 0; i < split.length; i++) {
                Fuwuxiangmu fuwuxiangmu = new Fuwuxiangmu();
                String[] strings = split[i].split(",");
                int num = Integer.parseInt(strings[0]);
                String price = strings[1];
                switch (num) {
                    case 1:
                    case 7:
                        fuwuxiangmu.setName("车外清洗");
                        break;
                    case 2:
                    case 9:
                        fuwuxiangmu.setName("标准清洗");
                        break;
                    case 16:
                    case 17:
                        fuwuxiangmu.setName("轮毂除锈");
                        break;
                    case 4:
                    case 11:
                        fuwuxiangmu.setName("打蜡");
                        break;
                    case 5:
                    case 12:
                        fuwuxiangmu.setName("室内精洗");
                        break;
                    case 18:
                    case 19:
                        fuwuxiangmu.setName("引擎清洗");
                        break;
                }
                fuwuxiangmu.setPrice(price);
                fuwuxiangmuList.add(fuwuxiangmu);
            }
        } else {
            Fuwuxiangmu fuwuxiangmu = new Fuwuxiangmu();
            String[] split = fuwu.split(",");
            int num = Integer.parseInt(split[0]);
            String price = split[1];
            switch (num) {
                case 1:
                case 7:
                    fuwuxiangmu.setName("车外清洗");
                    break;
                case 2:
                case 9:
                    fuwuxiangmu.setName("标准清洗");
                    break;
                case 16:
                case 17:
                    fuwuxiangmu.setName("轮毂除锈");
                    break;
                case 4:
                case 11:
                    fuwuxiangmu.setName("打蜡");
                    break;
                case 5:
                case 12:
                    fuwuxiangmu.setName("室内精洗");
                    break;
                case 18:
                case 19:
                    fuwuxiangmu.setName("引擎清洗");
                    break;
            }
            fuwuxiangmu.setPrice(price);
            fuwuxiangmuList.add(fuwuxiangmu);
        }
        FuwuxiangmuAdapter adapter = new FuwuxiangmuAdapter(DingdanXiangqingActivity.this, fuwuxiangmuList);
        lv_fuwuxiangmu.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lv_fuwuxiangmu);
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        setDatas();
//    }
    private void setDatas() {
        xichegongdianhua = new ArrayList<>();
        xichegongdianhua.add(dingdan.getO_WorkerTel());
        tv_dingdanhao.setText(dingdan.getO_ID());
        tv_shijian.setText(dingdan.getO_Time());
        tv_chepaihao.setText(dingdan.getO_PlateNumber());
        tv_lianxiren.setText(dingdan.getO_CarName());
        tv_shoujihao.setText(dingdan.getO_UTel());
        tv_dizhi.setText(dingdan.getO_Adress());
        tv_hejijine.setText("" + dingdan.getO_Money());
        if (dingdan.getO_CTID() == 3) {
            tv_jibie.setText("小轿车");
        } else if (dingdan.getO_CTID() == 4) {
            tv_jibie.setText("SUV/MPV");
        }
    }
    private void initViews() {
        quxiaoyuanyin = new ArrayList<>();
        quxiaoyuanyin.add("行程改变");
        quxiaoyuanyin.add("我想重新下单");
        quxiaoyuanyin.add("车辆信息不符");
        quxiaoyuanyin.add("洗车工要求取消订单");
        quxiaoyuanyin.add("其他原因");
        iv_back = (ImageView) findViewById(R.id.iv_dingdanxiangqing_back);
//        btn_quxiao = (Button) findViewById(R.id.btn_dingdanxiangqing_more);
        tv_dingdanhao = (TextView) findViewById(R.id.tv_dingdanxiangqing_dingdanhao);
        tv_shijian = (TextView) findViewById(R.id.tv_dingdanxiangqing_xiadanshijian);
        tv_chepaihao = (TextView) findViewById(R.id.tv_dingdanxiangqing_chepaihao);
        tv_lianxiren = (TextView) findViewById(R.id.tv_dingdanxiangqing_lianxiren);
        tv_shoujihao = (TextView) findViewById(R.id.tv_dingdanxiangqing_lianxifangshi);
        tv_dizhi = (TextView) findViewById(R.id.tv_dingdanxiangqing_dizhi);
        tv_jibie = (TextView) findViewById(R.id.tv_dingdanxiangqing_jibie);
        tv_hejijine = (TextView) findViewById(R.id.tv_dingdanxiangqing_hejijine);
        btn_zailaiyidan = (Button) findViewById(R.id.btn_dingdanxiangqing_zailaiyidan);

        iv_back.setOnClickListener(this);
        btn_zailaiyidan.setOnClickListener(this);
        compareTwoTime(time,shijian);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dingdanxiangqing_back:
                finish();
                break;
            case R.id.btn_dingdanxiangqing_zailaiyidan:
                ActivityUtils.finishAll();
                Intent intent = new Intent(DingdanXiangqingActivity.this, WorkActivity.class);
                intent.putExtra("dingdan", dingdan);
                startActivity(intent);
                kaishifuwu(dingdan);
                break;
        }
    }



    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    public boolean compareTwoTime(String starTime, String endString) {
        boolean isDayu = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        starTime = sdf.format(new Date(now));
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String yuyueshijian=Contast.dingdan.getO_WashTime();
        String weihao = yuyueshijian.substring(0,2);
        String shijian1=yuyueshijian.substring(yuyueshijian.length()-5,yuyueshijian.length());
        if (weihao.equals("今天")){
            riqi=mDay;
        }else if (weihao.equals("明天")){
            riqi=mDay+1;
        }
        endString=riqi+shijian1;
//        if (shijian.equals(time)){
//            btn_zailaiyidan.setClickable(false);
//            btn_zailaiyidan.setBackgroundResource(R.drawable.btn_hui_bg);
//            btn_zailaiyidan.setTextColor(Color.GRAY);
//        }else if (shijian.equals(time+1000*30*60)){
//            btn_zailaiyidan.setClickable(true);
//            btn_zailaiyidan.setBackgroundResource(R.drawable.btn_blue_bg);
//            btn_zailaiyidan.setTextColor(Color.parseColor("#ff33b5e5"));
//        }
        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endString+1000*30*60);
            long diff = parse1.getTime() - parse.getTime();
            if (diff > 0) {
                btn_zailaiyidan.setClickable(false);
                btn_zailaiyidan.setBackgroundResource(R.drawable.btn_hui_bg);
                btn_zailaiyidan.setTextColor(Color.GRAY);
            } else if (diff < 0&&diff==0){
                btn_zailaiyidan.setClickable(true);
                btn_zailaiyidan.setBackgroundResource(R.drawable.btn_blue_bg);
                btn_zailaiyidan.setTextColor(Color.parseColor("#ff33b5e5"));
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isDayu;

    }

    private void kaishifuwu(final Dingdan dingdan) {
        FormBody.Builder params = new FormBody.Builder();
        params.add("O_ID", dingdan.getO_ID());
        params.add("W_Tel", Contast.worker.getW_Tel());
        params.add("W_IMEI", Contast.worker.getW_IMEI());
        params.add("W_WSID", "5");
        params.add("data", "");
        params.add("keys", Contast.KEYS);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url_state)
                .post(params.build())
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DingdanXiangqingActivity.this, "网络连接异样，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("qq", "qq" + string);
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    Toast.makeText(DingdanXiangqingActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                } else {
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            if (dingdan.getO_ISCancel() == 0) {
                                final Error error = JSON.parseObject(string, Error.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DingdanXiangqingActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(DingdanXiangqingActivity.this, "该订单已取消", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            final Dingdan dingdan1 = JSON.parseObject(string, Dingdan.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(DingdanXiangqingActivity.this, WorkActivity.class);
                                    intent.putExtra("dingdan", dingdan1);
                                    startActivity(intent);
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DingdanXiangqingActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }
        });
    }

}
