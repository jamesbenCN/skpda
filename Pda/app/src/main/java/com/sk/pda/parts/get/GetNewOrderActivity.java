package com.sk.pda.parts.get;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.pda.R;
import com.sk.pda.parts.get.adapter.GetOrderAdapter;
import com.sk.pda.parts.get.bean.GetItemBean;
import com.sk.pda.parts.get.sql.GetOrderModelDao;

import java.util.ArrayList;
import java.util.List;

public class GetNewOrderActivity extends Activity {
    String TAG = "getneworderactivity";

    //接受到的窗口传递的信息
    String no;
    String isview;

    TextView tv_order_title;
    TextView tv_get_no;
    ImageView iv_order_scan;
    TextView tv_order_close;
    TextView tv_order_submit;
    RecyclerView rv_order;
    List<GetItemBean> mData = new ArrayList<GetItemBean>();
    GetOrderAdapter getOrderAdapter;
    GridLayoutManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_new_order);
        findViews();
        initData();
        initListener();
    }

    private void findViews() {
        tv_order_title = findViewById(R.id.tv_title);
        tv_get_no = findViewById(R.id.tv_get_no);
        iv_order_scan = findViewById(R.id.iv_get_scan);
        rv_order = findViewById(R.id.rv_get_order);
        tv_order_close = findViewById(R.id.tv_get_order_close);
        tv_order_submit = findViewById(R.id.tv_get_order_submit);

    }

    private void initData() {
        //接受其他窗口传过来的数据
        no = (String) getIntent().getSerializableExtra("no");
        isview = (String) getIntent().getSerializableExtra("view");
        //加载数据
        mData = (new GetOrderModelDao()).queryData(GetNewOrderActivity.this, no, "");

        //设置单号栏
        tv_get_no.setText("单号：" + no);

        //设置receclyview
        getOrderAdapter = new GetOrderAdapter(this, mData);
        rv_order.setAdapter(getOrderAdapter);
        manager = new GridLayoutManager(this, 1);
        rv_order.setLayoutManager(manager);
    }


    private void initListener() {
        iv_order_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        tv_order_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savaData();
                finish();
            }
        });

        tv_order_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }


    /**
     * 打开搜索对话框
     */
    private void showInputDialog() {
        final EditText editText = new EditText(GetNewOrderActivity.this);
        //设置为数字键盘
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(GetNewOrderActivity.this);
        inputDialog.setTitle("请输入条形码").setView(editText);
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scrollToItem(editText.getText().toString());
            }
        });
        inputDialog.show();
    }



    /**
     * 滑动到指定位置
     * @param mRecyclerView
     * @param position
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));

        if (position < firstItem) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
        }
    }


    /**
     * 查找条形码并滚动到其位置
     * @param barcode
     */
    private void scrollToItem(String barcode) {
        int position = isHaveBean(barcode);
        if (position > 0) {
            smoothMoveToPosition(rv_order,position);

        }else {
            Toast.makeText(GetNewOrderActivity.this,"没有找到该商品",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 查找mdata中是否有这个barcode的bean，如果有的话，返回这个bean的position值,如果没有的话返回-1
     * @param barcode 要查找的barcode值
     * @return  position或者-1
     */
    private int isHaveBean(String barcode) {
        int code=-1;
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getBarcode().equals(barcode)) {
                    code=i;
                }
            }
            return code;
    }




//    /**
//     * 打开填写数量对话框
//     */
//    private void showInputNumDialog(final GetItemBean itemBean) {
//        LayoutInflater inflater = getLayoutInflater();
//        View layout = inflater.inflate(R.layout.dialog_get_num, (ViewGroup) findViewById(R.id.dialog));
//        //设置输入法为数字输入法
//        EditText et_num;
//        et_num = layout.findViewById(R.id.etnum);
//        et_num.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//        int index = isHaveBean(itemBean.getBarcode());
//        //如果收过
//        if (index != -1) {
//            Toast.makeText(GetNewOrderActivity.this, "已收过该商品", Toast.LENGTH_SHORT).show();
//            Log.e(TAG, "showInputNumDialog: " + itemBean.getNum());
//            et_num.setText(Float.toString(mData.get(index).getNum()));
//        }
//
//
//        new AlertDialog.Builder(this)
//                .setTitle("商品" + itemBean.getItemname() + "的收货数量为：")
//                .setView(layout)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (index != -1) {
//                            //如果收过
//                            mData.get(index).setNum(Float.parseFloat(et_num.getText().toString()));
//                        } else {
//                            //如果没有收过
//                            itemBean.setNum(Float.parseFloat(et_num.getText().toString()));
//                            mData.add(itemBean);
//                        }
//                        getOrderAdapter.notifyDataSetChanged();
//                    }
//                })
//                .setNegativeButton("取消", null)
//                .show();
//    }


    /**
     * 将本地数据进行检查上传到服务器
     */
    private void submitData() {
        //开始校验
        //1.校验单品收货数量
        for (GetItemBean f : mData) {
            if (f.getNum() > f.getQty()) {
                Toast.makeText(GetNewOrderActivity.this, "货物" + f.getItemname() + "收货数量大于收货单数量", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //2.校验数量
        int dbnum = (new GetOrderModelDao()).queryItemCount(GetNewOrderActivity.this, no);
        if (mData.size() < dbnum) {
            Toast.makeText(GetNewOrderActivity.this, "实际收货货物品种数量小于收货单品种数量", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    /**
     * 保存数据到数据库
     */
    private void savaData() {
        for (GetItemBean f : mData) {
            String barcode = f.getBarcode();
            int flag = (new GetOrderModelDao()).updateItemNum(GetNewOrderActivity.this, no, barcode, f.getNum());
            Log.e(TAG, "savaData: flag" + flag);
        }
    }


}
