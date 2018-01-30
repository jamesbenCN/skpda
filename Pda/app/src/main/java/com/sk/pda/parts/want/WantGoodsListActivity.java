package com.sk.pda.parts.want;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.sk.pda.R;
import com.sk.pda.parts.want.base.BaseFragment;
import com.sk.pda.parts.want.fragment.ItemListFragment;
import com.sk.pda.parts.want.fragment.ItemHotFragment;

import java.util.ArrayList;
import java.util.List;

public class WantGoodsListActivity extends AppCompatActivity {
    private SegmentTabLayout segmentTabLayout;
    private FrameLayout fl_type;
    private List<BaseFragment> fragmentList;
    private Fragment tempFragment;
    public ItemListFragment listFragment;
    public ItemHotFragment hotTagFragment;
    public String currentTransType;
    private LinearLayout icon_broswe_ok;


    TextView tv_currentTransType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_goods_list);
        fl_type = (FrameLayout) findViewById(R.id.fl_type);

        segmentTabLayout = (SegmentTabLayout) findViewById(R.id.tl_1);
        icon_broswe_ok = (LinearLayout) findViewById(R.id.tv_good_list_ok);

        initData();

        //获取传过来的参数
        Intent intent = getIntent();
        currentTransType = intent.getStringExtra("currentTransType");
        Log.e("goodlistactivity", "当前类型为"+currentTransType );

        tv_currentTransType = (TextView) findViewById(R.id.tv_transtype);
        if(currentTransType.equals("DC")){
            tv_currentTransType.setText("配送");
        }else {
            tv_currentTransType.setText("直送");
        }



    }

    public void initData() {

        initFragment();

        String[] titles = { "热卖","全部"};

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


        icon_broswe_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
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
                    transaction.add(R.id.fl_type, nextFragment, "hotTagFragment").commit();
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

    private void initFragment() {
        fragmentList = new ArrayList<>();
        hotTagFragment = new ItemHotFragment();
        listFragment = new ItemListFragment();



        fragmentList.add(hotTagFragment);
        fragmentList.add(listFragment);

        switchFragment(tempFragment, fragmentList.get(0));
    }

    public String getCurrentTransType(){
        return this.currentTransType;
    }
}