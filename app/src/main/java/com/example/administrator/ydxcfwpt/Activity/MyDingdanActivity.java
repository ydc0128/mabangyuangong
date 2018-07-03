package com.example.administrator.ydxcfwpt.Activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Adapter.DingdanAdapter;
import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.administrator.ydxcfwpt.R.id.line_mydingdan_dengdaifuwu;
import static com.example.administrator.ydxcfwpt.R.id.line_mydingdan_yiquxiao;
import static com.example.administrator.ydxcfwpt.R.id.line_mydingdan_yiwancheng;
import static com.example.administrator.ydxcfwpt.R.id.line_mydingdan_zhengzaifuwu;

//订单
public class MyDingdanActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MyDingdanActivity";

    private String url = Contast.Domain + "api/WorkerAllOrder.ashx?";

    private ImageView iv_back;
    private RadioButton rb_dengdaifuwu;
    private RadioButton rb_zhengzaifuwu;
    private RadioButton rb_yiwancheng;
    private RadioButton rb_yiquxiao;

    private View line_dengdaifuwu;
    private View line_zhengzaifuwu;
    private View line_yiwancheng;
    private View line_yiquxiao;
    private PullToRefreshListView listView;
    private List<Dingdan> dingdanList;
    private DingdanAdapter adapter;
    private RelativeLayout rl_null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dingdan);
        initViews();
        initListView();
//      refelash();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_mydingdan_back);
        rb_dengdaifuwu = (RadioButton) findViewById(R.id.rb_mydingdan_dengdaifuwu);
        rb_zhengzaifuwu = (RadioButton) findViewById(R.id.rb_mydingdan_zhengzaifuwu);
        rb_yiwancheng = (RadioButton) findViewById(R.id.rb_mydingdan_yiwancheng);
        rb_yiquxiao = (RadioButton) findViewById(R.id.rb_mydingdan_yiquxiao);
        line_dengdaifuwu = findViewById(line_mydingdan_dengdaifuwu);
        line_zhengzaifuwu = findViewById(line_mydingdan_zhengzaifuwu);
        line_yiwancheng = findViewById(line_mydingdan_yiwancheng);
        line_yiquxiao = findViewById(line_mydingdan_yiquxiao);
        rl_null = (RelativeLayout) findViewById(R.id.rl_yuemingxi_null);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rb_dengdaifuwu.setOnCheckedChangeListener(this);
        rb_zhengzaifuwu.setOnCheckedChangeListener(this);
        rb_yiwancheng.setOnCheckedChangeListener(this);
        rb_yiquxiao.setOnCheckedChangeListener(this);
    }

    private void initListView() {
        listView = (PullToRefreshListView) findViewById(R.id.ptrlv_mydingdan_listview);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        dingdanList = new ArrayList<>();
        adapter = new DingdanAdapter(this, dingdanList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dingdan dingdan = (Dingdan) adapter.getItem(position);
                Intent intent = new Intent(MyDingdanActivity.this, WorkActivity.class);
                intent.putExtra("dingdan", dingdan);
                startActivity(intent);
            }
        });
        ILoadingLayout Labels = listView.getLoadingLayoutProxy(true, true);
        Labels.setPullLabel("快点拉");
        Labels.setRefreshingLabel("正在刷新");
        Labels.setReleaseLabel("放开刷新");
