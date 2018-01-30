package com.sk.pda.parts.get.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.pda.R;
import com.sk.pda.parts.get.GetNewOrderActivity;
import com.sk.pda.parts.get.bean.GetOrderListBean;
import com.sk.pda.parts.get.sql.GetOrderListModelDao;

import java.util.List;


public class GetOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "getorderListadapter";


    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<GetOrderListBean> getOrderList;

    public GetOrderListAdapter(Context mContext, List<GetOrderListBean> getOrderBeanList) {
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.getOrderList = getOrderBeanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View order_view = mLayoutInflater.inflate(R.layout.item_get_order_list, null);
        return new GetOrderListViewHolder(order_view, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetOrderListViewHolder itemViewHolder = (GetOrderListViewHolder) holder;
        itemViewHolder.setData(getOrderList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return getOrderList.size();
    }

    private class GetOrderListViewHolder extends RecyclerView.ViewHolder {


        private Context mContext;
        private TextView tv_order_time;
        private TextView tv_need_time;
        private TextView tv_is_ordered;
        private TextView tv_order_db_name;
        private TextView tv_order_look;
        private TextView tv_order_del;

        public GetOrderListViewHolder(View itemView, final Context mContext) {
            super(itemView);
            this.mContext = mContext;
            tv_order_time = itemView.findViewById(R.id.item_get_order_time);
            tv_need_time = itemView.findViewById(R.id.item_get_order_need_time);
            tv_is_ordered = itemView.findViewById(R.id.item_get_order_is_order);
            tv_order_db_name = itemView.findViewById(R.id.item_get_order_db_name);
            tv_order_look = itemView.findViewById(R.id.item_get_order_look);
            tv_order_del = itemView.findViewById(R.id.item_get_order_del);
        }

        public void setData(final GetOrderListBean getOrderListBean, final int position) {
            tv_order_time.setText(getOrderListBean.getOrderTime());
            tv_need_time.setText(getOrderListBean.getNeedTime());
            if (getOrderListBean.getIsOrdered().toString().equals("1")) {
                tv_is_ordered.setText("已经收货");
            } else {
                tv_is_ordered.setText("暂存收货");
            }

            tv_order_db_name.setText(getOrderListBean.getOrderDbName());

            tv_order_look.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "查看" + position + tv_order_db_name.getText().toString(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, GetNewOrderActivity.class);
                    // TODO: 2018/1/13  窗口间传递数据：no,打开的订单号；view,是浏览过去的;edit,是否能编辑
                    intent.putExtra("no", tv_order_db_name.getText().toString());
                    intent.putExtra("view","isview");
                    intent.putExtra("edit","enable");

//                    intent.putExtra("currentTransType", getOrderListBean.getType());
                    mContext.startActivity(intent);
                }
            });

            tv_order_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //弹窗提示是否删除该数据库
                    final AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
                    mDialog.setTitle("提示");
                    mDialog.setMessage("确定删除本条数据?");
                    mDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //删除缓存数据库
                                    mContext.deleteDatabase(getOrderListBean.getOrderDbName());
                                    //从缓存列表中删除数据
                                    getOrderList.remove(position);
                                    int delFlag;
                                    delFlag = (new GetOrderListModelDao()).delSingleData(mContext, "getorderlist", getOrderListBean.getOrderDbName());
                                    if (delFlag>0)
                                    {
                                        //刷新列表
                                        notifyDataSetChanged();
                                        Toast.makeText(mContext, "删除成功",Toast.LENGTH_SHORT).show();
                                    }
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
                }
            });
        }
    }




}
