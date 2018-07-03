package com.example.administrator.ydxcfwpt.Adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;


import com.example.administrator.ydxcfwpt.Bean.WChangyongdizhi;
import com.example.administrator.ydxcfwpt.R;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class ChongyongdizhiAdapter extends MyBaseAdapter {

    public ChongyongdizhiAdapter(Activity activity, List dataList) {
        super(activity, dataList);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_chongyongdizhi_listview;
    }

    @Override
    public Object getViewHolder(View rootView) {
        return new ChongyongdizhiAdapter.ViewHolder(rootView);
    }

    @Override
    public void setItemData(int position, Object dataItem, Object viewHolder) {
        final WChangyongdizhi dizhi = (WChangyongdizhi) dataItem;
        //将holder 转为自己holder
        ChongyongdizhiAdapter.ViewHolder myHolder = (ChongyongdizhiAdapter.ViewHolder) viewHolder;
        myHolder.tv_changyongdizhi.setText(dizhi.getAW_Adress());
        myHolder.tv_changyongdizhi_buchong.setText(dizhi.getAW_AdressWrite());
    }

    /**
     * ViewHolder 通过构造方法中 实现具体view的绑定的方式 创建一个自实现绑定View的ViewHolder
     * Created by bailiangjin on 16/7/5.
     */
    public static class ViewHolder {
        public final View root;
        private TextView tv_changyongdizhi;
        private TextView tv_changyongdizhi_buchong;


        public ViewHolder(View root) {
            this.root = root;
            this.tv_changyongdizhi = (TextView) this.root.findViewById(R.id.tv_item_changyongdizhi);
            this.tv_changyongdizhi_buchong = (TextView) this.root.findViewById(R.id.tv_item_changyongdizhi_buchong);
        }
    }
}
