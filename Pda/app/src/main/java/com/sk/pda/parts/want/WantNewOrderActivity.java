package com.sk.pda.parts.want;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.barcodescandemo.ScannerInerface;
import com.sk.pda.R;
import com.sk.pda.app.LoginActivity;
import com.sk.pda.app.MyApplication;
import com.sk.pda.parts.want.adapter.WantOrderAdapter;
import com.sk.pda.parts.want.adapter.TypeRightAdapter;
import com.sk.pda.bean.ItemBean;
import com.sk.pda.parts.want.bean.PostItemBean;
import com.sk.pda.parts.want.bean.WantOrderListBean;
import com.sk.pda.parts.want.sql.ItemModelDao;
import com.sk.pda.parts.want.sql.WantOrderListModelDao;
import com.sk.pda.parts.want.sql.WantOrderModelDao;
import com.sk.pda.utils.ACache;
import com.sk.pda.utils.CallWebService;
import com.sk.pda.utils.Constants;
import com.sk.pda.utils.PopupWindowHelper;
import com.sk.pda.utils.Unitl;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

import okhttp3.Call;
import zxing.android.CaptureActivity;


public class WantNewOrderActivity extends Activity {

    String TAG = "NewOrderActivity";

    //摄像头扫描用
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;

    //红外线扫描器用广播接收器和意图过滤器
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;

    //实例化一个红外线扫描器接口
    ScannerInerface Control1 = new ScannerInerface(this);

    boolean isOpen = false;

    ACache acache;


    private TextView want_tv_currentdb_name;
    private TextView want_tv_trans_type;

    private LinearLayout want_ll_find;
    private LinearLayout want_ll_scan;
    private LinearLayout want_ll_browse;

    private Button want_bt_search;
    private EditText want_et_search;
    private ImageView want_iv_scan;
    private ImageView want_iv_search_cancel;
    private LinearLayout want_ll_search;
    private String currentTransType;
    private String postNeedTime;

    private PopupWindowHelper popupWindowHelper;
    private View popView;
    private Button popwindowclose;
    private boolean windowIsOpen;
    private RecyclerView want_rv_search;
    private RecyclerView want_rv_cart;

    private TextView want_tv_close_order_activity;
    private TextView want_tv_submit_order;
    private TextView want_tv_need_time;

    private String currentOrderDbName;
    static final private int GET_CODE = 0;