//        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                setViews();
//                new FinishRefresh().execute();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                setViews();
//                new FinishRefresh().execute();
//            }
//        });
    }

    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listView.onRefreshComplete();
            adapter.notifyDataSetChanged();
        }
    }
    private void checkList(List<Dingdan> list) {
        if (list.size() > 0) {
            rl_null.setVisibility(View.INVISIBLE);
        } else {
            rl_null.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        refelash();
        setViews();
//        if(rb_zhengzaifuwu.isChecked()){
//            List<Dingdan> zhengzaifuwuList = new ArrayList<>();
//            for (Dingdan item : dingdanList) {
//                int type = item.getO_TypeID();
//                if (type == 2 || type == 3 || type == 8 || type == 7) {
//                    if (item.getO_ISCancel() == 0) {
//                        zhengzaifuwuList.add(item);
//                    }
//                }
//            }
//            adapter.setData(zhengzaifuwuList);
//        }else if(rb_dengdaifuwu.isChecked()){
//            List<Dingdan> dengdaifuwuList = new ArrayList<>();
//            for (Dingdan item : dingdanList) {
//                int type = item.getO_TypeID();
//                if (type == 1) {
//                    if (item.getO_ISCancel() == 0) {
//                        dengdaifuwuList.add(item);
//                    }
//                }
//            }
//            adapter.setData(dengdaifuwuList);
//        }else if(rb_yiwancheng.isChecked()){
//            List<Dingdan> yiwanchengList = new ArrayList<>();
//            for (Dingdan item : dingdanList) {
//                int type = item.getO_TypeID();
//                if (type == 4) {
//                    if (item.getO_ISCancel() == 0) {
//                        yiwanchengList.add(item);
//                    }
//                }
//            }
//            adapter.setData(yiwanchengList);
//        }else if(rb_yiquxiao.isChecked()){
//            List<Dingdan> yiquxiaoList = new ArrayList<>();
//            for (Dingdan item : dingdanList) {
//                int type = item.getO_TypeID();
//                if (type == 5 || type == 6 || item.getO_ISCancel() == 1) {
//                    yiquxiaoList.add(item);
//                }
//            }
//            adapter.setData(yiquxiaoList);
//        }
    }

    private void setViews() {
        if (rb_zhengzaifuwu.isChecked()) {
            refelash();
            List<Dingdan> zhengzaifuwuList = new ArrayList<>();
            for (Dingdan item : dingdanList) {
                int type = item.getO_TypeID();
                if (type == 2 || type == 3 || type == 8 || type == 7||type==9) {
                    if (item.getO_ISCancel() == 0) {
                        zhengzaifuwuList.add(item);
                    }
                }
            }
            checkList(zhengzaifuwuList);
            adapter.setData(zhengzaifuwuList);
        } else if (rb_dengdaifuwu.isChecked()) {
            refelash();
            List<Dingdan> dengdaifuwuList = new ArrayList<>();
            for (Dingdan item : dingdanList) {
                int type = item.getO_TypeID();
                if (type == 1) {
                    if (item.getO_ISCancel() == 0) {
                        dengdaifuwuList.add(item);
                    }
                }
            }
            checkList(dengdaifuwuList);
            adapter.setData(dengdaifuwuList);
        } else if (rb_yiwancheng.isChecked()) {
            refelash();
            List<Dingdan> yiwanchengList = new ArrayList<>();
            for (Dingdan item : dingdanList) {
                int type = item.getO_TypeID();
                if (type == 4) {
                    if (item.getO_ISCancel() == 0) {
                        yiwanchengList.add(item);
                    }
                }
            }
            checkList(yiwanchengList);
            adapter.setData(yiwanchengList);
        } else if (rb_yiquxiao.isChecked()) {
            refelash();
            List<Dingdan> yiquxiaoList = new ArrayList<>();
            for (Dingdan item : dingdanList) {
                if (item.getO_ISCancel() == 1) {
                    yiquxiaoList.add(item);
                }
            }
            checkList(yiquxiaoList);
            adapter.setData(yiquxiaoList);
            Log.e("yiwancheng",yiquxiaoList.toString());
        }
    }

    private void refelash() {
        final ProgressDialog pd = new ProgressDialog(MyDingdanActivity.this);
        pd.setMessage("拼命加载中...");
        pd.show();
        FormBody.Builder params = new FormBody.Builder();
        params.add("keys", Contast.KEYS);
        params.add("O_WorkerTel", Contast.worker.getW_Tel());
        params.add("W_IMEI", Contast.worker.getW_IMEI());
        params.add("month", "");
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
                        Toast.makeText(MyDingdanActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                pd.dismiss();
                final String string = response.body().string();
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    Toast.makeText(MyDingdanActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                } else {
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains("ErrorStr")) {
                            final Error error = JSON.parseObject(string, Error.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MyDingdanActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dingdanList = JSON.parseArray(string, Dingdan.class);
                                    Log.i(TAG, "run: dingdanList =" + dingdanList);
                                    List<Dingdan> zhengzaifuwuList = new ArrayList<>();
                                    for (Dingdan item : dingdanList) {
                                        int type = item.getO_TypeID();
                                        if (type == 2 || type == 3 || type == 7 || type == 8||type==9) {
                                            if (item.getO_ISCancel() == 0) {
                                            zhengzaifuwuList.add(item);
                                        }
                                    }
                                    }
                                    adapter.setData(zhengzaifuwuList);

                                }
                            });

                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyDingdanActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_mydingdan_zhengzaifuwu:
                if (isChecked) {
                    final ProgressDialog pd = new ProgressDialog(MyDingdanActivity.this);
                    pd.setMessage("拼命加载中...");
                    pd.show();
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("keys", Contast.KEYS);
                    params.add("O_WorkerTel", Contast.worker.getW_Tel());
                    params.add("W_IMEI", Contast.worker.getW_IMEI());
                    params.add("month", "");
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
                                    Toast.makeText(MyDingdanActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(final Call call, Response response) throws IOException {
                            pd.dismiss();
                            final String string = response.body().string();
                            if (response.code() != HttpURLConnection.HTTP_OK) {
                                Toast.makeText(MyDingdanActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!TextUtils.isEmpty(string)) {
                                    if (string.contains("ErrorStr")) {
                                        final Error error = JSON.parseObject(string, Error.class);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(MyDingdanActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                line_zhengzaifuwu.setVisibility(View.VISIBLE);
                                                rb_yiwancheng.setChecked(false);
                                                rb_dengdaifuwu.setChecked(false);
                                                rb_yiquxiao.setChecked(false);
                                                line_yiwancheng.setVisibility(View.INVISIBLE);
                                                line_dengdaifuwu.setVisibility(View.INVISIBLE);
                                                line_yiquxiao.setVisibility(View.INVISIBLE);
                                                dingdanList = JSON.parseArray(string, Dingdan.class);
                                                Log.i(TAG, "run: dingdanList =" + dingdanList);
                                                List<Dingdan> zhengzaifuwuList = new ArrayList<>();
                                                for (Dingdan item : dingdanList) {
                                                    int type = item.getO_TypeID();
                                                    if (type == 2 || type == 3 || type == 8 || type == 7||type==9) {
                                                        if (item.getO_ISCancel() == 0) {
                                                            zhengzaifuwuList.add(item);
                                                        }
                                                    }
                                                }
                                                checkList(zhengzaifuwuList);
                                                adapter.setData(zhengzaifuwuList);
                                            }
                                        });

                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyDingdanActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    });
//               line_zhengzaifuwu.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.rb_mydingdan_dengdaifuwu:
                if (isChecked) {
                        line_dengdaifuwu.setVisibility(View.VISIBLE);
                        rb_yiwancheng.setChecked(false);
                        rb_zhengzaifuwu.setChecked(false);
                        rb_yiquxiao.setChecked(false);
                        line_yiwancheng.setVisibility(View.INVISIBLE);
                        line_zhengzaifuwu.setVisibility(View.INVISIBLE);
                        line_yiquxiao.setVisibility(View.INVISIBLE);
                        List<Dingdan> dengdaifuwuList = new ArrayList<>();
                        for (Dingdan item : dingdanList) {
                            int type = item.getO_TypeID();
                            if (type == 1) {
                                if (item.getO_ISCancel() == 0) {
                                dengdaifuwuList.add(item);
                                }
                            }
                        }
                        checkList(dengdaifuwuList);
                        adapter.setData(dengdaifuwuList);
                    } else {
                        line_dengdaifuwu.setVisibility(View.INVISIBLE);
                    }

//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Dingdan dingdan = (Dingdan) adapter.getItem(position);
//                        Intent intent =new Intent(MyDingdanActivity.this,WorkActivity.class);
//                        intent.putExtra("dingdan", dingdan);
//                        startActivity();
//                    }
//                });
                break;
            case R.id.rb_mydingdan_yiwancheng:
                if (isChecked) {
                    final ProgressDialog pd = new ProgressDialog(MyDingdanActivity.this);
                    pd.setMessage("拼命加载中...");
                    pd.show();
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("keys", Contast.KEYS);
                    params.add("O_WorkerTel", Contast.worker.getW_Tel());
                    params.add("W_IMEI", Contast.worker.getW_IMEI());
                    params.add("month", "");
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
                                    Toast.makeText(MyDingdanActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(final Call call, Response response) throws IOException {
                            pd.dismiss();
                            final String string = response.body().string();
                            if (response.code() != HttpURLConnection.HTTP_OK) {
                                Toast.makeText(MyDingdanActivity.this, "服务器连接异常，请稍后重试...", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!TextUtils.isEmpty(string)) {
                                    if (string.contains("ErrorStr")) {
                                        final Error error = JSON.parseObject(string, Error.class);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(MyDingdanActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                line_yiwancheng.setVisibility(View.VISIBLE);
                                                rb_dengdaifuwu.setChecked(false);
                                                rb_zhengzaifuwu.setChecked(false);
                                                rb_yiquxiao.setChecked(false);
                                                line_dengdaifuwu.setVisibility(View.INVISIBLE);
                                                line_zhengzaifuwu.setVisibility(View.INVISIBLE);
                                                line_yiquxiao.setVisibility(View.INVISIBLE);
                                                dingdanList.clear();
                                                dingdanList = JSON.parseArray(string, Dingdan.class);
                                                Log.i(TAG, "run: dingdanList =" + dingdanList);
                                                List<Dingdan> zhengzaifuwuList = new ArrayList<>();
                                                for (Dingdan item : dingdanList) {
                                                    int type = item.getO_TypeID();
                                                    if (type == 4) {
                                                        if (item.getO_ISCancel() == 0) {
                                                            zhengzaifuwuList.add(item);
                                                        }
                                                    }
                                                }
                                                checkList(zhengzaifuwuList);
                                                adapter.setData(zhengzaifuwuList);
                                                Log.e("zz", "zz" + dingdanList);
                                            }
                                        });

                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyDingdanActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }

//
                break;
            case R.id.rb_mydingdan_yiquxiao:
                if (isChecked) {
                        line_yiquxiao.setVisibility(View.VISIBLE);
                        rb_dengdaifuwu.setChecked(false);
                        rb_zhengzaifuwu.setChecked(false);
                        rb_yiwancheng.setChecked(false);
                        line_dengdaifuwu.setVisibility(View.INVISIBLE);
                        line_zhengzaifuwu.setVisibility(View.INVISIBLE);
                        line_yiwancheng.setVisibility(View.INVISIBLE);
                        List<Dingdan> yiquxiaoList = new ArrayList<>();
                        for (Dingdan item : dingdanList) {
                            int quxiao = item.getO_ISCancel();
                            if (quxiao == 1) {
                                yiquxiaoList.add(item);
                            }
                        }
                        checkList(yiquxiaoList);
                        adapter.setData(yiquxiaoList);
                    } else {
                        line_yiquxiao.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

}
