package com.sk.pda.other.downdb;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.pda.R;
import com.sk.pda.app.MyApplication;
import com.sk.pda.utils.Constants;

import java.io.File;

public class DbGetActivity extends Activity {
    Button downloadBtn;
    Button exitBtn;

    ProgressBar progressBar;
    TextView percentTv;
    boolean btnFlag = true;
    String url = "";
    String localdb ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_get);
        initData();
        findView();
        initListener();
    }

    private void initData(){
        url = Constants.getRemoteDb();
        localdb= Constants.getLocalDb();
        Log.e("remotedb", url );
        Log.e("localdb", localdb );
    }

    private void findView() {
        downloadBtn = findViewById(R.id.main_btn_down1);
        exitBtn = findViewById(R.id.bt_exit);
        progressBar = findViewById(R.id.main_progress1);
        percentTv = findViewById(R.id.tv_percent);
    }

    private void initListener() {

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnFlag) {
                    btnFlag = false;
                    downloadBtn.setText("暂停");
                    DownloadManager.getInstance().download(url, new DownLoadObserver() {
                        @Override
                        public void onNext(DownloadInfo value) {
                            super.onNext(value);
                            percentTv.setText(Float.toString(value.getProgress() * 100 / value.getTotal()));
                            progressBar.setProgress((int) value.getProgress());
                            progressBar.setMax((int) value.getTotal());
                        }

                        @Override
                        public void onComplete() {
                            if (downloadInfo != null) {
                                dbtest(downloadInfo.getFileName());
                            }
                        }
                    });
                } else {
                    btnFlag = true;
                    downloadBtn.setText("开始");
                    DownloadManager.getInstance().cancel(url);
                }

            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void dbtest(String filename) {

        //校验
        SQLiteDatabase db = SQLiteDatabase.openDatabase(localdb, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cur = db.query("vend", null, null, null, null, null, null);
        String curCunt = Integer.toString(cur.getCount());
        cur.close();

        Cursor cur1 = db.query("item", null, null, null, null, null, null);
        String curCunt1 = Integer.toString(cur1.getCount());
        cur1.close();

        Cursor cur2 = db.query("barcode", null, null, null, null, null, null);
        String curCunt2 = Integer.toString(cur2.getCount());
        cur2.close();

        Cursor cur3 = db.query("vendprice", null, null, null, null, null, null);
        String curCunt3 = Integer.toString(cur3.getCount());
        cur3.close();

        db.close();

        if (Integer.parseInt(curCunt) > 0) {


            Toast.makeText(DbGetActivity.this, "数据库更新结束" +
                            "vend共" + curCunt + "条" +
                            "item共" + curCunt1 + "条" +
                            "barcode共" + curCunt2 + "条" +
                            "vendprice共" + curCunt3 + "条"
                    , Toast.LENGTH_SHORT).show();

            downloadBtn.setVisibility(View.GONE);
            exitBtn.setVisibility(View.VISIBLE);
        }
    }

}
