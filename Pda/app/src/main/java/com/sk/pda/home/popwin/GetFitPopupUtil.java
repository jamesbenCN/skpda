package com.sk.pda.home.popwin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sk.pda.R;
import com.sk.pda.parts.get.GetNewOrderActivity;
import com.sk.pda.parts.get.GetOrderListActivity;
import com.sk.pda.parts.get.bean.GetItemBean;
import com.sk.pda.parts.get.bean.GetNoBean;
import com.sk.pda.parts.get.bean.GetOrderListBean;
import com.sk.pda.parts.get.sql.GetOrderListModelDao;
import com.sk.pda.parts.get.sql.GetOrderModelDao;
import com.sk.pda.utils.popwinutils.DensityUtils;
import com.sk.pda.utils.popwinutils.FitPopupWindow;
import com.sk.pda.utils.popwinutils.ScreenUtils;
import com.sk.pda.utils.Constants;
import com.sk.pda.utils.Unitl;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class GetFitPopupUtil {

    private static final String TAG = "getfitpopuputil";
    private View contentView;
    private Activity context;
    private FitPopupWindow mPopupWindow;
    private LinearLayout icon_new_check;
    private ProgressDialog mPd;

    //默认数据设置
    private String currentType = "dc";
    private String currentNoUrl = "";
    private String currentDetailUrl = Constants.BASE_URL_GET_NO_DETAIL;

    private LinearLayout icon_peisong;
    private LinearLayout icon_zhisong;
    private LinearLayout icon_getorderlist;


    String deptcode;
    String storecode;
    String perscode;


    public GetFitPopupUtil(Activity context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.popwin_home_get, null);
        findViews(contentView);
        initListener();
    }

    private void findViews(View view) {
        icon_peisong = view.findViewById(R.id.icon_peisong_newGet);
        icon_zhisong = view.findViewById(R.id.icon_zhisong_newGet);
        icon_getorderlist = view.findViewById(R.id.icon_getOrderList);
    }

    private void initListener() {
        icon_peisong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = "dc";
                currentNoUrl = Constants.BASE_URL_GET_DC_GETNO;
                showInputDialog();
            }
        });

        icon_zhisong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = "ds";
                currentNoUrl = Constants.BASE_URL_GET_DS_GETNO;
                showInputDialog();
            }
        });

        icon_getorderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, GetOrderListActivity.class));
            }
        });

    }

    /**
     * 弹出自适应位置的popupwindow
     *
     * @param anchorView 目标view
     */
    public View showPopup(View anchorView) {
        if (mPopupWindow == null) {
            mPopupWindow = new FitPopupWindow(context,
                    ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(20),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
        mPopupWindow.setView(contentView, anchorView);
        mPopupWindow.show();
        return contentView;
    }


    /**
     * 打开搜索对话框
     */
    private void showInputDialog() {
        final EditText editText = new EditText(context);
        //设置为数字键盘
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(context);
        inputDialog.setTitle("请输入收货单后5位数").setView(editText);
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!Unitl.isNumeric(editText.getText().toString())) {
                    Toast.makeText(context, "输入字符有非数字，请输入收货单后5位数字", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(editText.getText().toString().length() == 5)) {
                    Toast.makeText(context, "输入的长度出错，请输入收货单后5位数字", Toast.LENGTH_SHORT).show();
                    return;
                }
                searchWord(editText.getText().toString());
            }
        });
        inputDialog.show();
    }

