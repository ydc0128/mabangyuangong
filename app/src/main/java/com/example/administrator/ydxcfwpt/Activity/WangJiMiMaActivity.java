package com.example.administrator.ydxcfwpt.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.administrator.ydxcfwpt.Utils.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscheckListener;
import cn.jpush.sms.listener.SmscodeListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/25.
 */
//忘记密码
public class WangJiMiMaActivity extends BaseActivity {
    private String url = Contast.Domain + "/api/WorkerUpdatePasswordByIdentifyCode.ashx?";
    private static final String TAG = "WangJiMiMaActivity";
    @BindView(R.id.iv_wjmm_back)
    ImageView ivWjmmBack;
    @BindView(R.id.tv_wjmm_title)
    TextView tvWjmmTitle;
    @BindView(R.id.btn_wjmm_more)
    Button btnWjmmMore;
    @BindView(R.id.et_wjmm_phone)
    EditText etRegisterPhone;
    @BindView(R.id.et_wjmm_code)
    EditText etWjmmCode;
    @BindView(R.id.btn_wjmm_code)
    Button btnWjmmCode;
    @BindView(R.id.et_wjmm_pwd)
    EditText etWjmmPwd;
    @BindView(R.id.iv_wjmm_chakanmima)
    ImageView ivWjmmChakanmima;
    @BindView(R.id.et_wjmm_surepwd)
    EditText etWjmmSurepwd;
    @BindView(R.id.iv_wjmm_chongfuchakan)
    ImageView ivWjmmChongfuchakan;
    @BindView(R.id.btn_wjmm_submit)
    Button btnWjmmSubmit;
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
                        btnWjmmCode.setText("重获验证码");
                        btnWjmmCode.setClickable(true);
                        flag = false;
                    } else {
                        btnWjmmCode.setText(time + "s");
                        btnWjmmCode.setClickable(false);
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
        setContentView(R.layout.activity_wangjimima);
        SMSSDK.getInstance().initSdk(this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_wjmm_back, R.id.btn_wjmm_code, R.id.btn_wjmm_submit, R.id.iv_wjmm_chakanmima, R.id.iv_wjmm_chongfuchakan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wjmm_back:
                finish();
                break;
            case R.id.btn_wjmm_code:
                String phone1 = etRegisterPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone1)) {
                    Toast.makeText(WangJiMiMaActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!StringUtils.isMobileNO(phone1)) {
                    Toast.makeText(WangJiMiMaActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.getInstance().getSmsCodeAsyn(phone1, "1", new SmscodeListener() {
                    @Override
                    public void getCodeSuccess(final String uuid) {
                        // 获取验证码成功，uuid 为此次获取的唯一标识码。
                        Toast.makeText(WangJiMiMaActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void getCodeFail(int errCode, final String errMsg) {
                        // 获取验证码失败 errCode 为错误码，详情请见文档后面的错误码表；errMsg 为错误描述。
                        Toast.makeText(WangJiMiMaActivity.this, errMsg, Toast.LENGTH_SHORT).show();
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
            case R.id.btn_wjmm_submit:
                final String phone = etRegisterPhone.getText().toString().trim();
                final String code = etWjmmCode.getText().toString().trim();
                final String pwd = etWjmmPwd.getText().toString();
                String newPwd = etWjmmSurepwd.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(WangJiMiMaActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!StringUtils.isMobileNO(phone)) {
                    Toast.makeText(WangJiMiMaActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(WangJiMiMaActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(WangJiMiMaActivity.this, "原密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.length() < 6 || pwd.length() > 16) {
                    Toast.makeText(WangJiMiMaActivity.this, "原密码长度不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newPwd)) {
                    Toast.makeText(WangJiMiMaActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPwd.length() < 6 || newPwd.length() > 16) {
                    Toast.makeText(WangJiMiMaActivity.this, "新密码长度不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pwd.equals(newPwd)) {
                    Toast.makeText(WangJiMiMaActivity.this, "密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.getInstance().checkSmsCodeAsyn(phone, code, new SmscheckListener() {
                    @Override
                    public void checkCodeSuccess(final String code) {
                        // 验证码验证成功，code 为验证码信息。
                        //发送网路请求,进行注册
                        final ProgressDialog pd = new ProgressDialog(WangJiMiMaActivity.this);
                        pd.setMessage("拼命加载中...");
                        pd.show();
                        FormBody.Builder params = new FormBody.Builder();
                        params.add("W_Tel", phone);
                        params.add("W_PwdNew", pwd);
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
                                        Toast.makeText(WangJiMiMaActivity.this, "网络连接异样，请稍后重试...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                                pd.dismiss();
                                //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                                String string = response.body().string();
                                Log.i(TAG, "onResponse: json=" + string);
                                if(response.code() != HttpURLConnection.HTTP_OK){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(WangJiMiMaActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    if (!TextUtils.isEmpty(string)) {
                                        if (string.contains("ErrorStr")) {
                                            final Error error = JSON.parseObject(string, Error.class);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(WangJiMiMaActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else if (string.contains("OkStr")) {
//                                        Log.i(TAG, "onResponse: " + Contast.worker.toString());
                                            //如果验证成功
//                                        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
//                                        SharedPreferences.Editor edit = sp.edit();
//                                        edit.putBoolean("isLogin", true);
//                                        edit.putString("W_Tel", Contast.worker.getW_Tel());
//                                        edit.putString("W_IMEI", Contast.worker.getW_IMEI());
//                                        edit.commit();
                                            finish();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(WangJiMiMaActivity.this, "密码已找回,请重新登录", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(WangJiMiMaActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(WangJiMiMaActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.iv_wjmm_chakanmima:
                if (ivWjmmChakanmima == null || etWjmmPwd == null) return;
                if (ivWjmmChakanmima.isSelected()) {
                    ivWjmmChakanmima.setSelected(false);
                    etWjmmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etWjmmPwd.setSelection(etWjmmPwd.getText().length());
                } else {
                    ivWjmmChakanmima.setSelected(true);
                    etWjmmPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etWjmmPwd.setSelection(etWjmmPwd.getText().length());

                }
                break;
            case R.id.iv_wjmm_chongfuchakan:
                if (ivWjmmChongfuchakan == null || etWjmmSurepwd == null) return;
                if (ivWjmmChongfuchakan.isSelected()) {
                    ivWjmmChongfuchakan.setSelected(false);
                    etWjmmSurepwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etWjmmSurepwd.setSelection(etWjmmSurepwd.getText().length());
                } else {
                    ivWjmmChongfuchakan.setSelected(true);
                    etWjmmSurepwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etWjmmSurepwd.setSelection(etWjmmSurepwd.getText().length());

                }
                break;
        }
    }
}
