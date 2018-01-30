package com.sk.pda.parts.check;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sk.pda.R;
import com.sk.pda.app.MyApplication;

public class CheckActivity extends Activity {

    TextView tvUserName;
    TextView tvShopName;
    LinearLayout icon_exit;
    LinearLayout icon_new_check;
    LinearLayout icon_check_list;

    String deptcode;
    String storecode;
    String perscode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_index);
        findViews();
        initData();
        initListener();
    }


    private void findViews(){
        tvUserName = findViewById(R.id.tv_username);
        tvShopName = findViewById(R.id.tv_storename);
        icon_exit = findViewById(R.id.icon_exit);
        icon_new_check = findViewById(R.id.icon_new_check);
        icon_check_list =  findViewById(R.id.icon_checkList);
    }

    private void initData(){
        MyApplication app = (MyApplication) getApplication();
        tvUserName.setText(app.getUserName());
        tvShopName.setText(app.getCurrentStoreBean().getStorename());

        deptcode = "";
        storecode = app.getCurrentStoreBean().getStorecode();
        perscode = app.getUserCode();

    }

    private void initListener(){
        icon_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
