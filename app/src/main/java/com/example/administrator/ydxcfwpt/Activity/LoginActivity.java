package com.example.administrator.ydxcfwpt.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.Worker;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.Utils.StateUtils;
import com.example.administrator.ydxcfwpt.Utils.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscheckListener;
import cn.jpush.sms.listener.SmscodeListener;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.jpush.android.api.JPushInterface.getRegistrationID;

//登录
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private ImageView iv_back, iv_lchakanmima;
    private EditText et_phone;
    private EditText et_pwd;
    private Button bt_zhaohui;
    private Button btn_login;
    private Button btn_register;
    //    private TextView tv_duanxin;
//    private TextView tv_mima;
    private ImageView iv_title;
    private Button btn_code;
    private int time = 60;
    private Timer timer;
    private boolean flag = true;
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
    String url_pwdlogin = Contast.Domain + "api/WorkerLogin.ashx?";
    String url_codelogin = Contast.Domain + "api/WorkerLoginPhone.ashx?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SMSSDK.getInstance().initSdk(this);
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_login_back);
        iv_title = (ImageView) findViewById(R.id.iv_login_title);
        et_phone = (EditText) findViewById(R.id.et_login_phone);
        et_pwd = (EditText) findViewById(R.id.et_login_pwd);
        btn_login = (Button) findViewById(R.id.btn_login_login);
        btn_code = (Button) findViewById(R.id.btn_login_code);
        btn_register = (Button) findViewById(R.id.btn_login_register);
        bt_zhaohui = (Button) findViewById(R.id.btn_login_zhaohui);
//      tv_duanxin = (TextView) findViewById(R.id.tv_login_duanxin);
//      tv_mima = (TextView) findViewById(R.id.tv_login_mima);
        iv_lchakanmima = (ImageView) findViewById(R.id.iv_lchakanmima);
        iv_lchakanmima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iv_lchakanmima == null || et_pwd == null) return;
                if (iv_lchakanmima.isSelected()) {
                    iv_lchakanmima.setSelected(false);
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().length());
                } else {
                    iv_lchakanmima.setSelected(true);
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().length());
                }
            }
        });
        iv_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_code.setOnClickListener(this);
        btn_register.setOnClickListener(this);
