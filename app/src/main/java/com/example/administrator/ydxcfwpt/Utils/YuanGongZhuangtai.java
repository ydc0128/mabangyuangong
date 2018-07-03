package com.example.administrator.ydxcfwpt.Utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.Worker;
import com.example.administrator.ydxcfwpt.Contast.Contast;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2018/3/7.
 */

public class YuanGongZhuangtai {
    private static String url_setState = "http://api.mabangxiche.com/api/WorkerStateNow.ashx";
    private static final String TAG = "StateUtils";
    public static void setState(final Activity activity, int state) {
        SharedPreferences sp =ContextUtil.getInstance().getSharedPreferences("Login", MODE_PRIVATE);
        String tel= sp.getString("W_Tel","");
        String imei=sp.getString("W_IMEI","");
        FormBody.Builder params = new FormBody.Builder();
        params.add("W_Tel", tel);
        params.add("W_IMEI", imei);
        params.add("W_WSID", "" + state);
        params.add("keys", Contast.KEYS);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url_setState)
                .post(params.build())
                .build();

        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //响应失败
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                String string = response.body().string();
                Log.i(TAG, "onResponse: json=" + string);
                if (!TextUtils.isEmpty(string)) {
                    if (string.contains("ErrorStr")) {
                        final Error error = JSON.parseObject(string, Error.class);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                      Toast.makeText(activity, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Worker worker = JSON.parseObject(string, Worker.class);
                        Contast.worker = worker;
                        Log.i(TAG, "onResponse: " + worker.toString());
                        //如果验证成功
//                        SharedPreferences sp = activity.getSharedPreferences("Login", activity.MODE_PRIVATE);
//                        SharedPreferences.Editor edit = sp.edit();
//                        edit.putBoolean("isLogin", true);
//                        edit.putString("W_Tel", Contast.worker.getW_Tel());
//                        edit.putString("W_IMEI", Contast.worker.getW_IMEI());
//                        edit.commit();
                    }
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}
