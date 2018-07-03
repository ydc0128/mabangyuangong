package com.example.administrator.ydxcfwpt.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Adapter.YingyeeAdapter;
import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.TuiKuan;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.View.MultiListView;
import com.example.administrator.ydxcfwpt.View.MultiScrollView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.administrator.ydxcfwpt.R.id.btn_mymoney_tixian;

public class MyMoneyActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MyMoneyActivity";
    //    private ScrollView scrollView;
    private String url = Contast.Domain + "api/WorkerAllOrder.ashx?";
    private String url_tuikuan = Contast.Domain + "api/WorkerReturnMoney.ashx";
    private ImageView iv_back;
    private TextView tv_dingdanjiangjin;
    private Button btn_tixian;

    //  private RadioButton rb_zuotian;
    private RadioButton rb_yixingqi;
    private RadioButton rb_yigeyue;
    //  private View line_zuotian;
    private View line_yixingqi;
    private View line_yigeyue;
    private RelativeLayout meiyoushuju;
    private ListView listView, tuikuanlistview;
    private List<Dingdan> dingdanList, xiaofeiList;
    private List<TuiKuan> tuikuanList;
    private TKAdapter tkAdapter;
    private YingyeeAdapter adapter;
    private TextView tv_heji;
    private TextView tv_heji_shuliang,tv_heji_tuikuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_money);
        initViews();
        refelash();
        initListView();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_mymoney_back);
        tv_dingdanjiangjin = (TextView) findViewById(R.id.tv_mymoney_dingdanjiangjin);
        tv_heji = (TextView) findViewById(R.id.tv_mymoney_heji);
        tv_heji_tuikuan=(TextView)findViewById(R.id.tv_hejituikuan);
//      scrollView=(ScrollView) findViewById(R.id.sv_huadong);
//      rb_zuotian = (RadioButton) findViewById(R.id.rb_mymoney_zuotian);
        rb_yixingqi = (RadioButton) findViewById(R.id.rb_mymoney_yixingqi);
        rb_yigeyue = (RadioButton) findViewById(R.id.rb_mymoney_yigeyue);
        btn_tixian = (Button) findViewById(btn_mymoney_tixian);
        tv_heji_shuliang = (TextView) findViewById(R.id.tv_hejishuliang);
