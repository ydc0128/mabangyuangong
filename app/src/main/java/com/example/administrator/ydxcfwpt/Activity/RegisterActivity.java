package com.example.administrator.ydxcfwpt.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.RegisterPhone;
import com.example.administrator.ydxcfwpt.Bean.Worker;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.Utils.ActivityUtils;
import com.example.administrator.ydxcfwpt.Utils.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscheckListener;
import cn.jpush.sms.listener.SmscodeListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.jpush.android.api.JPushInterface.*;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private String url_register = Contast.Domain + "api/WorkerRegister.ashx?";
    private String url = Contast.Domain + "api/WorkerRegistrationID.ashx?";

    private ImageView iv_back, iv_chakanmima, iv_chongfu_chakan;
    private EditText et_phone;
    private EditText et_code;
    private EditText et_pwd;
    private EditText et_surepwd;
    private Button btn_register;
    private Button btn_code;
    private TextView tv_login;
    private int time = 60;
    private Timer timer;
    private boolean flag = true;
    private CheckBox cb_tiaokuan;
    private TextView tv_tiaokuan;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (time <= 0) {
                        time = 60;
                        btn_code.setText("重获验证码");
                        btn_code.setClickable(true);
                        flag = false;
                    } else {
                        btn_code.setText(time + "s");
                        btn_code.setClickable(false);
                        time--;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SMSSDK.getInstance().initSdk(this);
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_register_back);
        tv_login = (TextView) findViewById(R.id.tv_register_login);
        et_phone = (EditText) findViewById(R.id.et_register_phone);
        et_code = (EditText) findViewById(R.id.et_register_code);
        et_pwd = (EditText) findViewById(R.id.et_register_pwd);
        et_surepwd = (EditText) findViewById(R.id.et_register_surepwd);
        btn_code = (Button) findViewById(R.id.btn_register_code);
        btn_register = (Button) findViewById(R.id.btn_register_submit);
        iv_chakanmima = (ImageView) findViewById(R.id.iv_chakanmima);
        iv_chongfu_chakan = (ImageView) findViewById(R.id.iv_chongfuchakanmima);
        cb_tiaokuan = (CheckBox) findViewById(R.id.cb_register_tiaokuan);
        tv_tiaokuan = (TextView) findViewById(R.id.tv_register_tiaokuan);
        tv_tiaokuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(RegisterActivity.this, ContentActivity.class);
                intent3.putExtra("from", "fuwutiaokuan");
                startActivity(intent3);
            }
        });
        iv_back.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        btn_code.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        iv_chakanmima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iv_chongfu_chakan == null || et_pwd == null) return;
                if (iv_chongfu_chakan.isSelected()) {
                    iv_chongfu_chakan.setSelected(false);
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().length());
                } else {
                    iv_chongfu_chakan.setSelected(true);
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().length());

                }
            }
        });

        iv_chongfu_chakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iv_chakanmima == null || et_surepwd == null) return;
                if (iv_chakanmima.isSelected()) {
                    iv_chakanmima.setSelected(false);
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_pwd.setSelection(et_surepwd.getText().length());
                } else {
                    iv_chakanmima.setSelected(true);
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_pwd.setSelection(et_surepwd.getText().length());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_register_back:
                finish();
                break;
            case R.id.tv_register_login:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register_code:
                String phone1 = et_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone1)) {
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!StringUtils.isMobileNO(phone1)) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.getInstance().getSmsCodeAsyn(phone1, "1", new SmscodeListener() {
                    @Override
                    public void getCodeSuccess(final String uuid) {
                        // 获取验证码成功，uuid 为此次获取的唯一标识码。
                        Toast.makeText(RegisterActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void getCodeFail(int errCode, final String errMsg) {
                        // 获取验证码失败 errCode 为错误码，详情请见文档后面的错误码表；errMsg 为错误描述。
                        Toast.makeText(RegisterActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                    }
                });
                timer = new Timer();
                flag = true;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (flag) {
                            Message msg = Message.obtain();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    }
                }, 0, 1000);
                break;
            case R.id.btn_register_submit:
                //发送验证码之前对手机号进行判断
                final String phone = et_phone.getText().toString().trim();
                final String code = et_code.getText().toString().trim();
                final String pwd = et_pwd.getText().toString().trim();
                final String surepwd = et_surepwd.getText().toString().trim();
                if (!cb_tiaokuan.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "请先同意服务条款，在进行注册", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!StringUtils.isMobileNO(phone)) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(surepwd)) {
                    Toast.makeText(RegisterActivity.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pwd.equals(surepwd)) {
                    Toast.makeText(RegisterActivity.this, "两次密码输入不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.getInstance().checkSmsCodeAsyn(phone, code, new SmscheckListener() {
                    @Override
                    public void checkCodeSuccess(final String code) {
                        // 验证码验证成功，code 为验证码信息。
                        //发送网路请求,进行注册
                        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
                        pd.setMessage("拼命加载中...");
                        pd.show();
                        FormBody.Builder params = new FormBody.Builder();
                        params.add("W_Tel", phone);
                        params.add("W_Province", Contast.Province);
                        params.add("W_City", Contast.City);
                        params.add("W_County", Contast.District);
                        params.add("W_Pwd", pwd);
                        params.add("keys", Contast.KEYS);
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(url_register)
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
                                        Toast.makeText(RegisterActivity.this, "网络连接异样，请稍后重试...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                                pd.dismiss();
                                //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                                String string = response.body().string();
                                Log.i(TAG, "onResponse: json=" + string);
                                if (response.code() != HttpURLConnection.HTTP_OK) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    if (!TextUtils.isEmpty(string)) {
                                        if (string.contains("ErrorStr")) {
                                            final Error error = JSON.parseObject(string, Error.class);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(RegisterActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Contast.worker = JSON.parseObject(string, Worker.class);
                                            Log.i(TAG, "onResponse: " + Contast.worker.toString());
                                            //如果验证成功
//                                        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
//                                        SharedPreferences.Editor edit = sp.edit();
//                                        edit.putBoolean("isLogin", true);
//                                        edit.putString("W_Tel", Contast.worker.getW_Tel());
//                                        edit.putString("W_IMEI", Contast.worker.getW_IMEI());
//                                        edit.commit();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
//                                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                    zhucejiguang();
                                                }
                                            });
//                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                        intent.putExtra("zhuangtai", 4);
//                                        startActivity(intent);
//                                        finish();
                                        }
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(RegisterActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void checkCodeFail(int errCode, final String errMsg) {
                        // 验证码验证失败, errCode 为错误码，详情请见文档后面的错误码表；errMsg 为错误描述。
                        Toast.makeText(RegisterActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }


    private void zhucejiguang() {
        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage("拼命加载中...");
        pd.show();
        FormBody.Builder params = new FormBody.Builder();
        String registrationId = getRegistrationID(RegisterActivity.this);
        Log.i(TAG, "zhucejiguang: registrationId = " + registrationId);
        if (TextUtils.isEmpty(registrationId)) {
            Toast.makeText(RegisterActivity.this, "注册号为空", Toast.LENGTH_SHORT).show();
            return;
        }
        setAlias(RegisterActivity.this, 99999, registrationId);
        params.add("W_Tel", Contast.worker.getW_Tel());
        params.add("W_IMEI", Contast.worker.getW_IMEI());
        params.add("W_RegistrationID", registrationId);
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
                        Toast.makeText(RegisterActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                pd.dismiss();
                //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                String string = response.body().string();
                Log.i(TAG, "onResponse: json=" + string);
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            final Error error = JSON.parseObject(string, Error.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Contast.worker = JSON.parseObject(string, Worker.class);
                            Log.i(TAG, "onResponse: " + Contast.worker.toString());
                            //如果验证成功
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sp.edit();
                                    edit.putBoolean("isLogin", true);
                                    edit.putString("W_Tel", Contast.worker.getW_Tel());
                                    edit.putString("W_IMEI", Contast.worker.getW_IMEI());
                                    edit.commit();
                                    ActivityUtils.finishAll();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.putExtra("zhuangtai", 4);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}

