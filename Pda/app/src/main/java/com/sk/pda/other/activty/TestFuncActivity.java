package com.sk.pda.other.activty;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.barcodescandemo.ScannerInerface;
import com.sk.pda.R;
import com.sk.pda.utils.Unitl;

import zxing.android.CaptureActivity;


public class TestFuncActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView scanResultTV;
    public Button scanButton;
    public Button camScanButton;
    public Button handsetinfoButton;
    public Button testpageButton;

    //摄像头扫描用
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;

    //红外线扫描器用广播接收器和意图过滤器
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;

    //实例化一个红外线扫描器接口
    ScannerInerface Control1 = new ScannerInerface(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testfunc);

        //绑定扫描结果文本框
        scanResultTV = (TextView) findViewById(R.id.TextViewScanResult);

        //绑定按钮
        scanButton = (Button) findViewById(R.id.buttonScan);
        scanButton.setOnClickListener(this);
        camScanButton = (Button) findViewById(R.id.buttoncamscan);
        camScanButton.setOnClickListener(this);
        handsetinfoButton = (Button) findViewById(R.id.buttonhandsetinfo);
        handsetinfoButton.setOnClickListener(this);


        testpageButton = (Button) findViewById(R.id.buttontestpage);
        testpageButton.setOnClickListener(this);


        //打开红外线扫描器接口
        Control1.open();
        //设置红外线扫描器发出Beep声
        Control1.enablePlayBeep(true);
        //设置输出模式
        Control1.setOutputMode(1);
        //解锁扫描键
        Control1.lockScanKey();

        //定义意图过滤器
        mFilter = new IntentFilter("android.intent.action.SCANRESULT");

        //定义广播接收器
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //接受扫码结果并赋值
                final String scanResult = intent.getStringExtra("value");
                int scanLen = intent.getIntExtra("length", 0);
                int type = intent.getIntExtra("type", 0);

                //结果处理
                scanResultTV.setText(scanResult);

                Log.i("【扫码结果】", "条码值：" + scanResult + "\n" + "条码长度：" + scanLen + "\n" + "类型：" + type);
            }
        };

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan:
                LaserScan(v);
                Log.e("fuck", "onClick:laserscan ");
                break;

            case R.id.buttoncamscan:
                CamScan();
                Log.e("fuck", "onClick:camscan ");
                break;

            case R.id.buttonhandsetinfo:
                GetHandSetInfo();
                Log.e("fuck", "onClick: ");
                break;

            case R.id.buttontestpage:
                testpage();
                Log.e("fuck", "testpage ");
                break;


        }
    }

    /**
     * 获取设备信息
     */
    public void testpage() {

    }



    /**
     * 获取设备信息
     */
    public void GetHandSetInfo() {

        Toast.makeText(TestFuncActivity.this, Unitl.getHandSetInfo(), Toast.LENGTH_SHORT).show();
    }


    /**
     * 激光扫描
     *
     * @param v
     */
    public void LaserScan(View v) {
        Control1.scan_start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Control1.scan_stop();
    }

    /**
     * 摄像头扫描
     */
    public void CamScan() {
        Intent intent = new Intent(TestFuncActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                //结果处理
                scanResultTV.setText(content);
            }
        }
    }

    //窗体恢复
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mFilter);
    }

    //窗体暂停
    @Override
    protected void onPause() {
        this.unregisterReceiver(mReceiver);
        super.onPause();
    }

    //窗体销毁
    @Override
    protected void onDestroy() {
        mReceiver = null;
        mFilter = null;
        scanResultTV = null;
        Control1.close();
        super.onDestroy();
    }

}
