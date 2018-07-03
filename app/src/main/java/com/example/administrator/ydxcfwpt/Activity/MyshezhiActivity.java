package com.example.administrator.ydxcfwpt.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.Utils.ActivityUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//设置
public class MyshezhiActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MyshezhiActivity";

    private String url = Contast.Domain + "api/WorkerGetIdentifyCodeUserLogin.ashx?";
    private String url_out = Contast.Domain + "api/WorkerStateNow.ashx?";


    private ImageView iv_back;
    private LinearLayout ll_xiugaimima;
    //    private LinearLayout ll_changjianwenti;
//    private LinearLayout ll_shiyongxuzhi;
    private LinearLayout ll_fuwutiaokuan;
    private LinearLayout ll_guanyuwomen;
    private Button btn_logout;
    private String code;
    private LinearLayout ll_changyongdizhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshezhi);
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_myshezhi_back);
        ll_xiugaimima = (LinearLayout) findViewById(R.id.ll_myshezhi_xiugaimima);
//        ll_changjianwenti = (LinearLayout) findViewById(R.id.ll_myshezhi_changjianwenti);
//        ll_shiyongxuzhi = (LinearLayout) findViewById(R.id.ll_myshezhi_shiyongxuzhi);
        ll_fuwutiaokuan = (LinearLayout) findViewById(R.id.ll_myshezhi_fuwutiaokuan);
        ll_guanyuwomen = (LinearLayout) findViewById(R.id.ll_myshezhi_guanyuwomen);
        btn_logout = (Button) findViewById(R.id.btn_myshezhi_logout);
        ll_changyongdizhi=(LinearLayout)findViewById(R.id.ll_myshezhi_changyongdizhi);
        iv_back.setOnClickListener(this);
        ll_xiugaimima.setOnClickListener(this);
//        ll_changjianwenti.setOnClickListener(this);
//        ll_shiyongxuzhi.setOnClickListener(this);
        ll_fuwutiaokuan.setOnClickListener(this);
        ll_guanyuwomen.setOnClickListener(this);
        ll_changyongdizhi.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_myshezhi_back:
                finish();
                break;
            case R.id.ll_myshezhi_xiugaimima:
//                View bottomView = View.inflate(MyshezhiActivity.this, R.layout.item_listview_dialog, null);//填充ListView布局
//                ListView listView = (ListView) bottomView.findViewById(R.id.lv_item_listview_dialog);//初始化ListView控件
//                List<String> mimas = new ArrayList<String>();
//                mimas.add("短信验证修改");
//                mimas.add("用原密码修改");
//                listView.setAdapter(new DialogListViewAdapter(MyshezhiActivity.this, mimas));//ListView设置适配器
//                final AlertDialog dialog = new AlertDialog.Builder(MyshezhiActivity.this)
//                        .setTitle("修改方式").setView(bottomView)//在这里把写好的这个listview的布局加载dialog中
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).create();
//                dialog.show();
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//响应listview中的item的点击事件
//
//                    @Override
//                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//                                            long arg3) {
//                        // TODO Auto-generated method stub
//                        TextView tv = (TextView) arg1.findViewById(R.id.tv_item_listview_dialog_text);//取得每条item中的textview控件
//                        String str = tv.getText().toString();
//                        if(str.equals("短信验证修改")){
//                            initCoede();
//                        }else if(str.equals("用原密码修改")){
                Intent intent = new Intent(MyshezhiActivity.this, YuanmimaxiugaiActivity.class);
                startActivity(intent);
//                        }
//                        dialog.dismiss();
//                    }
//                });
                break;
//            case R.id.ll_myshezhi_changjianwenti:
//                break;
            case R.id.ll_myshezhi_fuwutiaokuan:
                Intent intent3 = new Intent(MyshezhiActivity.this, ContentActivity.class);
                startActivity(intent3);
                break;
            case R.id.ll_myshezhi_guanyuwomen:
                Intent intent2 = new Intent(MyshezhiActivity.this, GuanyuwomenActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll_myshezhi_changyongdizhi:
                Intent intent1 = new Intent(MyshezhiActivity.this,ChangyongdizhiActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_myshezhi_logout:
//               退出登录
                final ProgressDialog pd = new ProgressDialog(MyshezhiActivity.this);
                pd.setMessage("拼命加载中...");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                FormBody.Builder params = new FormBody.Builder();
                params.add("W_Tel", Contast.worker.getW_Tel());
                params.add("W_IMEI", Contast.worker.getW_IMEI());
                params.add("W_WSID", "4");
                params.add("keys", Contast.KEYS);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url_out)
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
                                pd.dismiss();
                                Toast.makeText(MyshezhiActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        pd.dismiss();
                        //响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                        String string = response.body().string();
                        if (response.code() != HttpURLConnection.HTTP_OK) {
                            Toast.makeText(MyshezhiActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG, "onResponse: json=" + string);
                            if (!TextUtils.isEmpty(string)) {
                                if (string.contains("ErrorStr")) {
                                    final Error error = JSON.parseObject(string, Error.class);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyshezhiActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else  {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                                            SharedPreferences.Editor edit = sp.edit();
                                            edit.putBoolean("isLogin", false);
                                            edit.putString("W_Tel", "");
                                            edit.putString("W_IMEI", "");
                                            edit.commit();
                                            Contast.worker = null;
                                            ActivityUtils.finishAll();
                                            Intent intent1 = new Intent(MyshezhiActivity.this, MainActivity.class);
                                            startActivity(intent1);
                                            Toast.makeText(MyshezhiActivity.this, "已经退出登录", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyshezhiActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });
                break;
        }
    }

    private void initCoede() {
        final ProgressDialog pd = new ProgressDialog(MyshezhiActivity.this);
        pd.setMessage("拼命加载中...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        FormBody.Builder params = new FormBody.Builder();
        params.add("W_Tel", Contast.worker.getW_Tel());
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
                        pd.dismiss();
                        Toast.makeText(MyshezhiActivity.this, "网络连接异样，请稍后重试...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String string = response.body().string();
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    Toast.makeText(MyshezhiActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "onResponse: json=" + string);
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            final Error error = JSON.parseObject(string, Error.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                    Toast.makeText(MyshezhiActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            code = string.substring(1, string.length() - 1);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                    Intent intent = new Intent(MyshezhiActivity.this, DuanxinxiugaiActivity.class);
                                    if (!TextUtils.isEmpty(code)) {
                                        intent.putExtra("code", code);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                Toast.makeText(MyshezhiActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

}