    private ProgressDialog mPd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_new_order);


        //初始化弹窗
        popView = LayoutInflater.from(this).inflate(R.layout.popupview, null);
        popupWindowHelper = new PopupWindowHelper(popView);
        //初始化弹出窗口标志
        windowIsOpen = false;


        findViews();
        initData();
        initListener();
    }

    /**
     * 摄像头扫描返回的结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_CODE) {
            refreshCartRv();
        }
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                //结果处理
                findData(content);
            }
        }
    }


    private void findViews() {
        want_tv_currentdb_name = findViewById(R.id.want_tv_currentDbname);
        want_tv_need_time = findViewById(R.id.want_tv_need_time);

        want_tv_trans_type = findViewById(R.id.tv_transtype);
        want_rv_cart = findViewById(R.id.rv_want_new_order);

        //搜索按钮
        want_ll_browse = findViewById(R.id.icon_want_browse);
        want_ll_find = findViewById(R.id.icon_want_find);
        want_ll_search = findViewById(R.id.ll_want_search);
        want_et_search = findViewById(R.id.et_want_search);
        want_bt_search = findViewById(R.id.bt_want_search);
        want_iv_search_cancel = findViewById(R.id.iv_want_search_cancel);
        want_iv_scan = findViewById(R.id.iv_want_scan);

        //弹窗关闭按钮
        popwindowclose = findViewById(R.id.want_bt_popwindow_close);

        //容器关闭按钮
        want_tv_close_order_activity = findViewById(R.id.tv_close_order);

        //提交订单按钮
        want_tv_submit_order = findViewById(R.id.tv_submit_order);

    }

    private void initData() {

        //初始化缓存
        acache = ACache.get(this, "main");

        //获取传过来的参数
        Intent intent = getIntent();

        //获取系统时间
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String date = df.format(System.currentTimeMillis());

        //获得类型
        currentTransType = intent.getStringExtra("currentTransType");

        //判断是从哪里来的，并且初始化要设置的值
        currentOrderDbName = intent.getStringExtra("currentOrderDbName");

        //判断是否为新建的
        if (currentOrderDbName == null) {
            //如果是新建的
            //设置当前数据库名字
            currentOrderDbName = "order" + date;
            //并插入名字到数据库
            WantOrderListBean orderListBean = new WantOrderListBean();
            orderListBean.setOrderDbName(currentOrderDbName);
            orderListBean.setIsOrdered("0");
            if (currentTransType.toString().equals("DS")) {
                orderListBean.setType("DS");
            } else {
                orderListBean.setType("DC");
            }
            orderListBean.setOrderTime("");
            orderListBean.setNeedTime(want_tv_need_time.getText().toString());

            boolean insetFlag = (new WantOrderListModelDao()).insertOrderModelToDb(this, "orderlistdb", orderListBean);
            if (insetFlag) {
                //修改并更新当前orderDbName的缓存
                acache.remove("currentOrderDbName");
                acache.put("currentOrderDbName", currentOrderDbName);
            } else {
                Toast.makeText(WantNewOrderActivity.this, "Orderlist数据库插入失败", Toast.LENGTH_SHORT).show();
            }

        } else {
            currentOrderDbName = intent.getStringExtra("currentOrderDbName");
            Log.e(TAG, "浏览过来的");
        }

        //初始化默认要货时间
        Date date1 = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
        String needtime = sdf1.format(date1);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        postNeedTime = sdf2.format(date1);

        want_tv_need_time.setText(needtime);

        ///////////////////////////////////////////////////////////////////////////////

        //设置订单名栏的文字
        want_tv_currentdb_name.setText(currentOrderDbName);
        //设置要货模式文字内容
        if (currentTransType.equals("DC")) {
            want_tv_trans_type.setText("配送订单");
        } else {
            want_tv_trans_type.setText("直送订单");
        }

        //初始化更新数据进度对话框
        mPd = new ProgressDialog(this);
        mPd.setMessage("正在提交订单数据，请稍等");
        //设置是否可以取消
//        mPd.setCancelable(false);
//        mPd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                Toast.makeText(WantNewOrderActivity.this, "提交被取消", Toast.LENGTH_SHORT).show();
//            }
//        });

        //刷新视图
        refreshCartRv();
    }

    ///修改时间栏///////////////////////////////////////////////////////////////////////////////////////
    private void showDataDiaglog() {
        // 获取当前系统时间
        Calendar calendar = Calendar.getInstance();
        // 获取当前的年
        int year = calendar.get(calendar.YEAR);
        // 获取当前的月
        int month = calendar.get(calendar.MONTH);
        // 获取当前月的第几天
        int day = calendar.get(calendar.DAY_OF_MONTH);
        // 获取当前周的第几天
        // int day = calendar.get(calendar.DAY_OF_WEEK);
        // 获取当前年的第几天
        // int day = calendar.get(calendar.DAY_OF_YEAR);
        // 参数1：上下文
        // 参数2：年
        // 参数3：月
        // 参数4：：日(ps:参数2、3、4是默认时间,月是从0开始的)
        final DatePickerDialog dialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog1) {
                //设置按钮颜色为红色
                dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(android.graphics.Color.RED);
                dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(android.graphics.Color.RED);
            }
        });
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        // 参数1：触发事件
        // 参数2：年
        // 参数3：月
        // 参数4：：日(ps:参数2、3、4是显示的时间)
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String needtime = Integer.toString(year) + "年"
                    + String.format("%02d", monthOfYear + 1) + "月"
                    + String.format("%02d", dayOfMonth) + "日";

            postNeedTime = Integer.toString(year)
                    + String.format("%02d", monthOfYear + 1)
                    + String.format("%02d", dayOfMonth);
            want_tv_need_time.setText(needtime);
//            int needTimeFlag = (new OrderListModelDao()).updateSingleDataOrderOrderTime(NewOrderActivity.this,"orderlistdb",currentOrderDbName,want_tv_need_time.getText().toString());
//            if(needTimeFlag>0){
//                Toast.makeText(NewOrderActivity.this, "要货时间修改成功", Toast.LENGTH_SHORT).show();
//            }
        }
    };
///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 提交订单
     */
    private void submitOrder() {


        //检查数据
        String needTime = want_tv_need_time.getText().toString();
        String orderNum = Integer.toString(want_rv_cart.getAdapter().getItemCount());

        if (want_rv_cart.getAdapter().getItemCount() > 0) {
            //弹出对话框要求确认

            final AlertDialog.Builder mDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
            mDialog.setTitle("提示");
            mDialog.setMessage("确定提交本要货订单吗？共有" + orderNum + "款商品，要货时间为" + needTime);
            mDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            postData();
                        }
                    });
            mDialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            // 显示
            mDialog.show();
        } else {
            Toast.makeText(WantNewOrderActivity.this, "本订单内商品数量为0", Toast.LENGTH_SHORT).show();
        }


    }




    ////////////////////////////////////////////////////////////////////////////////

    private void initListener() {
        //要货订单要货时间
        want_tv_need_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataDiaglog();
            }
        });

        //提交订单
        want_tv_submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder();

            }
        });

        //浏览图标
        want_ll_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WantNewOrderActivity.this, WantGoodsListActivity.class);
                intent.putExtra("currentTransType", currentTransType);
                startActivityForResult(intent, GET_CODE);
            }
        });

        //搜索图标
        want_ll_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    isOpen = false;
                    want_ll_search.setVisibility(View.GONE);
                    closeOpenInput();
                } else {
                    isOpen = true;
                    want_ll_search.setVisibility(View.VISIBLE);
                    //打开键盘
                    closeOpenInput();
                    want_et_search.requestFocus();
                }
            }
        });

        //文字搜索栏设置
        want_et_search.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        //设置软键盘回车键为"搜索键"
        want_et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //设置点击为全选
        want_et_search.setSelectAllOnFocus(true);
        //设置EditText的按键监听
        want_et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //如果是回车键，进行搜索
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //搜索
                    search();
                }
                return false;
            }
        });


        //清空搜索栏文字按钮
        want_iv_search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                want_et_search.setText("");
            }
        });

        //搜索按钮
        want_bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        ////红外线扫描////////////////////////////////////////////////////////////////
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

                //结果在数据库中处理
                findData(scanResult);
            }
        };
        ////红外线扫描////////////////////////////////////////////////////////////////
        popView.setFocusable(true);

        //弹窗关闭
        popView.findViewById(R.id.want_bt_popwindow_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowHelper.dismiss();
                refreshCartRv();
            }
        });

        //扫描按钮
        want_iv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CamScan();
            }
        });

        //关闭窗体
        want_tv_close_order_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void search() {
        //搜索
        String keyword = want_et_search.getText().toString();
        if (keyword.equals("")) {
            Toast.makeText(WantNewOrderActivity.this, "请输入搜索关键词", Toast.LENGTH_SHORT).show();
        } else {
            findData(keyword);
            //点击搜索后清空搜索栏文字
            want_et_search.setText("");
        }
    }

    /**
     * 查找关键字
     *
     * @param keyword 关键字
     */
    private void findData(String keyword) {

        if (windowIsOpen) {
            popupWindowHelper.dismiss();
        }
        //从缓存中取得数据库名字
        String dbName = acache.getAsString("wantdb");
        ItemModelDao itemModel = new ItemModelDao();

        boolean isNum = false;
        List<ItemBean> searchResult;
        if (keyword == null) {
            //点击结束后，不会执行任何操作。。。不能动
            //Toast.makeText(NewOrderActivity.this, "查找的字符串有误", Toast.LENGTH_SHORT).show();
        } else {
            isNum = Unitl.isNumeric(keyword);
            if (isNum) {
                //从数据库中使用条形码查找
                searchResult = itemModel.queryData("barcode", keyword);
            } else {
                //从数据库中使用名称查找
                searchResult = itemModel.queryData("name", keyword);
            }

            //如果返回的数据，
            if (searchResult == null || searchResult.size() == 0) {
                Toast.makeText(WantNewOrderActivity.this, "没有找到您搜索的数据", Toast.LENGTH_SHORT).show();
            } else {
                //关闭输入法
                closeOpenInput();
                TypeRightAdapter searchresultadapter = new TypeRightAdapter(this, searchResult);
                want_rv_search = popView.findViewById(R.id.rv_want_search_result);
                //设置recycleview动画刷新时间为0，解决频闪
//                want_rv_search.getItemAnimator().setChangeDuration(0);
                want_rv_search.setAdapter(searchresultadapter);

                //弹出居中的窗口
                popupWindowHelper.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

                //设置弹出窗口标志
                windowIsOpen = true;
                //常用分类设置为每行1个
                GridLayoutManager manager = new GridLayoutManager(this, 1);
                //并设置布局
                want_rv_search.setLayoutManager(manager);

            }
        }
    }

    /**
     * 刷新主RececlyView
     */
    private void refreshCartRv() {
        List<ItemBean> orderlist = new ArrayList<>();
        Log.e("xxx", "currentOrderDbName " + currentOrderDbName);
        orderlist = (new WantOrderModelDao()).queryData(this, currentOrderDbName, "all", null);
        WantOrderAdapter orderAdapter = new WantOrderAdapter(this, orderlist, currentOrderDbName);
        want_rv_cart.setAdapter(orderAdapter);
        //常用分类设置为每行1个
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        //并设置布局
        want_rv_cart.setLayoutManager(manager);
        Toast.makeText(WantNewOrderActivity.this, "进行了刷新", Toast.LENGTH_SHORT).show();

    }

    private void postData() {
        mPd.show();
        MyApplication app = (MyApplication) getApplication();

        String deptCode;
        String storeCode;
        String persCode;
        String needTime;
        String hTNo;
        List<PostItemBean> postItemBeanList;
        postItemBeanList =new ArrayList<>();

        //// TODO: 2018/1/24 需要修改为部门码
        deptCode =app.getDeptList().get(0).getDeptcode();
        storeCode =app.getCurrentStoreBean().getStorecode();
        persCode =app.getUserCode();
        needTime = postNeedTime;
        hTNo =app.getTerminalId();

        List<ItemBean> orderlist = new ArrayList<>();
        orderlist = (new WantOrderModelDao()).queryData(this, currentOrderDbName, "all", null);
        for (ItemBean f:orderlist){
            PostItemBean postItemBean=new PostItemBean();
            postItemBean.setItemCode(f.getItemcode());
            postItemBean.setQty(Double.parseDouble(f.getQty()));
            if(currentTransType.equals("DC")){
                postItemBean.setVendCode("0001");
            }else{
                postItemBean.setVendCode(f.getVendcode());
            }

            postItemBeanList.add(postItemBean);
        }

        sendMarketPo(deptCode,storeCode,persCode,needTime,hTNo,postItemBeanList);

    }

    public void sendMarketPo(String deptCode, String storeCode, String persCode, String needTime, String hTNo, List<PostItemBean> postItemBeanList) {
        String method_name = "sendMarketPo";
        String name_space = Constants.WEBSERVICE_NAME_SPACE;
        String wdsl_link = Constants.WEBSERVICE_WDSL_LINK;
        Thread thr = new Thread() {
            public void run() {
                try {
                    JSONObject jsonObject=new  JSONObject();

                    jsonObject.put("deptCode",deptCode);
                    jsonObject.put("storeCode",storeCode);
                    jsonObject.put("persCode",persCode);
                    jsonObject.put("needTime",needTime);
                    jsonObject.put("hTNo",hTNo);
                    jsonObject.put("itemList",postItemBeanList);

                    String jsonStr=jsonObject.toString();

                    SoapObject request = new SoapObject(name_space, method_name);
                    Log.e("xxx", "run: "+ jsonStr );
                    request.addProperty("jsonRequest",jsonStr);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.bodyOut = request;
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE ht = new HttpTransportSE(wdsl_link);

                    ht.call("", envelope);

                    String ret = String.valueOf(envelope.getResponse());

                    Log.d("resultStr = ", ret);
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
    android.os.Handler handler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x000) {
                String jsonstr = (String) msg.obj;
                handlemsg(jsonstr);
            }
            if (msg.what == 0x001) {
                Toast.makeText(WantNewOrderActivity.this, "服务器发生SoapFault错误", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 0x002) {
                Toast.makeText(WantNewOrderActivity.this, "服务器发生IOException错误", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 0x003) {
                Toast.makeText(WantNewOrderActivity.this, "服务器发生XmlPullParser错误", Toast.LENGTH_SHORT).show();
            }
            mPd.cancel();
        }
    };


    private void handlemsg(String jsonstr){
        JSONObject dataJson = JSON.parseObject(jsonstr);
        String code = dataJson.getString("code");
        String msg = dataJson.getString("msg");
        String marketPoNo = dataJson.getString("marketPoNo");
        mPd.cancel();
        if (code.equals("200")) {
            Log.e(TAG, "marketPoNo: "+marketPoNo );
            Toast.makeText(WantNewOrderActivity.this,"要货成功,服务器返回单号" + marketPoNo,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(WantNewOrderActivity.this,"要货失败,服务器返回失败原因" + msg,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 关闭输入法
     */
    private void closeOpenInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    /**
     * 摄像头扫描
     */
    public void CamScan() {
        Log.e(TAG, "CamScan: " + Unitl.hasBackFacingCamera());
        if (!Unitl.hasBackFacingCamera()) {
            Toast.makeText(WantNewOrderActivity.this, "您的手持设备没有摄像头", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(WantNewOrderActivity.this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        }
    }


    /**
     * 恢复窗体
     */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mFilter);
    }

    /**
     * 暂停窗体
     */
    @Override
    protected void onPause() {
        this.unregisterReceiver(mReceiver);
        super.onPause();
    }

    /**
     * 销毁窗体
     */
    @Override
    protected void onDestroy() {
        mReceiver = null;
        mFilter = null;
        Control1.close();
        super.onDestroy();
        checkZeroData();
    }

    /**
     * 检查是否为零order的操作
     */
    private void checkZeroData() {
        List<ItemBean> orderlist = new ArrayList<>();
        orderlist = (new WantOrderModelDao()).queryData(this, currentOrderDbName, "all", null);
        if (orderlist.size() > 0) {
            Log.e(TAG, "没有执行任何操作");
            return;
        } else {
            Log.e(TAG, "执行了检查是否为零的程序");
            //删除当前缓存数据库
            this.deleteDatabase(currentOrderDbName);
            //并从列表数据中删除
            int delFlag = (new WantOrderListModelDao()).delSingleData(this, "orderlistdb", currentOrderDbName);
            if (delFlag > 0) {
                Log.e(TAG, "检查为零的程序操作结束");
            }
        }
    }


    /**
     * 屏蔽back键
     *
     * @param keyCode 键码
     * @param event   事件
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;//不执行父类点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}
