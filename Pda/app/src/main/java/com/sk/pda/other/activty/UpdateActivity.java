package com.sk.pda.other.activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sk.pda.R;
import com.sk.pda.other.utils.UpdateManager;

import java.io.File;



public class UpdateActivity extends Activity{
    private TextView textView;
    public static int version,serverVersion;
    public static String versionName,serverVersionName,downloadResult;
    private Button btn;
    private ProgressBar proBar;
    public static receiveVersionHandler handler;
    private UpdateManager manager = UpdateManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_update);

        textView = (TextView) findViewById(R.id.textview_id);
        btn = (Button) findViewById(R.id.button_id);
        proBar=(ProgressBar)findViewById(R.id.progressBar_id);

        Context c = this;
        version = manager.getVersion(c);
        versionName = manager.getVersionName(c);

        textView.setText("当前版本号:"+version+"\n"+"当前版本名:"+versionName);

        handler = new receiveVersionHandler();

        //检查更新按钮点击事件
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.compareVersion(UpdateActivity.this);
            }
        });
    }


    public class receiveVersionHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            proBar.setProgress(msg.arg1);
            proBar.setVisibility(View.VISIBLE);
            textView.setText("下载进度："+msg.arg1);
            if(msg.arg1 == 100){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String path = Environment.getExternalStorageDirectory()+"/AndroidUpdateDemo.apk";
                intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
                startActivity(intent);
            }
            proBar.setVisibility(View.GONE);
        }

    }
}
