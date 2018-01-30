package com.sk.pda.parts.want;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.sk.pda.R;
import com.sk.pda.parts.want.base.BaseFragment;
import com.sk.pda.parts.want.fragment.DCListFragment;
import com.sk.pda.parts.want.fragment.DSListFragment;

import java.util.ArrayList;
import java.util.List;

public class WantOrderListActivity extends AppCompatActivity {
    FrameLayout fl_type;
    SegmentTabLayout segmentTabLayout;
    private List<BaseFragment> fragmentList;
    private Fragment tempFragment;
    DCListFragment allFragment;
    DSListFragment dsFragment;

    static final private int BROSWE_CODE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_order_list);
        fl_type = (FrameLayout) findViewById(R.id.fl_type);
        segmentTabLayout = (SegmentTabLayout) findViewById(R.id.tl_1);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        switchFragment(tempFragment, fragmentList.get(0));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //浏览过来的话就刷新窗体数据
        if (requestCode == BROSWE_CODE) {
            initData();
        }
    }


    public void initData() {
        initFragment();

        String[] titles = {"配送订单", "直送订单"};

        //设置标题
        segmentTabLayout.setTabData(titles);
        //设置热卖红点
        segmentTabLayout.showDot(1);

        segmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switchFragment(tempFragment, fragmentList.get(position));
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void initFragment() {
        fragmentList = new ArrayList<>();
        allFragment = new DCListFragment();
        dsFragment = new DSListFragment();

        fragmentList.add(allFragment);
        fragmentList.add(dsFragment);

        switchFragment(tempFragment, fragmentList.get(0));
    }

    public void switchFragment(Fragment fromFragment, BaseFragment nextFragment) {
        if (tempFragment != nextFragment) {
            tempFragment = nextFragment;
            if (nextFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //判断nextFragment是否添加
                if (!nextFragment.isAdded()) {
                    //隐藏当前Fragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.add(R.id.fl_type, nextFragment, "allFragment").commit();
                } else {
                    //隐藏当前Fragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.show(nextFragment).commit();
                }
            }
        }
    }


}
