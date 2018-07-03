package com.example.administrator.ydxcfwpt.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcfwpt.Activity.DingdanXiangqingActivity;
import com.example.administrator.ydxcfwpt.Activity.MainActivity;
import com.example.administrator.ydxcfwpt.Activity.MyDingdanActivity;
import com.example.administrator.ydxcfwpt.Activity.WorkActivity;
import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.Error;
import com.example.administrator.ydxcfwpt.Bean.Fuwuxiangmu;
import com.example.administrator.ydxcfwpt.Bean.Worker;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.Utils.StateUtils;
import com.example.administrator.ydxcfwpt.View.MyListView;
import com.liyi.sutils.utils.time.CountdownUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.administrator.ydxcfwpt.Contast.Contast.dingdan;

/**
 * Created by Administrator on 2017/8/15.
 */

public class DangqianDingdanAdapter extends MyBaseAdapter {
    MainActivity mainActivity;
    private static final String TAG = "DingdanAdapter";
    private long shijiancha;

    public DangqianDingdanAdapter(MainActivity mainActivity, List dataList) {
        super(mainActivity, dataList);
        this.mainActivity = mainActivity;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_listview_dangqiandingdan;
    }

    @Override
    public Object getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    @Override
    public void setItemData(final int position, Object dataItem, Object viewHolder) {
        /**
         * 时间
         */
        /**
         * 原来代码
         */
        final Dingdan dingdan = (Dingdan) dataItem;
        //将holder 转为自己holder
        final ViewHolder myHolder = (ViewHolder) viewHolder;
//        String str = dingdan.getO_Time();
//        String shijian = str.replace("T", "  ");
        final List<String> phone = new ArrayList<>();
        phone.add(dingdan.getO_UTel());
        myHolder.tv_dingdanhao.setText(dingdan.getO_ID());
        myHolder.tv_phone.setText(dingdan.getO_CarName() + dingdan.getO_UTel());
        myHolder.tv_car.setText(dingdan.getO_PlateNumber());
        myHolder.heji.setText("¥"+dingdan.getO_Money());
        myHolder.tv_dizhi.setText(dingdan.getO_Adress() + "\n" + dingdan.getO_WriteAdress());
        myHolder.tv_daijinquan.setText("-¥"+dingdan.getO_Ticket()+"");
        myHolder.tv_shijian.setText(dingdan.getO_WashTime());
        String yy = dingdan.getO_WashTime();
        long yytime = getDateFromStr(yy);
        String time = dingdan.getO_Time();
        String shijian1 = time.replace("T", " ");
        String shijian2=shijian1.substring(0,shijian1.length()-3);
        long zztime=getDateFromStr(shijian2);
        long minutes = yytime - zztime;
        long shijiancha1=minutes/1000/60;
        if (shijiancha1 > 30&&shijiancha1<60*6) {
            myHolder.tv_fuwufei.setText("(含服务费¥10)");
        }else if  (shijiancha1>60*6) {
            myHolder.tv_fuwufei.setText("(含服务费¥20)");
        }else if (shijiancha1<30){
            myHolder.tv_fuwufei.setVisibility(View.GONE);
        }
        if (dingdan.getO_WashType() == 0) {
            Log.e("1111",1111+"");
            myHolder.zhuanchu.setVisibility(View.VISIBLE);
            myHolder.jiedan.setVisibility(View.VISIBLE);
            myHolder.tv_shijianleixing.setText("服务时间：");
        } else {
            Log.e("222",222+"");
            myHolder.zhuanchu.setVisibility(View.GONE);
            myHolder.jiedan.setVisibility(View.VISIBLE);
            myHolder.tv_shijianleixing.setText("预约时间：");
        }
        myHolder.jiedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dingdan.getO_ISCancel()==0) {
                        mainActivity.jiedan(position);
                }else {
                    Toast.makeText(mainActivity,"您的订单已取消",Toast.LENGTH_SHORT).show();
                }

//                    StateUtils.setState(activity, 5);
//                    Toast.makeText(context, "接单成功", Toast.LENGTH_SHORT).show();
            }
        });
        myHolder.zhuanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dingdan.getO_ISCancel() == 0) {
                    if (dingdan.getO_WashType() == 0) {
                        new android.app.AlertDialog.Builder(mainActivity).setTitle("您确认要转出订单吗？")
//                      .setIcon(android.R.drawable.ic_dialog_info)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        mainActivity.zhuanchu(position);
                                    }
                                })
                                .setNegativeButton("返回", null)
                                .show();

                    } else {
                        Toast.makeText(mainActivity,"预约订单不能转出",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(mainActivity, "您的订单已取消", Toast.LENGTH_SHORT).show();

                }
            }
        });
        myHolder.iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View photoView1 = View.inflate(context, R.layout.item_listview_dialog, null);//填充ListView布局
                ListView listView1 = (ListView) photoView1.findViewById(R.id.lv_item_listview_dialog);//初始化ListView控件
                listView1.setAdapter(new DialogListViewAdapter(context, phone));//ListView设置适配器
                final AlertDialog dialog1 = new AlertDialog.Builder(context)
                        .setView(photoView1)//在这里把写好的这个listview的布局加载dialog中
                        .create();
                dialog1.show();
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv = (TextView) view.findViewById(R.id.tv_item_listview_dialog_text);//取得每条item中的textview控件
                        String dianhua = tv.getText().toString();
                        // 拨号：激活系统的拨号组件
                        Intent intent = new Intent(); // 意图对象：动作 + 数据
                        intent.setAction(Intent.ACTION_DIAL); // 设置动作
                        Uri data = Uri.parse("tel:" + dianhua); // 设置数据
                        intent.setData(data);
                        mainActivity.startActivity(intent); // 激活Activity组件
                    }
                });
            }
        });
        List<Fuwuxiangmu> fuwuxiangmuList = new ArrayList<>();
        String fuwu = dingdan.getO_WPart();
        if (fuwu.contains("|")) {
            String[] split = fuwu.split("\\|");
            for (int i = 0; i < split.length; i++) {
                Fuwuxiangmu fuwuxiangmu = new Fuwuxiangmu();
                String[] strings = split[i].split(",");
                int num = Integer.parseInt(strings[0]);
                String price = strings[1];
                switch (num) {
                    case 1:
                    case 7:
                        fuwuxiangmu.setName("车外清洗");
                        break;
                    case 2:
                    case 9:
                        fuwuxiangmu.setName("标准清洗");
                        break;
                    case 16:
                    case 17:
                        fuwuxiangmu.setName("轮毂除锈");
                        break;
                    case 4:
                    case 11:
                        fuwuxiangmu.setName("打蜡");
                        break;
                    case 5:
                    case 12:
                        fuwuxiangmu.setName("室内精洗");
                        break;
                    case 18:
                    case 19:
                        fuwuxiangmu.setName("引擎清洗");
                        break;
                }
                fuwuxiangmu.setPrice(price);
                fuwuxiangmuList.add(fuwuxiangmu);
            }
        } else {
            Fuwuxiangmu fuwuxiangmu = new Fuwuxiangmu();
            String[] split = fuwu.split(",");
            int num = Integer.parseInt(split[0]);
            String price = split[1];
            switch (num) {
                case 1:
                case 7:
                    fuwuxiangmu.setName("车外清洗");
                    break;
                case 2:
                case 9:
                    fuwuxiangmu.setName("标准清洗");
                    break;
                case 16:
                case 17:
                    fuwuxiangmu.setName("轮毂除锈");
                    break;
                case 4:
                case 11:
                    fuwuxiangmu.setName("打蜡");
                    break;
                case 5:
                case 12:
                    fuwuxiangmu.setName("室内精洗");
                    break;
                case 18:
                case 19:
                    fuwuxiangmu.setName("引擎清洗");
                    break;
            }
            if (dingdan.getO_WashType() == 1) {
                    String yy1 = dingdan.getO_WashTime();
                    Long yytime1 = getDateFromStr(yy1);
                    long time1 = System.currentTimeMillis();
                    long minutes1 = yytime1 - time1;
                    long shijiancha=minutes1/1000/60;
                    Log.w("nima", shijiancha+"时间差");
                    if (shijiancha<=0){
                        myHolder.tv_fuwushijian.setText("倒计时结束");
                        myHolder.tv_fuwushijian.setTextColor(android.graphics.Color.RED);

                    }else if (shijiancha <= 30) {
                        myHolder.ll_daojishi.setVisibility(View.VISIBLE);
                        myHolder.tv_fuwushijian.setText("距离服务时间还有"+shijiancha + "分钟");
                        myHolder.daojishi();

                    }
            } else {
                String str = dingdan.getO_Time();
                String shijian = str.replace("T", " ");
                myHolder.ll_daojishi.setVisibility(View.GONE);
                myHolder.tv_shijian.setText(shijian);
            }
            fuwuxiangmu.setPrice(price);
            fuwuxiangmuList.add(fuwuxiangmu);
        }
        FuwuxiangmuAdapter adapter = new FuwuxiangmuAdapter(mainActivity, fuwuxiangmuList);
        myHolder.lv_fuwuxiangmu.setAdapter(adapter);
        setListViewHeightBasedOnChildren(myHolder.lv_fuwuxiangmu);
