package com.sk.pda.message.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sk.pda.base.BaseFragment;

/**
 * Created by Administrator on 2017/10/30.
 */

public class MessageFragment extends BaseFragment{

    private TextView textView;

    @Override
    public View initView() {
        textView= new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("该功能暂未开放");
    }
}
