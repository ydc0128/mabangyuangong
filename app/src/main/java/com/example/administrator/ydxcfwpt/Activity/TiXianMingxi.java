package com.example.administrator.ydxcfwpt.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Adapter.TiXianAdapter;
import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.PayMent;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/22.
 */
//提现明细
public class TiXianMingxi extends BaseActivity {

    @BindView(R.id.iv_tixianmingxi)
    ImageView ivTixianmingxi;
    @BindView(R.id.rv_tixianmingxi)
    ListView rvTixianmingxi;
    @BindView(R.id.rl_tixian_null)
    RelativeLayout rlTixianNull;
    private List<PayMent> TiXianList;
    private TiXianAdapter adapter;
    private static final String TAG = "TiXianMingXi";
    private String url = Contast.Domain + "api/WorkerGetMoneyList.ashx?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixianmingxi);
        ButterKnife.bind(this);
        initListView();
    }

    private void initListView() {
        rvTixianmingxi = (ListView) findViewById(R.id.rv_tixianmingxi);
        TiXianList = new ArrayList<>();
        adapter = new TiXianAdapter(TiXianMingxi.this, TiXianList);
        rvTixianmingxi.setAdapter(adapter);
    }

    @OnClick({R.id.iv_tixianmingxi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_tixianmingxi:
                finish();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tixian();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */

    public void backgroundAlpha(float bgAlpha)

    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);

    }

    private void tixian() {
        final ProgressDialog pd = new ProgressDialog(TiXianMingxi.this);
        pd.setMessage("拼命加载中...");
        pd.show();
        FormBody.Builder params = new FormBody.Builder();
        params.add("keys", Contast.KEYS);
        params.add("W_Tel", Contast.worker.getW_Tel());
        params.add("W_IMEI", Contast.worker.getW_IMEI());
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
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
                        Toast.makeText(TiXianMingxi.this, "网络连接异样，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                pd.dismiss();
                final String string = response.body().string();
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TiXianMingxi.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                            Log.i("TAG", string);
                        }
                    });
                } else {
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            final Error error = JSON.parseObject(string, Error.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TiXianMingxi.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            TiXianList = JSON.parseArray(string, PayMent.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i(TAG, "run: TiXianList =" + TiXianList);
                                    adapter.setData(TiXianList);
                                    if (TiXianList.size()>0){
                                        rlTixianNull.setVisibility(View.GONE);
                                    }else {
                                        rlTixianNull.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TiXianMingxi.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

}
