package com.sk.pda.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sk.pda.R;
import com.sk.pda.base.fragment.BaseFragment;
import com.sk.pda.home.fragment.HomeFragment;
import com.sk.pda.other.fragment.OtherFragment;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {


    FrameLayout frameLayout;
    RadioButton rbHome;
    RadioButton rbOther;
    RadioGroup rgMain;

    private ArrayList<BaseFragment> fragments;
    private int position;
    private BaseFragment mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initFragment();
        initListener();
    }

    private void findView()
    {
        frameLayout = findViewById(R.id.frameLayout);
        rbHome = findViewById(R.id.rb_home);
        rbOther = findViewById(R.id.rb_other);
        rgMain = findViewById(R.id.rg_main);

    }


    /**
     * 初始化Fragment组
     */
    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new OtherFragment());
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        position = 0;
                        break;
                    case R.id.rb_other:
                        position = 1;
                        break;
                }
                //根据position选中目标显示fragment
                BaseFragment baseFragment = getFragment(position);
                switchFragment(mContext, baseFragment);
            }
        });
        //默认选中home
        rgMain.check(R.id.rb_home);
    }


    /**
     *根据Position值，获取目标fragment
     * @param position
     * @return
     */
    private BaseFragment getFragment(int position) {
        if (fragments != null && fragments.size() > 0) {
            BaseFragment baseFragment = fragments.get(position);
            return baseFragment;
        }
        return null;
    }

    /**跳转窗体函数
    * @param fromFragment 跳转起始fragment
     * @param nextFragment 跳转目标fragment
    * **/
    private void switchFragment(Fragment fromFragment, BaseFragment nextFragment) {
        if (mContext != nextFragment) {
            mContext = nextFragment;
            if (nextFragment != null) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //判断nextFragment是否添加
                if (!nextFragment.isAdded()) {
                    //隐藏当前Fragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.add(R.id.frameLayout, nextFragment).commit();
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