//      line_zuotian = findViewById(R.id.line_mymoney_zuotian);
        line_yixingqi = findViewById(R.id.line_mymoney_yixingqi);
        line_yigeyue = findViewById(R.id.line_mymoney_yigeyue);
        meiyoushuju = (RelativeLayout) findViewById(R.id.rl_myqianbao_null);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_tixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Contast.worker.getW_Type() == 2) {
                    String kahao = Contast.worker.getW_BankCard();
                    if (kahao != null) {
                        Intent intent = new Intent(MyMoneyActivity.this, TiXianActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MyMoneyActivity.this, "请完善资料", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyMoneyActivity.this, "公司直营不能提现", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rb_yixingqi.setOnCheckedChangeListener(this);
        rb_yigeyue.setOnCheckedChangeListener(this);

    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.lv_mymoney_listview);
        dingdanList = new ArrayList<>();
        adapter = new YingyeeAdapter(this, dingdanList);
        listView.setAdapter(adapter);
        tuikuanlistview = (ListView) findViewById(R.id.lv_mymoney_tuikuan_listview);
        tuikuanList = new ArrayList<>();
        tkAdapter = new TKAdapter(this, dingdanList);
        tuikuanlistview.setAdapter(tkAdapter);
        setListViewHeightBasedOnChildren(listView);
        setListViewHeightBasedOnChildren(tuikuanlistview);

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    @Override
    protected void onResume() {
        super.onResume();
        refelash();
        tv_dingdanjiangjin.setText("" + Contast.worker.getW_Money());
    }

    private void refelash() {
        final ProgressDialog pd = new ProgressDialog(MyMoneyActivity.this);
        pd.setMessage("拼命加载中...");
        pd.show();
        FormBody.Builder params = new FormBody.Builder();
        params.add("keys", Contast.KEYS);
        params.add("O_WorkerTel", Contast.worker.getW_Tel());
        params.add("W_IMEI", Contast.worker.getW_IMEI());
//        if (rb_zuotian.isChecked()) {
//            params.add("month", "day");
//        } else
        if (rb_yixingqi.isChecked()) {
            params.add("month", "week");
        } else if (rb_yigeyue.isChecked()) {
            params.add("month", "month");
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyMoneyActivity.this, "网络连接异样，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                pd.dismiss();
                final String string = response.body().string();
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    Toast.makeText(MyMoneyActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                } else {
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            final Error error = JSON.parseObject(string, Error.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MyMoneyActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dingdanList = JSON.parseArray(string, Dingdan.class);
                                    xiaofeiList = new ArrayList<>();
                                    for (Dingdan item : dingdanList) {
                                        int type = item.getO_TypeID();
                                        if (type == 4) {
                                            xiaofeiList.add(item);
//                                            adapter.leixing=1;
                                        }
                                    }
                                    adapter.setData(xiaofeiList);
                                    tv_heji_shuliang.setText("共完成"+xiaofeiList.size()+"单");
//                                    adapter.notifyDataSetChanged();
                                    if (dingdanList.size() > 0) {
                                        meiyoushuju.setVisibility(View.GONE);
                                    } else {
                                        meiyoushuju.setVisibility(View.VISIBLE);
                                    }
                                    Log.e("1111", dingdanList.size() + "");
                                    int heji = 0;
                                    for (Dingdan dingdan : xiaofeiList) {
                                        if (dingdan.getO_ISCancel() == 0) {
                                            int p = dingdan.getO_Price();
                                            heji += p;
                                        }
                                    }
                                    tv_heji.setText("" + heji);
                                }
                            });

                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyMoneyActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        tuikuan();
    }

    //
    private void tuikuan() {

        final ProgressDialog pd = new ProgressDialog(MyMoneyActivity.this);
        pd.setMessage("拼命加载中...");
        pd.show();
        FormBody.Builder params = new FormBody.Builder();
        params.add("keys", Contast.KEYS);
        params.add("W_Tel", Contast.worker.getW_Tel());
        params.add("W_IMEI", Contast.worker.getW_IMEI());
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url_tuikuan)
                .post(params.build())
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyMoneyActivity.this, "网络连接异样，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                pd.dismiss();
                final String string = response.body().string();
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    Toast.makeText(MyMoneyActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                } else {
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            final Error error = JSON.parseObject(string, Error.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MyMoneyActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tuikuanList = JSON.parseArray(string, TuiKuan.class);
                                    tkAdapter.setData(tuikuanList);
                                    tv_heji_tuikuan.setText("退款"+tuikuanList.size()+"单");

                                }
                            });

                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyMoneyActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
//            case R.id.btn_mymoney_tixian:
//                double money =  Contast.worker.getW_Money();
//                if(money >= 500){
//                    Toast.makeText(MyMoneyActivity.this,"提现申请已经提交，等待审核...",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(MyMoneyActivity.this,"您尚未到达最低提现金额500元，需加倍努力",Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.rb_mymoney_zuotian:
//                if (isChecked) {
//                    rb_yixingqi.setChecked(false);
//                    rb_yigeyue.setChecked(false);
//                    line_zuotian.setVisibility(View.VISIBLE);
//                    line_yixingqi.setVisibility(View.INVISIBLE);
//                    line_yigeyue.setVisibility(View.INVISIBLE);
//                    refelash();
//                } else {
//                    line_zuotian.setVisibility(View.INVISIBLE);
//                }
//                break;
            case R.id.rb_mymoney_yixingqi:
                if (isChecked) {
//                    rb_zuotian.setChecked(false);
                    rb_yigeyue.setChecked(false);
//                    line_zuotian.setVisibility(View.INVISIBLE);
                    line_yixingqi.setVisibility(View.VISIBLE);
                    line_yigeyue.setVisibility(View.INVISIBLE);
                    refelash();
                } else {
                    line_yixingqi.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.rb_mymoney_yigeyue:
                if (isChecked) {
                    rb_yixingqi.setChecked(false);
//                    rb_zuotian.setChecked(false);
//                    line_zuotian.setVisibility(View.INVISIBLE);
                    line_yixingqi.setVisibility(View.INVISIBLE);
                    line_yigeyue.setVisibility(View.VISIBLE);
                    refelash();
                } else {
                    line_yigeyue.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

}
