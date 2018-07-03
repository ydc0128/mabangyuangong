package com.example.administrator.ydxcfwpt.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//短信修改密码
public class DuanxinxiugaiActivity extends BaseActivity {

    private static final String TAG = "DuanxinxiugaiActivity";

    private String url = Contast.Domain+"api/WorkerGetIdentifyCodeUserLogin.ashx?";

    private ImageView iv_back;
    private TextView tv_phone;
    private EditText et_code;
    private Button btn_code;
    private Button btn_next;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duanxinxiugai);
        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_duanxinxiugai_back);
        tv_phone = (TextView) findViewById(R.id.tv_duanxinxiugai_phone);
        et_code = (EditText) findViewById(R.id.et_duanxinxiugai_code);
        btn_code = (Button) findViewById(R.id.btn_duanxinxiugai_code);
        btn_next = (Button) findViewById(R.id.btn_duanxinxiugai_next);


        String phone = Contast.worker.getW_Tel();
        String qian = phone.substring(0,3);
        String hou = phone.substring(7,phone.length());
        tv_phone.setText(qian+"****"+hou);
        et_code.setText(code);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCode();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = et_code.getText().toString();
                Intent intent = new Intent(DuanxinxiugaiActivity.this,ShezhimimaActivity.class);
                intent.putExtra("code",code);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initCode() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("拼命加载中...");
        pd.show();
        FormBody.Builder params = new FormBody.Builder();
        params.add("W_Tel", Contast.worker.getW_Tel());
        params.add("keys", Contast.KEYS);
        OkHttpClient client = new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .post(params.build())
                .build();

        okhttp3.Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //响应失败
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(DuanxinxiugaiActivity.this,"网络连接异样，请稍后重试...",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                String string = response.body().string();
                if(response.code() != HttpURLConnection.HTTP_OK){
                    Toast.makeText(DuanxinxiugaiActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i(TAG, "onResponse: json=" + string);
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            final Error error = JSON.parseObject(string, Error.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                    Toast.makeText(DuanxinxiugaiActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            final String str = string.substring(1, string.length() - 1);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                    et_code.setText(str);
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                Toast.makeText(DuanxinxiugaiActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
