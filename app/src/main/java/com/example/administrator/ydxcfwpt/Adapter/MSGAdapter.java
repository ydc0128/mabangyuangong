package com.example.administrator.ydxcfwpt.Adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ydxcfwpt.Bean.PayMent;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/15.
 */

public class MSGAdapter extends MyBaseAdapter {
    private static final String TAG = "MSGAdapter";
    private Activity activity;

    public MSGAdapter(Activity activity, List dataList) {
        super(activity, dataList);
        this.activity = activity;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_msg;
    }

    @Override
    public Object getViewHolder(View rootView) {
        return new MSGAdapter.ViewHolder(rootView);
    }

    @Override
    public void setItemData(int position, Object dataItem, Object viewHolder) {
        //将holder 转为自己holder
        MSGAdapter.ViewHolder myHolder = (MSGAdapter.ViewHolder) viewHolder;
    }

    /**
     * ViewHolder 通过构造方法中 实现具体view的绑定的方式 创建一个自实现绑定View的ViewHolder
     * Created by bailiangjin on 16/7/5.
     */
    public static class ViewHolder {
        public final View root;
        private ImageView iv_xiaoxi;
        private TextView tv_xiaoxi;

        public ViewHolder(View root) {
            this.root = root;
            this.iv_xiaoxi = (ImageView) this.root.findViewById(R.id.iv_msg_msg);
            this.tv_xiaoxi = (TextView) this.root.findViewById(R.id.tv_msg_msg);

        }
    }
}
