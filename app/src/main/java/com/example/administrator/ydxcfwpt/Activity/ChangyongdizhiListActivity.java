package com.example.administrator.ydxcfwpt.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Adapter.ChongyongdizhiAdapter;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.WChangyongdizhi;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChangyongdizhiListActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ChangyongdizhiListActiv";

    private String url = Contast.Domain + "api/AdressOftenList.ashx";

    private ImageView iv_back;
    private LinearLayout ll_jia;
    private LinearLayout ll_gongsi;

    private TextView tv_jia;
    private TextView tv_jia_buchong;
    private TextView tv_gongsi;
    private TextView tv_gongsi_buchong;

    private ListView listView;
    private List<WChangyongdizhi> chongyongdizhiList;
    private ChongyongdizhiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changyongdizhi_list);
        initViews();
        initListView();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_changyongdizhi_list_back);
        ll_jia = (LinearLayout) findViewById(R.id.ll_changyongdizhi_list_jia);
        ll_gongsi = (LinearLayout) findViewById(R.id.ll_changyongdizhi_list_gongsi);
        tv_jia = (TextView) findViewById(R.id.tv_changyongdizhi_list_jia);
        tv_jia_buchong = (TextView) findViewById(R.id.tv_changyongdizhi_list_jia_buchong);
        tv_gongsi = (TextView) findViewById(R.id.tv_changyongdizhi_list_gongsi);
        tv_gongsi_buchong = (TextView) findViewById(R.id.tv_changyongdizhi_list_gongsi_buchong);


        iv_back.setOnClickListener(this);
        ll_jia.setOnClickListener(this);
        ll_gongsi.setOnClickListener(this);
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.lv_changyongdizhi_list_listview);
        chongyongdizhiList = new ArrayList<>();
        adapter = new ChongyongdizhiAdapter(ChangyongdizhiListActivity.this, chongyongdizhiList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WChangyongdizhi dizhi = (WChangyongdizhi) adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("dizhi", dizhi);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setViews();
        refelash();
    }

    private void setViews() {
        Log.i(TAG, "setViews: ");
        if (!TextUtils.isEmpty(Contast.worker.getW_Home())) {
            tv_jia.setText(Contast.worker.getW_Home());
        }
        if (!TextUtils.isEmpty(Contast.worker.getW_HomeWrite())) {
            tv_jia_buchong.setText(Contast.worker.getW_HomeWrite());
        }
        if (!TextUtils.isEmpty(Contast.worker.getW_Office())) {
            tv_gongsi.setText(Contast.worker.getW_Office());
        }
        if (!TextUtils.isEmpty(Contast.worker.getW_OfficeWrite())) {
            tv_gongsi_buchong.setText(Contast.worker.getW_OfficeWrite());
        }
    }


    private void refelash() {
        final ProgressDialog pd = new ProgressDialog(ChangyongdizhiListActivity.this);
        pd.setMessage("拼命加载中...");
        pd.show();
        FormBody.Builder params = new FormBody.Builder();
        params.add("AW_WTel", Contast.worker.getW_Tel()+"");
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
                pd.dismiss();
                //响应失败
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangyongdizhiListActivity.this, "网络连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                pd.dismiss();
                //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                final String string = response.body().string();
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    Toast.makeText(ChangyongdizhiListActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "onResponse: json=" + string);
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            final Error error = JSON.parseObject(string, Error.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ChangyongdizhiListActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chongyongdizhiList = JSON.parseArray(string, WChangyongdizhi.class);
                                    adapter.setData(chongyongdizhiList);
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangyongdizhiListActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_changyongdizhi_list_back:
                finish();
                break;
            case R.id.ll_changyongdizhi_list_jia:
                Intent intent = new Intent();
                WChangyongdizhi jia = new WChangyongdizhi();
                jia.setAW_WTel(Contast.worker.getW_Tel());
                jia.setAW_Adress(Contast.worker.getW_Home());
                jia.setAW_AdressWrite(Contast.worker.getW_HomeWrite());
                jia.setAW_Lat(Contast.worker.getW_HomeLat());
                jia.setAW_Lng(Contast.worker.getW_HomeLng());
                intent.putExtra("dizhi", jia);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.ll_changyongdizhi_list_gongsi:
                Intent intent1 = new Intent();
                WChangyongdizhi gongsi = new WChangyongdizhi();
                gongsi.setAW_WTel(Contast.worker.getW_Tel());
                gongsi.setAW_Adress(Contast.worker.getW_Office());
                gongsi.setAW_AdressWrite(Contast.worker.getW_OfficeWrite());
                gongsi.setAW_Lat(Contast.worker.getW_OfficeLat());
                gongsi.setAW_Lng(Contast.worker.getW_OfficeLng());
                intent1.putExtra("dizhi", gongsi);
                setResult(RESULT_OK, intent1);
                finish();
                break;
        }
    }
}