//     tv_duanxin.setOnClickListener(this);
//     tv_mima.setOnClickListener(this);
        iv_title.setTag("mima");
        bt_zhaohui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, WangJiMiMaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login_back:
                finish();
                break;
            case R.id.btn_login_register:
                Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
                intent2.putExtra("from", 1);//注册账号，进行短信验证
                startActivity(intent2);
                break;
            case R.id.btn_login_code:
                String phone1 = et_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone1)) {
                    Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!StringUtils.isMobileNO(phone1)) {
                    Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.getInstance().getSmsCodeAsyn(phone1, "1", new SmscodeListener() {
                    @Override
                    public void getCodeSuccess(final String uuid) {
                        // 获取验证码成功，uuid 为此次获取的唯一标识码。
                        Toast.makeText(LoginActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void getCodeFail(int errCode, final String errMsg) {
                        // 获取验证码失败 errCode 为错误码，详情请见文档后面的错误码表；errMsg 为错误描述。
                        Toast.makeText(LoginActivity.this, errMsg, Toast.LENGTH_SHORT).show();
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
//            case R.id.tv_login_duanxin:
//                tv_duanxin.setTextSize(24);
//                tv_mima.setTextSize(16);
//                iv_title.setTag("duanxin");
//                et_pwd.setHint("请输入验证码");
//                et_pwd.setInputType(InputType.TYPE_CLASS_NUMBER);
//                btn_register.setVisibility(View.GONE);
//                btn_code.setVisibility(View.VISIBLE);
//                btn_login.setText("登录");
//                break;
//            case R.id.tv_login_mima:
//                tv_duanxin.setTextSize(16);
//                tv_mima.setTextSize(24);
//                iv_title.setTag("mima");
//                et_pwd.setHint("请输入密码");
//                et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                btn_code.setVisibility(View.GONE);
//                btn_register.setVisibility(View.VISIBLE);
//                btn_login.setText("登录");
//                break;
            case R.id.btn_login_login:
                //对输入框进行判空，验证
                final String phone = et_phone.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                String tag = (String) iv_title.getTag();
                if ("duanxin".equals(tag)) {
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(pwd)) {
                        Toast.makeText(LoginActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!StringUtils.isMobileNO(phone)) {
                        Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //TODO
                    //发送网路请求,跳转到输入短信验证码界面
                    SMSSDK.getInstance().checkSmsCodeAsyn(phone, pwd, new SmscheckListener() {
                        @Override
                        public void checkCodeSuccess(final String code) {
                            // 验证码验证成功，code 为验证码信息。
                            //发送网路请求,进行注册
                            final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                            pd.setMessage("拼命加载中...");
                            pd.show();
                            FormBody.Builder params = new FormBody.Builder();
                            params.add("W_Tel", phone);
                            params.add("keys", Contast.KEYS);
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(url_codelogin)
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
                                            Toast.makeText(LoginActivity.this, "网络异样，请稍后重试...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                                    pd.dismiss();
                                    //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                                    String string = response.body().string();
                                    if (response.code() != HttpURLConnection.HTTP_OK) {
                                        Toast.makeText(LoginActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.i(TAG, "onResponse: json=" + string);
                                        if (!TextUtils.isEmpty(string)) {
                                            if (string.contains("ErrorStr")) {
                                                final Error error = JSON.parseObject(string, Error.class);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(LoginActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                Worker worker = JSON.parseObject(string, Worker.class);
                                                Contast.worker = worker;
                                                Log.i(TAG, "onResponse: " + worker.toString());
                                                //如果验证成功
                                                SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                                                SharedPreferences.Editor edit = sp.edit();
                                                edit.putBoolean("isLogin", true);
                                                edit.putString("W_Tel", Contast.worker.getW_Tel());
                                                edit.putString("W_IMEI", Contast.worker.getW_IMEI());
                                                edit.commit();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
//                                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                        StateUtils.setState(LoginActivity.this, 2);
                                                    }
                                                });
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                                Toast.makeText(LoginActivity.this, worker.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(LoginActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(LoginActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if ("mima".equals(tag)) {
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(pwd)) {
                        Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!StringUtils.isMobileNO(phone)) {
                        Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pwd.length() < 6 || pwd.length() > 16) {
                        Toast.makeText(LoginActivity.this, "密码长度不正确，请重新输入", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    if (phone.equals("18717309732") && pwd.equals("123456")) {
//                        //如果验证成功
//                        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
//                        SharedPreferences.Editor edit = sp.edit();
//                        edit.putBoolean("isLogin", true);
//                        edit.commit();
//                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                        Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent1);
//                        finish();
//                    }
//                    //TODO
//                    //发送网路请求对账号密码进行验证
                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("拼命加载中...");
                    pd.show();
                    FormBody.Builder params = new FormBody.Builder();
                    String registrationId = getRegistrationID(LoginActivity.this);
                    params.add("W_Tel", phone);
                    params.add("W_Pwd", pwd);
                    params.add("keys", Contast.KEYS);
                    params.add("W_RegistrationID", registrationId);
                    Log.w("",registrationId);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url_pwdlogin)
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
                                    Toast.makeText(LoginActivity.this, "网络异样，请稍后重试...", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(okhttp3.Call call, Response response) throws IOException {
                            pd.dismiss();
                            //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                            String string = response.body().string();
                            if (response.code() != HttpURLConnection.HTTP_OK) {
                                Toast.makeText(LoginActivity.this, "连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i(TAG, "onResponse: json=" + string);
                                if (!TextUtils.isEmpty(string)) {
                                    if (string.contains("ErrorStr")) {
                                        final Error error = JSON.parseObject(string, Error.class);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Worker worker = JSON.parseObject(string, Worker.class);
                                        Contast.worker = worker;
                                        Log.i(TAG, "onResponse: " + worker.toString());
                                        //如果验证成功
                                        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                                        SharedPreferences.Editor edit = sp.edit();
                                        edit.putBoolean("isLogin", true);
                                        edit.putString("W_Tel", Contast.worker.getW_Tel());
                                        edit.putString("W_IMEI", Contast.worker.getW_IMEI());
                                        edit.commit();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
//                                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                StateUtils.setState(LoginActivity.this, 2);
                                            }
                                        });
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
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
