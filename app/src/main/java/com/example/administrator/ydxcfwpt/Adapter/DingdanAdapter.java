package com.example.administrator.ydxcfwpt.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.Fuwuxiangmu;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.View.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */

public class DingdanAdapter extends MyBaseAdapter {

    private static final String TAG = "DingdanAdapter";
    private Activity activity;

    public DingdanAdapter(Activity activity, List dataList) {
        super(activity, dataList);
        this.activity = activity;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_mydingdan;
    }

    @Override
    public Object getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    @Override
    public void setItemData(int position, Object dataItem, Object viewHolder) {
        final Dingdan dingdan = (Dingdan) dataItem;
        //将holder 转为自己holder
        ViewHolder myHolder = (ViewHolder) viewHolder;
        String str = dingdan.getO_Time();
        String shijian = str.replace("T","  ");
        final List<String> phone = new ArrayList<>();
        phone.add(dingdan.getO_UTel());
        if (dingdan.getO_TypeID()==4||dingdan.getO_ISCancel()==1){
            myHolder.ll_dianhua.setVisibility(View.GONE);
        }else {
            myHolder.ll_dianhua.setVisibility(View.VISIBLE);
        }
        myHolder.tv_shijian.setText(shijian);
        myHolder.tv_name.setText(dingdan.getO_CarName());
        myHolder.tv_dingdanhao.setText(dingdan.getO_ID());
        myHolder.tv_phone.setText(dingdan.getO_UTel());
        myHolder.tv_car.setText(dingdan.getO_PlateNumber());
        myHolder.tv_dizhi.setText(dingdan.getO_Adress()+"\n"+dingdan.getO_WriteAdress());
        myHolder.heji.setText("¥"+dingdan.getO_Money());
//        myHolder.tv_daijinquan.setText("-"+"¥"+dingdan.getO_Ticket());
        myHolder.iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View photoView1 = View.inflate(activity, R.layout.item_listview_dialog, null);//填充ListView布局
                ListView listView1 = (ListView) photoView1.findViewById(R.id.lv_item_listview_dialog);//初始化ListView控件
                listView1.setAdapter(new DialogListViewAdapter(activity, phone));//ListView设置适配器

                final AlertDialog dialog1 = new AlertDialog.Builder(activity)
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
                        activity.startActivity(intent); // 激活Activity组件
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
        }else{
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
            fuwuxiangmu.setPrice(price);
            fuwuxiangmuList.add(fuwuxiangmu);
        }

        FuwuxiangmuAdapter adapter = new FuwuxiangmuAdapter(activity,fuwuxiangmuList);
        myHolder.lv_fuwuxiangmu.setAdapter(adapter);
        setListViewHeightBasedOnChildren(myHolder.lv_fuwuxiangmu);
        switch (dingdan.getO_TypeID()) {
            case 1:
                if (dingdan.getO_ISCancel() == 0) {
                    myHolder.tv_zhuangtai.setText("待接单");
                } else {
                    myHolder.tv_zhuangtai.setText("已取消");
                }
                break;
            case 2:
                myHolder.tv_zhuangtai.setText("已接单");
                break;
            case 3:
                myHolder.tv_zhuangtai.setText("洗车中");
                break;
            case 4:
                myHolder.tv_zhuangtai.setText("已完成");
                break;
            case 5:
                myHolder.tv_zhuangtai.setText("申请退款");

                break;
            case 6:
                myHolder.tv_zhuangtai.setText("已退款");
                break;
            case 7:
                myHolder.tv_zhuangtai.setText("检查车辆状况");
                break;
            case 8:
                myHolder.tv_zhuangtai.setText("已到达");
                break;
            case 9:
                myHolder.tv_zhuangtai.setText("赶往途中");
                break;
        }

//        myHolder.tv_chepaihao.setText();
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for(int i = 0;i<listAdapter.getCount (); i++){ // listAdapter.getCount()返回数据项的数目

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
    public static class ViewHolder {
        public final View root;
        private TextView tv_name;
        private TextView tv_dingdanhao;
        private TextView tv_phone;
        private ImageView iv_phone;
        private TextView tv_car;
        private TextView tv_dizhi;
        private TextView tv_shijian;
        private MyListView lv_fuwuxiangmu;
        private TextView tv_zhuangtai;
        private TextView heji;
//        private TextView tv_daijinquan;
        private LinearLayout ll_dianhua;
        public ViewHolder(View root) {
            this.root = root;
            tv_name = (TextView) root.findViewById(R.id.tv_mydingdan_listview_name);
            tv_dingdanhao = (TextView) root.findViewById(R.id.tv_mydingdan_listview_dingdanhao);
            tv_phone = (TextView) root.findViewById(R.id.tv_mydingdan_listview_phone);
            tv_car = (TextView) root.findViewById(R.id.tv_mydingdan_listview_car);
            tv_dizhi = (TextView) root.findViewById(R.id.tv_mydingdan_listview_dizhi);
            tv_shijian = (TextView) root.findViewById(R.id.tv_mydingdan_listview_shijian);
            tv_zhuangtai=(TextView)root.findViewById(R.id.tv_dingdan_listview_zhuangtai);
            iv_phone = (ImageView) root.findViewById(R.id.iv_mydingdan_listview_phone);
            lv_fuwuxiangmu = (MyListView) root.findViewById(R.id.lv_mydingdan_fiwuxiangmu);
             heji=(TextView)root.findViewById(R.id.tv_dingdan1_listview_heji);
//            tv_daijinquan=(TextView)root.findViewById(R.id.tv_dingdan1_daijinquan);
            ll_dianhua=(LinearLayout) root.findViewById(R.id.ll_dianhua);
        }
    }


}
