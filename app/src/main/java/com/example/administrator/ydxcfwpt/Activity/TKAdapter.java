package com.example.administrator.ydxcfwpt.Activity;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.ydxcfwpt.Adapter.MyBaseAdapter;
import com.example.administrator.ydxcfwpt.Adapter.YingyeeAdapter;
import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.TuiKuan;
import com.example.administrator.ydxcfwpt.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 */


public class TKAdapter extends MyBaseAdapter {


    public TKAdapter(Activity activity, List dataList) {
        super(activity, dataList);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_fuwuxiangmu_listview;
    }

    @Override
    public Object getViewHolder(View rootView) {
        return new TKAdapter.ViewHolder(rootView);
    }

    @Override
    public void setItemData(int position, Object dataItem, Object viewHolder) {
        final TuiKuan dingdan = (TuiKuan) dataItem;
        //将holder 转为自己holder
        TKAdapter.ViewHolder myHolder = (TKAdapter.ViewHolder) viewHolder;
        String str = dingdan.getP_Time();
        String shijian = str.replace("T","  ");
        myHolder.tv_time.setText(shijian);
        myHolder.tv_price.setText("-" + dingdan.getP_PayPrice());
    }
    /**
     * ViewHolder 通过构造方法中 实现具体view的绑定的方式 创建一个自实现绑定View的ViewHolder
     * Created by bailiangjin on 16/7/5.
     */
    public static class ViewHolder {
        public final View root;
        private TextView tv_time;
        private TextView tv_price;


        public ViewHolder(View root) {
            this.root = root;
            this.tv_time = (TextView) this.root.findViewById(R.id.tv_fuwuxiangmu_list_name);
            this.tv_price = (TextView) this.root.findViewById(R.id.tv_fuwuxiangmu_list_price);
        }
    }

}
