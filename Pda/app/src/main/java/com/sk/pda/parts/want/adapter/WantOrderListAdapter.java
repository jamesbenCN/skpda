package com.sk.pda.parts.want.adapter;

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
import com.sk.pda.parts.want.WantNewOrderActivity;
import com.sk.pda.parts.want.bean.WantOrderListBean;
import com.sk.pda.parts.want.sql.WantOrderListModelDao;

import java.util.List;


public class WantOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String TAG = "OrderListAdapter";
    private Context mContext;
    private List<WantOrderListBean> orderList;
    private final LayoutInflater mLayoutInflater;

    /**
     * 父类构造函数
     *
     * @param mContext  容器
     * @param orderlist 列表
     */
    public WantOrderListAdapter(Context mContext, List<WantOrderListBean> orderlist) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        orderList = orderlist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderlistViewHolder(mLayoutInflater.inflate(R.layout.item_want_order_list, null), mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderlistViewHolder orderlistViewHolder = (OrderlistViewHolder) holder;
        orderlistViewHolder.setData(orderList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderlistViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private TextView tv_order_time;
        private TextView tv_need_time;
        private TextView tv_is_ordered;
        private TextView tv_order_db_name;
        private TextView tv_order_look;
        private TextView tv_order_del;
        private TextView tv_order_no_label;
        private TextView tv_order_no;
        private TextView tv_order_count;
        private TextView tv_order_amount;

        public OrderlistViewHolder(View itemView, final Context mContext) {
            super(itemView);
            this.mContext = mContext;
            tv_order_time = itemView.findViewById(R.id.item_want_order_time);
            tv_need_time = itemView.findViewById(R.id.item_want_order_need_time);
            tv_is_ordered = itemView.findViewById(R.id.item_want_order_is_order);
            tv_order_db_name = itemView.findViewById(R.id.item_want_order_db_name);
            tv_order_no_label = itemView.findViewById(R.id.item_want_order_no_label);
            tv_order_no = itemView.findViewById(R.id.item_want_order_no);
            tv_order_count = itemView.findViewById(R.id.item_want_order_count);
            tv_order_amount = itemView.findViewById(R.id.item_want_order_amount);
            tv_order_look = itemView.findViewById(R.id.item_want_order_look);
            tv_order_del = itemView.findViewById(R.id.item_want_order_del);
        }



        public void setData(final WantOrderListBean orderListBean, final int position) {
            tv_order_time.setText(orderListBean.getOrderTime());
            tv_need_time.setText(orderListBean.getNeedTime());
            if (orderListBean.getIsOrdered().toString().equals("1")) {
                tv_is_ordered.setText("已定货");
                tv_order_no_label.setVisibility(View.VISIBLE);
                tv_order_no.setVisibility(View.VISIBLE);
                tv_order_no.setText(orderListBean.getNo());
            } else {
                tv_is_ordered.setText("暂存");
            }

            tv_order_count.setText(orderListBean.getCount());

            tv_order_amount.setText(orderListBean.getAmount());

            tv_order_db_name.setText(orderListBean.getOrderDbName());

            tv_order_look.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "查看" + position + tv_order_db_name.getText().toString(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, WantNewOrderActivity.class);
                    //从浏览来的
                    intent.putExtra("currentOrderDbName", tv_order_db_name.getText().toString());
                    intent.putExtra("currentTransType", orderListBean.getType());
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
                                    mContext.deleteDatabase(orderListBean.getOrderDbName());
                                    //从缓存列表中删除数据
                                    orderList.remove(position);
                                    int delFlag;
                                    delFlag = (new WantOrderListModelDao()).delSingleData(mContext, "orderlistdb", orderListBean.getOrderDbName());
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
