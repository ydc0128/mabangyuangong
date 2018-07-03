package com.example.administrator.ydxcfwpt.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.PayMent;
import com.example.administrator.ydxcfwpt.Bean.Worker;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;

import java.io.IOException;
import java.net.HttpURLConnection;

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
//提现
public class TiXianActivity extends BaseActivity {
    private String url = Contast.Domain + "api/WorkerGetMoney.ashx?";
    @BindView(R.id.iv_tixian_back)
    ImageView ivTixianBack;
    @BindView(R.id.tv_mydingdan_title)
    TextView tvMydingdanTitle;
    @BindView(R.id.iv_tixian)
    ImageView ivTixian;
    @BindView(R.id.ed_tixian)
    EditText edTixian;
    @BindView(R.id.tv_tixian_yue)
    TextView tvTixianjiner;
//    @BindView(R.id.tv_quanbutixian)
//    TextView tvQuanbutixian;
    @BindView(R.id.tixianzhanghao)
    TextView tixianzhanghao;
    @BindView(R.id.bt_tixian)
    Button btTixian;
    String yue;
    String kahao = Contast.worker.getW_BankCard();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixian);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvTixianjiner.setText("" + Contast.worker.getW_Money());
        String weihao = kahao.substring(kahao.length()-4,kahao.length());
        tixianzhanghao.setText("尾号"+weihao);
        edTixian.setHint(""+Contast.worker.getW_Money());
//      tixianzhanghao.setText(Contast.worker.getW_BankName()+"("+weihao+")");
    }

    @OnClick({R.id.iv_tixian_back, R.id.iv_tixian,  R.id.tixianzhanghao, R.id.bt_tixian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_tixian_back:
                finish();
                break;
            case R.id.iv_tixian:
                Intent intent = new Intent(TiXianActivity.this, TiXianMingxi.class);
                startActivity(intent);
                break;
//          case R.id.tv_quanbutixian:
//                edTixian.setText("" + Contast.worker.getW_Money());
//                break;
            case R.id.tixianzhanghao:
                break;
            case R.id.ed_tixian:
                edTixian.setHint("");
                break;
            case R.id.bt_tixian:
                yue = edTixian.getText().toString();
                if (kahao==null&&Contast.worker.getW_BankName()==null){
                    Toast.makeText(TiXianActivity.this, "请先完善资料", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Contast.worker.getW_Money()<300){
                    Toast.makeText(TiXianActivity.this, "您尚未到达最低提现金额300元，需加倍努力", Toast.LENGTH_SHORT).show();
                    edTixian.setText("");
                    return;
                }
                if(TextUtils.isEmpty(yue)){
                    Toast.makeText(TiXianActivity.this, "提现金额不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                double d = Double.parseDouble(yue);
                if(d>Contast.worker.getW_Money()){
                    Toast.makeText(TiXianActivity.this, "提现金额超出余额，请重新输入", Toast.LENGTH_SHORT).show();
                    edTixian.setText("");
                    return;
                }
                tixian(yue);
                break;
        }
    }

    private void tixian(String str) {
        final ProgressDialog pd = new ProgressDialog(TiXianActivity.this);
        pd.setMessage("拼命加载中...");
        pd.show();
        FormBody.Builder params = new FormBody.Builder();
        params.add("keys", Contast.KEYS);
        params.add("W_Tel", Contast.worker.getW_Tel());
        params.add("W_IMEI", Contast.worker.getW_IMEI());
        params.add("W_Money", str);
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
                        Toast.makeText(TiXianActivity.this, "网络连接异样，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                pd.dismiss();
                final String string = response.body().string();
                if(response.code() != HttpURLConnection.HTTP_OK){
                    Toast.makeText(TiXianActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                }else {
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            final Error error = JSON.parseObject(string, Error.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TiXianActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Contast.worker = JSON.parseObject(string,Worker.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(TiXianActivity.this, "提现申请成功，正在审核...", Toast.LENGTH_SHORT).show();
                                    Intent intent1=new Intent(TiXianActivity.this,TiXianXiangQingActivity.class);
                                    intent1.putExtra("shijian",System.currentTimeMillis());
                                    if (!TextUtils.isEmpty(yue))
                                    intent1.putExtra("jine",yue);
                                    startActivity(intent1);
                                    finish();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TiXianActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        }
}
