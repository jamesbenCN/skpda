package com.sk.pda.other.activty;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sk.pda.R;

public class HelpActivity extends Activity {

    private WebView myWebView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_help);

        //初始化浏览器
        initWebView();
    }

    /**
     * 初始化浏览器
     */
    private void initWebView(){
        myWebView = (WebView) findViewById(R.id.wb_help);

        //浏览器设置在APP内打开
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        myWebView.setWebViewClient(new WebViewClient());

        //WebView加载web资源，帮助文档地址
        myWebView.loadUrl("http://baidu.com");


    }
}
