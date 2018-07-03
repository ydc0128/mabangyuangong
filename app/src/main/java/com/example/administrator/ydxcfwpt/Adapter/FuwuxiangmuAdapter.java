package com.example.administrator.ydxcfwpt.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ydxcfwpt.Bean.Fuwuxiangmu;
import com.example.administrator.ydxcfwpt.R;

import java.util.List;


/**
 * Created by Administrator on 2017/8/26.
 */

public class FuwuxiangmuAdapter extends MyBaseAdapter {

    public FuwuxiangmuAdapter(Activity activity, List dataList) {
        super(activity, dataList);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_fuwuxiangmu_listview;
    }

    @Override
    public Object getViewHolder(View rootView) {
        return new FuwuxiangmuAdapter.ViewHolder(rootView);
    }

    @Override
    public void setItemData(int position, Object dataItem, Object viewHolder) {
        final Fuwuxiangmu fuwuxiangmu = (Fuwuxiangmu) dataItem;
        //将holder 转为自己holder
        FuwuxiangmuAdapter.ViewHolder myHolder = (FuwuxiangmuAdapter.ViewHolder) viewHolder;
        myHolder.tv_name.setText(fuwuxiangmu.getName());
        if(fuwuxiangmu.getName().equals("优惠券")){
            myHolder.tv_price.setText("-"+"¥"+fuwuxiangmu.getPrice());
            myHolder.tv_price.setTextColor(Color.RED);
        }else{
            myHolder.tv_price.setText("¥"+fuwuxiangmu.getPrice());
            myHolder.tv_price.setTextColor(Color.BLACK);
        }

    }

    /**
     * ViewHolder 通过构造方法中 实现具体view的绑定的方式 创建一个自实现绑定View的ViewHolder
     * Created by bailiangjin on 16/7/5.
     */
    public static class ViewHolder {
        public final View root;
        private TextView tv_name;
        private TextView tv_price;


        public ViewHolder(View root) {
            this.root = root;
            this.tv_name = (TextView) this.root.findViewById(R.id.tv_fuwuxiangmu_list_name);
            this.tv_price = (TextView) this.root.findViewById(R.id.tv_fuwuxiangmu_list_price);
        }
    }
}