//        if (shijiancha > 30&&shijiancha<60*6) {
//            myHolder.tv_fuwufei.setText("10");
//        }else if  (shijiancha>60*6) {
//            myHolder.tv_fuwufei.setText("20");
//        }else if (shijiancha<30){
//            myHolder.tv_fuwufei.setText("0");
//        }
        if (dingdan.getO_TypeID()==1){
            myHolder.zhuanchu.setVisibility(View.VISIBLE);
            myHolder.jiedan.setVisibility(View.VISIBLE);
        }else {
            myHolder.zhuanchu.setVisibility(View.GONE);
            myHolder.jiedan.setVisibility(View.GONE);
        }
//        int type = dingdan.getO_TypeID();
//        if(type==1){
//            myHolder.btn_kaishifuwu.setVisibility(View.VISIBLE);
//        }else{
//            myHolder.btn_kaishifuwu.setVisibility(View.GONE);
//        }
//        myHolder.btn_kaishifuwu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(position==0){
//                    kaishifuwu(myHolder,dingdan);
//                }else{
//                    Toast.makeText(activity,"请按照订单顺序完成服务",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
//    private void kaishifuwu(final ViewHolder myHolder, final Dingdan dingdan) {
//        final ProgressDialog pd = new ProgressDialog(activity);
//        pd.setMessage("拼命加载中...");
//        pd.show();
//        FormBody.Builder params = new FormBody.Builder();
//        params.add("O_ID", dingdan.getO_ID());
//        params.add("W_Tel", Contast.worker.getW_Tel());
//        params.add("W_IMEI", Contast.worker.getW_IMEI());
//        params.add("W_WSID", "5");
//        params.add("data", "");
//        params.add("keys", Contast.KEYS);
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(params.build())
//                .build();
//        okhttp3.Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                pd.dismiss();
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(activity, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                pd.dismiss();
//                String string = response.body().string();
//                if (!TextUtils.isEmpty(string)) {
//                    if (string.contains("ErrorStr")) {
//                        final Error error = JSON.parseObject(string, Error.class);
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(activity, error.getErrorStr(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    } else  {
//                        dingdan = JSON.parseObject(string, Dingdan.class);
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                myHolder.btn_kaishifuwu.setVisibility(View.GONE);
//                                Intent intent = new Intent(activity,WorkActivity.class);
//                                intent.putExtra("dingdan",dingdan);
//                                activity.startActivity(intent);
//                            }
//                        });
//                    }
//                } else {
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(activity, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
//    }
    public void setListViewHeightBasedOnChildren(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0); // 计算子项View 的宽高

            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度

        listView.setLayoutParams(params);

    }


    /**
     * ViewHolder 通过构造方法中 实现具体view的绑定的方式 创建一个自实现绑定View的ViewHolder
     * Created by bailiangjin on 16/7/5.
     */
    public  class  ViewHolder {
        public final View root;
        private TextView tv_dingdanhao;
        private TextView tv_phone;
        private ImageView iv_phone;
        private TextView tv_car;
        private TextView tv_dizhi;
        private TextView tv_shijian;
        //private TextView tv_jiage;
        private MyListView lv_fuwuxiangmu;
        private TextView tv_fuwushijian;
        private LinearLayout ll_daojishi;
        private Button jiedan;
        private Button zhuanchu;
        private TextView heji;
        private TextView tv_daijinquan;
        private TextView tv_fuwufei;
        private TextView tv_shijianleixing;
        public ViewHolder(View root) {
            this.root = root;
            tv_fuwushijian = (TextView) root.findViewById(R.id.tv_fuwushijian);
            ll_daojishi = (LinearLayout) root.findViewById(R.id.ll_daojishi);
            tv_phone = (TextView) root.findViewById(R.id.tv_dangqiandingdan_listview_phone);
            tv_dingdanhao = (TextView) root.findViewById(R.id.tv_dangqiandingdan_listview_dingdanhao);
            tv_car = (TextView) root.findViewById(R.id.tv_dangqiandingdan_listview_car);
            tv_dizhi = (TextView) root.findViewById(R.id.tv_dangqiandingdan_listview_dizhi);
            tv_shijian = (TextView) root.findViewById(R.id.tv_dangqiandingdan_listview_shijian);
            tv_fuwufei=(TextView)root.findViewById(R.id.tv_dangqiandingdan_fuwufei);
// tv_jiage = (TextView) root.findViewById(R.id.tv_dangqiandingdan_listview_heji);
            tv_shijianleixing=(TextView)root.findViewById(R.id.tv_shijianleixing);
            iv_phone = (ImageView) root.findViewById(R.id.iv_dangqiandingdan_listview_phone);
            lv_fuwuxiangmu = (MyListView) root.findViewById(R.id.lv_dangqiandingdan_fiwuxiangmu);
            RelativeLayout fuwushijian_xianshi = (RelativeLayout) root.findViewById(R.id.rl_fuwushijian);
            zhuanchu = (Button) root.findViewById(R.id.btn_dangqiandingdan_listview_zhuanchu);
            jiedan = (Button) root.findViewById(R.id.btn_dangqiandingdan_listview_jiedan);
            heji=(TextView)root.findViewById(R.id.tv_dangqiandingdan_listview_heji);
            tv_daijinquan=(TextView)root.findViewById(R.id.tv_dingdan_daijinquan);
            lv_fuwuxiangmu.setFocusable(false);
            lv_fuwuxiangmu.setClickable(false);
            lv_fuwuxiangmu.setPressed(false);
            lv_fuwuxiangmu.setEnabled(false);

        }


        private void daojishi() {
//            CountDownTimer timer = new CountDownTimer((long) (30 * 60 * 1000), 1000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    //每隔countDownInterval秒会回调一次onTick()方法
////               Log.d(TAG, "onTick  " + millisUntilFinished / 1000);
//                }
//
//                @Override
//                public void onFinish() {
//
//                }
//            };
//            timer.start();// 开始计时
            String yy = dingdan.getO_WashTime();
            Long yytime = getDateFromStr(yy);
            long time = System.currentTimeMillis();
            long minutes = yytime - time;
            long shijiancha = minutes / 1000 / 60;
            Log.w("nima", shijiancha + "时间差");
            if (shijiancha > 30) {
                Log.w("22222222222222", "4444");
                long day = minutes / (24 * 60 * 60 * 1000);
                long hour1 = minutes / (60 * 60 * 1000);
                long min = ((minutes / (60 * 1000)) - day * 24 * 60 - hour1 * 60);
//                Toast.makeText(mainActivity.this, "离预约时间还有" + day + "天" + hour1 + "小时" + min + "分钟", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public long getDateFromStr(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Long temp = 0L;
            try {
                Date date = (Date) df.parse(dateStr);
                temp = date.getTime();
                return temp;
            } catch (Exception e) {
                e.printStackTrace();
                return temp;
        }
    }
}