// TODO: 2018/1/13 ////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 进行查询操作
     */
    private void searchWord(String keyword) {
        mPd = new ProgressDialog(context);
        mPd.setMessage("正在查询");
        mPd.setCancelable(false);
        mPd.show();


        /**
         * 从网络查询信息
         */

        try {
            Log.e(TAG, "searchWord:URL 为" + currentNoUrl);
            OkHttpUtils
                    .get()
//                    .addParams("DeptCode", deptcode)
//                    .addParams("StoreCode", storecode)
//                    .addParams("PersCode", perscode)
//                    .addParams("QueryNoteStr", keyword)
                    .url(currentNoUrl)
                    .id(100)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(context, "网络出现故障error", Toast.LENGTH_SHORT).show();
                            mPd.cancel();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            mPd.cancel();
                            handlemsg(response);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            //发生错误处理
            Toast.makeText(context, "网络出现故障xxxxx", Toast.LENGTH_SHORT).show();
            mPd.cancel();
        }

    }


    /**
     * 处理返回的JSON消息
     *
     * @param json
     */
    private void handlemsg(String json) {
        JSONObject dataJson = JSON.parseObject(json);
        String code = dataJson.getString("code");
        String getNoteList = dataJson.getString("getNoteList");

        if (code.equals("200")) {
            List<GetNoBean> noteList = new ArrayList<>();
            noteList = JSON.parseArray(getNoteList, GetNoBean.class);
            queryNoSuccess(noteList);
        } else {
            queryNoFail();
        }
    }


    /**
     * 查询失败处理
     */
    private void queryNoFail() {
        Toast.makeText(context, "获取失败", Toast.LENGTH_SHORT).show();
    }


    /**
     * 查询成功处理
     *
     * @param notelist 查询的结果
     */
    private void queryNoSuccess(List<GetNoBean> notelist) {
        if (notelist.size() == 0) {
            Toast.makeText(context, "没有找到相关订单号", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "获取到" + notelist.size() + "条订单", Toast.LENGTH_SHORT).show();
            /**
             * 显示列表
             */
            String[] mString;
            mString = new String[notelist.size()];
            for (int i = 0; i < notelist.size(); i++) {
                mString[i] = notelist.get(i).getGetNoteNo();
            }

            AlertDialog.Builder listDialog = new AlertDialog.Builder(context);
            listDialog.setTitle("找到" + notelist.size() + "条收货单");
            listDialog.setItems(mString, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //打开订单
                    openNoDetail(mString[which]);
                }
            });
            listDialog.show();
        }
    }

// TODO: 2018/1/13 ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 打开订单
     *
     * @param openNo 要打开的订单号
     */
    private void openNoDetail(String openNo) {
        getNoDetailJson(openNo);
    }


    /**
     * 从网上拉取具体订单内容
     *
     * @param openNo
     */
    private void getNoDetailJson(String openNo) {
        mPd = new ProgressDialog(context);
        mPd.setMessage("正在查询");
        mPd.setCancelable(false);
        mPd.show();

        try {
            Log.e(TAG, "currentdetailurl为" + currentDetailUrl);
            OkHttpUtils
                    .post()
//                    .addParams("DeptCode", deptcode)
//                    .addParams("StoreCode", storecode)
//                    .addParams("PersCode", perscode)
//                    .addParams("QueryNoteNo", openNo)
                    .url(currentDetailUrl)
                    .id(100)
                    .build()
                    .execute(new GetDetailStringCallback());

        } catch (Exception e) {
            e.printStackTrace();
            //发生错误处理
            Toast.makeText(context, "网络出现故障", Toast.LENGTH_SHORT).show();
            mPd.cancel();
        }
    }


    /**
     * 处理拉取的订单中的具体信息
     */
    private class GetDetailStringCallback extends StringCallback {

        @Override
        public void onError(Call call, Exception e, int id) {
            mPd.cancel();
            Toast.makeText(context, "网络出现问题", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response, int id) {
            mPd.cancel();
            Log.e("xxx", "onResponse: " + response);
            JSONObject dataJson;
            String code = "";
            String getnoteno = "";
            String itemListStr = "";
            try {
                dataJson = JSON.parseObject(response);
                code = dataJson.getString("code");
                getnoteno = dataJson.getString("getNoteNo");
                itemListStr = dataJson.getString("itemList");
            } catch (Exception e) {
                Log.e(TAG, "onResponse: " + e.getMessage());
            }


            List<GetItemBean> itemBeanList = (new GetOrderModelDao()).addItemJsonObjectToList(itemListStr);
            //把拉取的订单信息存储为本地缓存数据
            boolean flag = (new GetOrderModelDao()).insertOrderModelToDb(context, getnoteno, itemBeanList);

            if (flag) {
                //如果存储成功
                GetOrderListBean getOrderListBean = new GetOrderListBean();
                getOrderListBean.setOrderDbName(getnoteno);

                boolean insertToListFlag;
                //插入记录到单号列表本地数据库
                insertToListFlag = (new GetOrderListModelDao()).insertOrderModelToDb(context, "getorderlist", getOrderListBean);
                if (insertToListFlag) {
                    Intent intent = new Intent(context, GetNewOrderActivity.class);

                    // TODO: 2018/1/13 窗口间传递数据
                    intent.putExtra("no", getnoteno);
                    context.startActivity(intent);
                }

            }
        }
    }


}
