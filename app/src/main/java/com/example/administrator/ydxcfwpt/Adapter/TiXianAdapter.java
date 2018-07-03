package com.example.administrator.ydxcfwpt.Adapter;

import android.app.Activity;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ydxcfwpt.Bean.Dingdan;
import com.example.administrator.ydxcfwpt.Bean.PayMent;
import com.example.administrator.ydxcfwpt.Contast.Contast;
import com.example.administrator.ydxcfwpt.R;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/22.
 */

public class TiXianAdapter extends MyBaseAdapter {
    @BindView(R.id.tv_mx_daozhangshijian)
    TextView tvMxDaozhangshijian;
    @BindView(R.id.tv_mx_yinhangka)
    TextView tvMxYinhangka;
    @BindView(R.id.tv_mx_tixianjine)
    TextView tvMxTixianjine;
    @BindView(R.id.tv_mx_tixianzhungtai)
    TextView tvMxTixianzhungtai;
    @BindView(R.id.tv_mx_yuannyin)
    TextView tvMxYuannyin;
    private Activity activity;
    private String mYear,mMonth,mDay;


    public TiXianAdapter(Activity activity, List dataList) {
        super(activity, dataList);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_tixianmingxi;
    }

    @Override
    public Object getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    @Override
    public void setItemData(int position, Object dataItem, Object viewHolder) {
        final PayMent payMent = (PayMent) dataItem;
        //将holder 转为自己holder
        ViewHolder myHolder = (ViewHolder) viewHolder;
        String str = payMent.getP_Time();
        String shijian = str.replace("T", "  ");
        String kahao = Contast.worker.getW_BankCard();
        String weihao = kahao.substring(kahao.length() - 4, kahao.length());
        myHolder.tvMxDaozhangshijian.setText(shijian);
        myHolder.tvMxYinhangka.setText(Contast.worker.getW_BankName() + "(" + weihao + ")");
        switch (payMent.getP_GetMoneyState()) {
            case 1:
                myHolder.tvMxTixianzhungtai.setText("审核中");
                myHolder.tvMxYuannyin.setVisibility(View.GONE);
                break;
            case 2:
                myHolder.tvMxTixianzhungtai.setText("已审核，待转账");
                myHolder.tvMxYuannyin.setVisibility(View.GONE);
                break;
            case 3:
                myHolder.tvMxTixianzhungtai.setText("已转账");
                myHolder.tvMxYuannyin.setVisibility(View.GONE);
                break;
            case 4:
                myHolder.tvMxTixianzhungtai.setText("未通过审核");
                myHolder.tvMxYuannyin.setVisibility(View.VISIBLE);
                myHolder.tvMxYuannyin.setText("" + payMent.getP_GetMoneyValue());
                break;
        }
        myHolder.tvMxTixianjine.setText("" + payMent.getP_PayPrice());
    }

    /**
     * ViewHolder 通过构造方法中 实现具体view的绑定的方式 创建一个自实现绑定View的ViewHolder
     * Created by bailiangjin on 16/7/5.
     */
    public static class ViewHolder {
        public final View root;
        private TextView tvMxDaozhangshijian;
        private TextView tvMxYinhangka;
        private TextView tvMxTixianzhungtai;
        private TextView tvMxTixianjine;
        private TextView tvMxYuannyin;

        public ViewHolder(View root) {
            this.root = root;
            this.tvMxDaozhangshijian = (TextView) this.root.findViewById(R.id.tv_mx_daozhangshijian);
            this.tvMxYinhangka = (TextView) this.root.findViewById(R.id.tv_mx_yinhangka);
            this.tvMxTixianjine = (TextView) this.root.findViewById(R.id.tv_mx_tixianjine);
            this.tvMxTixianzhungtai = (TextView) this.root.findViewById(R.id.tv_mx_tixianzhungtai);
            this.tvMxYuannyin = (TextView) this.root.findViewById(R.id.tv_mx_yuannyin);

        }
    }
}
