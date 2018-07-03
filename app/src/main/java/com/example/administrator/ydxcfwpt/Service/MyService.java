package com.example.administrator.ydxcfwpt.Service;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Activity.LoginActivity;
import com.example.administrator.ydxcfwpt.Activity.MainActivity;
import com.example.administrator.ydxcfwpt.Activity.WorkActivity;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.Worker;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.Utils.ContextUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyService extends Service {

    private static final String TAG = "MyService";

    private String url = Contast.Domain + "api/WorkerAdress.ashx?";


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (Contast.worker.getW_Tel()!=null) {
                    SharedPreferences sf = ContextUtil.getInstance().getSharedPreferences("Login", MODE_PRIVATE);
                    String w_tel= sf.getString("W_Tel", "");
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("W_Tel", w_tel);
                    params.add("W_IMEI", Contast.worker.getW_IMEI());
                    params.add("W_Lng", "" + Contast.Lon);
                    params.add("W_Lat", "" + Contast.Lat);
                    params.add("keys", Contast.KEYS);
                    Log.w("nima",String.valueOf(Contast.Lat)+String.valueOf(Contast.Lon));
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(params.build())
                            .build();
                    okhttp3.Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "实时位置刷新失败", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String string = response.body().string();
                            Log.i("nima",string);
                            if (!TextUtils.isEmpty(string)) {
                                if (string.contains("ErrorStr")) {
                                    final Error error = JSON.parseObject(string, Error.class);
                                    Toast.makeText(getApplicationContext(), error.getErrorStr(), Toast.LENGTH_SHORT).show();
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
                                }
                            } else {
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), "实时位置刷新失败", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    });
                }else {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "数据获取失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }, 0, 60000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
