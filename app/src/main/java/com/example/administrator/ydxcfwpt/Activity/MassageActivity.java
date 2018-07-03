package com.example.administrator.ydxcfwpt.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Adapter.MSGAdapter;
import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.Utils.DataString;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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
 * Created by Administrator on 2018/1/11.
 */

public class MassageActivity extends BaseActivity {
    @BindView(R.id.iv_msg_back)
    ImageView ivMsgBack;
    @BindView(R.id.tv_msg_title)
    TextView tvMsgTitle;
    @BindView(R.id.btn_msg_more)
    Button btnMsgMore;
    @BindView(R.id.ptrlv_masg_listview)
    PullToRefreshListView ptrlvMasgListview;
    @BindView(R.id.xianhao)
    TextView xianhao;
    @BindView(R.id.riqi)
    TextView riqi;
    private List<Dingdan> dingdanList;
    private MSGAdapter adapter;
    private static final String TAG = "XIAOXI";
    private String url = Contast.Domain + "api/WorkerGetMoneyList.ashx?";
    private DataString dataString;
    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;
    private String shijian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        initListView();
        xianxing();
    }

    private void initListView() {
        dingdanList = new ArrayList<>();
        adapter = new MSGAdapter(MassageActivity.this, dingdanList);
        ptrlvMasgListview.setAdapter(adapter);
    }

    @OnClick({R.id.iv_msg_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_msg_back:
                finish();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        tixian();
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
        final ProgressDialog pd = new ProgressDialog(MassageActivity.this);
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
                        Toast.makeText(MassageActivity.this, "网络连接异样，请稍后再试", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MassageActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(MassageActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            dingdanList = JSON.parseArray(string, Dingdan.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i(TAG, "run: TiXianList =" + dingdanList);
                                    adapter.setData(dingdanList);
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MassageActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private void xianxing() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        shijian = mYear + "年" + mMonth + "月" + mDay + "日";
        if ("1".equals(mWay)) {
            mWay = "天";
            Toast.makeText(MassageActivity.this, "今天是周末，不限行", Toast.LENGTH_SHORT).show();
            xianhao.setText( "今天是周末，不限行");

        } else if ("2".equals(mWay)) {
            mWay = "一";
            Toast.makeText(MassageActivity.this, "今天限行尾号：1和6", Toast.LENGTH_SHORT).show();
            xianhao.setText( "今天限行尾号：1和6");
            riqi.setText(shijian + "周" + mWay );
        } else if ("3".equals(mWay)) {
            mWay = "二";
            Toast.makeText(MassageActivity.this, "今天限行尾号：2和7", Toast.LENGTH_SHORT).show();
            xianhao.setText( "今天限行尾号：2和7");
            riqi.setText(shijian + "周" + mWay );
        } else if ("4".equals(mWay)) {
            mWay = "三";
            Toast.makeText(MassageActivity.this, "今天限行尾号：3和8", Toast.LENGTH_SHORT).show();
            riqi.setText(shijian + "周" + mWay );
            xianhao.setText("今天限行尾号：3和8");
        } else if ("5".equals(mWay)) {
            mWay = "四";
            Toast.makeText(MassageActivity.this, "今天限行尾号：4和9", Toast.LENGTH_SHORT).show();
            xianhao.setText( "今天限行尾号：4和9");
        } else if ("6".equals(mWay)) {
            mWay = "五";
            Toast.makeText(MassageActivity.this, "今天限行尾号：5和0", Toast.LENGTH_SHORT).show();
            riqi.setText(shijian + "周" + mWay );
            xianhao.setText( "今天限行尾号：5和0");
        } else if ("7".equals(mWay)) {
            mWay = "六";
            Toast.makeText(MassageActivity.this, "今天是周末，不限行", Toast.LENGTH_SHORT).show();
           riqi.setText(shijian + "周" + mWay );
            xianhao.setText( "今天是周末，不限行");
        }
    }
}
