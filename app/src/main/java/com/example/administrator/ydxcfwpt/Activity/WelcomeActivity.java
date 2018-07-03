package com.example.administrator.ydxcfwpt.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.Worker;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.Utils.StateUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//欢迎页面
public class WelcomeActivity extends BaseActivity {
    private String url = Contast.Domain + "api/WorkerLoginDefault.ashx?";
    private static final String TAG = "WelcomeActivity";
    boolean isFirstIn = false;
    private Intent intent;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        final SharedPreferences sharedPreferences = getSharedPreferences("is_first_in_data", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("firstStart", true)) {
            editor = sharedPreferences.edit();
            editor.putBoolean("firstStart", false);
            editor.commit();
            Intent intent = new Intent();
            intent.setClass(this,Yindaoye.class);
            startActivity(intent);
            finish();
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        initTitleInfo();
        initPermission();
    }
    private void initPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 1);
            } else {
                initLogin();
            }
        } else {
            initLogin();
        }
    }
    private void initLogin() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        String U_Tel = sp.getString("W_Tel", "");
        String U_IMEI = sp.getString("W_IMEI", "");
        if (isLogin) {
            FormBody.Builder params = new FormBody.Builder();
            params.add("W_Tel", U_Tel);
            params.add("W_IMEI", U_IMEI);
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
                    //响应失败
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //如果验证失败
                            SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putBoolean("isLogin", false);
                            edit.putString("W_Tel", "");
                            edit.putString("W_IMEI", "");
                            edit.commit();
                            Toast.makeText(WelcomeActivity.this, "网络连接异样，请稍后重试...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                    String string = response.body().string();
                    Log.i(TAG, "onResponse: json=" + string);
                    if(response.code() != HttpURLConnection.HTTP_OK){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WelcomeActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        if (!TextUtils.isEmpty(string)) {
                            if (string.contains("Error")) {
                                final Error error = JSON.parseObject(string, Error.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(WelcomeActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Worker worker = JSON.parseObject(string, Worker.class);
                                Contast.worker = worker;
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
//                                    Toast.makeText(WelcomeActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                                    StateUtils.setState(WelcomeActivity.this, 1);
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sp.edit();
                                    edit.putBoolean("isLogin", false);
                                    edit.putString("W_Tel", "");
                                    edit.putString("W_IMEI", "");
                                    edit.commit();
                                    Toast.makeText(WelcomeActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    initLogin();
                } else {
                    Toast.makeText(WelcomeActivity.this, "由于您没有赋予权限，可能导致部分功能无法使用", Toast.LENGTH_SHORT).show();
                    initLogin();
                }
        }
    }

}
