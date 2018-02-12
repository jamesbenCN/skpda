package com.sk.pda.app;

import android.app.Application;
import android.content.Context;

import com.sk.pda.base.bean.DeptBean;
import com.sk.pda.base.bean.StoreBean;
import com.sk.pda.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/***
 * 整个软件
 */
public class MyApplication extends Application {

    //登录用户名
    private String userName;
    //用户码
    private String userCode;
    //部门列表
    private List<DeptBean> deptList;
    //商店列表
    private List<StoreBean> storeList;
    //当前门店BEAN
    private StoreBean currentStoreBean;
    //登录终端唯一识别码
    private String terminalId;



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
