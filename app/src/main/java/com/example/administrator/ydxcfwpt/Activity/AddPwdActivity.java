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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.RegisterPhone;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//设置密码
public class AddPwdActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AddPwdActivity";

    private String url = Contast.Domain + "api/WorkerRegisterPwd.ashx?";

    private ImageView iv_back;
    private EditText et_pwd;
    private Button btn_submit;
    private RegisterPhone registerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pwd);
        Intent intent = getIntent();
        registerPhone = (RegisterPhone) intent.getSerializableExtra("registerPhone");
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_addpwd_back);
        et_pwd = (EditText) findViewById(R.id.et_addpwd_pwd);
        btn_submit = (Button) findViewById(R.id.btn_addpwd_ok);

        iv_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_addpwd_back:
                finish();
                break;
            case R.id.btn_addpwd_ok:
                //对输入框进行判空，验证
                String pwd = et_pwd.getText().toString().trim();

                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(AddPwdActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pwd.length() < 6 || pwd.length() > 16) {
                    Toast.makeText(AddPwdActivity.this, "密码长度不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                //TODO
                //发送网络请求，完成设置密码，如果设置成功，跳转到主页面
                final ProgressDialog pd = new ProgressDialog(AddPwdActivity.this);
                pd.setMessage("拼命加载中...");
                pd.show();
                FormBody.Builder params = new FormBody.Builder();
                params.add("W_Tel", registerPhone.getW_Tel());
                params.add("W_Pwd", pwd);
                params.add("keys", Contast.KEYS);
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
                                Toast.makeText(AddPwdActivity.this, "网络异样，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        pd.dismiss();
                        String string = response.body().string();
                        if(response.code() != HttpURLConnection.HTTP_OK){
                            Toast.makeText(AddPwdActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.i(TAG, "onResponse: " + string);
                            if (!TextUtils.isEmpty(string)) {
                                if (string.contains("ErrorStr")) {
                                    final Error error = JSON.parseObject(string, Error.class);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(AddPwdActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(AddPwdActivity.this, "密码设置完成", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Intent intent = new Intent(AddPwdActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddPwdActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });
                break;
        }
    }
}

