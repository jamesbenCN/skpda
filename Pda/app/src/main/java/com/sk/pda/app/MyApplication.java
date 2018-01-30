package com.sk.pda.app;

import android.app.Application;
import android.content.Context;

import com.sk.pda.bean.DeptBean;
import com.sk.pda.bean.StoreBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/***
 * 整个软件
 */
public class MyApplication extends Application {

    //登录终端唯一识别码
    private String terminalId;

    //登录用户名
    private String userName;
    //用户码
    private String userCode;
    //当前门店BEAN
    private StoreBean currentStoreBean;
    private List<DeptBean> deptList;
    private List<StoreBean> storeList;

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;

        userName = "null";
        userCode = "null";

        deptList = new ArrayList<>();
        storeList = new ArrayList<>();
    }

    // 获取全局上下文
    public static Context getContext() {
        return mContext;
    }

    private void initOkhttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }


    public StoreBean getCurrentStoreBean() {
        return currentStoreBean;
    }

    public void setCurrentStoreBean(StoreBean currentStoreBean) {
        this.currentStoreBean = currentStoreBean;
    }


    public List<DeptBean> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<DeptBean> deptList) {
        this.deptList = deptList;
    }

    public List<StoreBean> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreBean> storeList) {
        this.storeList = storeList;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
