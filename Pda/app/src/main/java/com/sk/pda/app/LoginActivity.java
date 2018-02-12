package com.sk.pda.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sk.pda.R;
import com.sk.pda.base.bean.DeptBean;
import com.sk.pda.base.bean.StoreBean;
import com.sk.pda.other.downdb.DbGetActivity;
import com.sk.pda.base.sql.DeptModelDao;
import com.sk.pda.base.sql.StoreModelDao;
import com.sk.pda.utils.Constants;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoginActivity extends Activity {

    private EditText et_username;
    private EditText et_passwd;
    private CheckBox cb_remember;


    /**
     * 1.定义一个共享参数(存放数据方便的api)
     */
    private SharedPreferences sp;
    private Button bt_login;
    private Button bt_clean;
    private ProgressDialog mPd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 2.通过上下文得到一个共享参数的实例对象
        sp = this.getSharedPreferences("config", this.MODE_PRIVATE);

        //设置元素
        et_username = (EditText) findViewById(R.id.et_username);
        et_passwd = (EditText) findViewById(R.id.et_password);
        cb_remember = (CheckBox) findViewById(R.id.cB_remmber);
        bt_login = (Button) findViewById(R.id.btn_userlogin);
        bt_clean = (Button) findViewById(R.id.btn_clean);

        mPd = new ProgressDialog(this);
        mPd.setMessage("正在登陆");
        mPd.setCancelable(false);
        restoreInfo();

    }

    /**
     * 从sp文件当中读取信息
     */
    private void restoreInfo() {
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");
        String remember = sp.getString("remember", "");
        if (remember.equals("1")) {
            cb_remember.setChecked(true);
            et_username.setText(username);
            et_passwd.setText(password);
        }
    }

    public void cleanInfo(View view) {
        SharedPreferences.Editor editor = sp.edit();
        et_username.setText("");
        et_passwd.setText("");
        //清空sp数据
        editor.remove("username");
        editor.remove("password");
        editor.apply();
        //用户名框获得焦点
        et_username.setFocusable(true);
        et_username.setFocusableInTouchMode(true);
        et_username.requestFocus();
        et_username.findFocus();
    }

    /**
     * 登录按钮的点击事件
     *
     * @param view
     */
    public void login(View view) {
        final String username = et_username.getText().toString().trim();
        final String password = et_passwd.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 判断是否需要记录用户名和密码
        if (cb_remember.isChecked()) {
            // 被选中状态，需要记录用户名和密码
            // 3.将数据保存到sp文件中
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putString("remember", "1");
            editor.apply();// 提交数据，类似关闭流，事务
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("username");
            editor.remove("password");
            editor.putString("remember", "0");
            editor.apply();
        }

        bt_login.setEnabled(false);
        mPd.show();

        //开启登录
        userVaild(username, password);

    }

    public void userVaild(String userId, String userPsw) {
        String method_name = "userVaild";
        String name_space = Constants.WEBSERVICE_NAME_SPACE;
        String wdsl_link = Constants.WEBSERVICE_WDSL_LINK;
        Thread thr = new Thread() {
            public void run() {
                Looper.prepare();
                try {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("userId", userId);
                    jsonObject.put("userPsw", userPsw);

                    String jsonStr = jsonObject.toString();

                    SoapObject request = new SoapObject(name_space, method_name);
                    Log.e("xxx", "run: " + jsonStr);
                    request.addProperty("jsonRequest", jsonStr);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.bodyOut = request;
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE ht = new HttpTransportSE(wdsl_link);

                    ht.call("", envelope);
                    String ret = String.valueOf(envelope.getResponse());
                    //处理收到的数据
                    Message msg = handler.obtainMessage();
                    msg.what = 0x000;
                    msg.obj = ret;
                    handler.sendMessage(msg);


                } catch (SoapFault e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0x001);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0x002);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0x003);
                }

            }
        };
        thr.start();
    }


    /**
     * 处理器UI界面
     */
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x000) {
                String jsonstr = (String) msg.obj;
                handlemsg(jsonstr);
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 0x001) {
                Toast.makeText(LoginActivity.this, "服务器发生SoapFault错误", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 0x002) {
                Toast.makeText(LoginActivity.this, "服务器发生IOException错误", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 0x003) {
                Toast.makeText(LoginActivity.this, "服务器发生XmlPullParser错误", Toast.LENGTH_SHORT).show();
            }
            bt_login.setEnabled(true);
            mPd.cancel();
        }
    };


    /**
     * 处理收到的json数据
     *
     * @param json
     */
    private void handlemsg(String json) {
        JSONObject dataJson = JSON.parseObject(json);
        String code = dataJson.getString("code");
        mPd.cancel();
        if (code.equals("200")) {
            String usercode = dataJson.getString("userCode");
            String username = dataJson.getString("userName");
            String deptjson = dataJson.getString("deptCodeList");
            String storejson = dataJson.getString("storeCodeList");
            loginsuccess(usercode, username, deptjson, storejson);
        } else {
            loginfail();
        }
    }


    /**
     * 登录成功后处理
     */
    private void loginsuccess(String usercode, String username, String deptjson, String storejson) {
        //全局变量中设置名称和门店
        MyApplication app = (MyApplication) getApplication();
        app.setUserCode(usercode);
        app.setUserName(username);
        app.setTerminalId(terminalId());

        //获取门店数据
        List<StoreBean> storeBeanList = new ArrayList<StoreBean>();
        storeBeanList = JSON.parseArray(storejson, StoreBean.class);
        Log.e("网上找到门店数量", "共有" + storeBeanList.size() + "条数据");

        //设置本地通配数据库地址
        Constants.setLocalDb(Constants.LOCAL_DB_PATH + storeBeanList.get(0).getStorecode() + ".zip");
        //设置本地通配数据库地址
        Constants.setRemoteDb(Constants.REMOTE_DB_PATH + storeBeanList.get(0).getStorecode() + ".zip");

        //检查本地是否有该门店的通用数据库，如果没有，打开下载窗口
        File findDb = new File(Constants.getLocalDb());
        if(!findDb.exists()){
            startActivity(new Intent(LoginActivity.this,DbGetActivity.class));
            return;
        }


        List<StoreBean> localStoreBeanList = (new StoreModelDao()).transToLocalStore(storeBeanList);
        //添加门店数据
        app.setStoreList(localStoreBeanList);
        //设置默认门店
        app.setCurrentStoreBean(app.getStoreList().get(0));



        //获取部门数据
        List<DeptBean> deptBeanList = new ArrayList<DeptBean>();
        deptBeanList = JSON.parseArray(deptjson, DeptBean.class);
        Log.e("网上找到部门数量", "共有: " + deptBeanList.size());
        List<DeptBean> localDeptBeanList = (new DeptModelDao()).transToLocalDept(deptBeanList);
        Log.e("部门数量", "共有: " + localDeptBeanList.size());
        //添加部门数据
        app.setDeptList(localDeptBeanList);

        if (findDb.exists()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "账号密码正确,请花费1分钟时间更新您的通配数据库", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, DbGetActivity.class);
            startActivity(intent);
            bt_login.setEnabled(true);
        }
    }

    /**
     * 登录失败后处理
     */
    private void loginfail() {
        Toast.makeText(LoginActivity.this, "账户或密码错误", Toast.LENGTH_SHORT).show();
        bt_login.setEnabled(true);
    }

    /**
     * 获取终端识别码
     */
    private String terminalId() {
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = TelephonyMgr.getDeviceId();
        String simSerialNumber = TelephonyMgr.getSimSerialNumber();
        String androidId = android.provider.Settings.Secure.getString(
                getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) imei.hashCode() << 32) | simSerialNumber.hashCode());
        String uniqueId;
        uniqueId = deviceUuid.toString();
        return uniqueId;
    }

}