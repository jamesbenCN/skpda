package com.sk.pda.parts.want.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.pda.R;
import com.sk.pda.base.bean.ItemBean;
import com.sk.pda.parts.want.sql.WantOrderModelDao;
import com.sk.pda.utils.ACache;

import java.util.List;


public class WantOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;

    private String TAG = "OrderAdapter";

    /**
     * 商品列表数据
     */
    private List<ItemBean> orderList;
    private final LayoutInflater mLayoutInflater;
    private String dbname;
    private TextView amount;
    private TextView count;

    /**
     * 继承父类构造函数
     *
     * @param mContext      窗体上下文
     * @param orderBeanList 普通数据
     */
    public WantOrderAdapter(Context mContext, List<ItemBean> orderBeanList, String dbName,TextView amount,TextView count) {
        this.mContext = mContext;
        this.dbname = dbName;
        this.amount =amount;
        this.count = count;
        mLayoutInflater = LayoutInflater.from(mContext);
        orderList = orderBeanList;
        refreshAmountCount();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderViewHolder(mLayoutInflater.inflate(R.layout.item_want_order_cart, null), mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderViewHolder ordinaryViewHolder = (OrderViewHolder) holder;
        ordinaryViewHolder.setData(orderList.get(position), position);
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private ImageView iv_ordinary_right;
        private TextView tv_ordinary_name;
        private TextView tv_ordinary_no;
        private TextView tv_ordinary_id;
        private TextView tv_ordinary_price;
        private TextView tv_ordinary_min_num;
        private TextView tv_ordinary_max_num;
        private EditText et_ordinary_num;
        private TextView tv_ordinary_numAdd;
        private TextView tv_ordinary_numSub;
        private TextView tv_oridinary_spec;
        private TextView tv_oridinary_unit;
        private TextView tv_ordinary_confirmProduct;
        private TextView tv_ordinary_delProduct;



        public OrderViewHolder(View itemView, final Context mContext) {
            super(itemView);
            this.mContext = mContext;
            iv_ordinary_right = itemView.findViewById(R.id.want_iv_right);
            tv_ordinary_name = itemView.findViewById(R.id.want_tv_right_name);
            tv_ordinary_no = itemView.findViewById(R.id.want_tv_right_no);
            tv_ordinary_id = itemView.findViewById(R.id.want_tv_right_id);
            tv_ordinary_price = itemView.findViewById(R.id.want_tv_right_price);
            et_ordinary_num = itemView.findViewById(R.id.want_et_item_num);
            tv_ordinary_numAdd = itemView.findViewById(R.id.want_tv_item_numAdd);
            tv_ordinary_numSub = itemView.findViewById(R.id.want_tv_item_numSub);
            tv_ordinary_min_num = itemView.findViewById(R.id.want_tv_right_min);
            tv_ordinary_max_num = itemView.findViewById(R.id.want_tv_right_max);
            tv_oridinary_spec = itemView.findViewById(R.id.want_tv_right_spec);
            tv_oridinary_unit = itemView.findViewById(R.id.want_tv_right_unit);
            tv_ordinary_confirmProduct = itemView.findViewById(R.id.want_tv_item_confirmProduct);
            tv_ordinary_delProduct = itemView.findViewById(R.id.want_tv_item_delProduct);
        }

        public void setData(final ItemBean orderBean, final int position) {

            //加载图片
//            Glide.with(mContext)
//                    .load(Constants.BASE_URl_IMAGE + orderBean.getFigure())
//                    .into(iv_ordinary_right);
            //设置名称
            tv_ordinary_name.setText(orderBean.getItemname());
            tv_ordinary_no.setText(orderBean.getBarcode());
            tv_ordinary_id.setText(orderBean.getItemcode());
            tv_ordinary_price.setText(orderBean.getPurprice());
            et_ordinary_num.setText(orderBean.getQty());

            tv_ordinary_min_num.setText(orderBean.getMinmpoqty());
            tv_oridinary_spec.setText(orderBean.getCapacity());
            tv_oridinary_unit.setText(orderBean.getStockunit());

            //设置EditText打开数字键盘
            et_ordinary_num.setInputType(InputType.TYPE_CLASS_NUMBER);//输入类型
            //可以点击
            et_ordinary_num.setFocusableInTouchMode(true);
            //可以获取焦点
            et_ordinary_num.setFocusable(true);
            //打开后全选编辑栏
            et_ordinary_num.setSelectAllOnFocus(true);

            //增加
            tv_ordinary_numAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.parseInt(et_ordinary_num.getText().toString());
                    int newnum;
                    if (Integer.parseInt(orderBean.getMinmpoqty()) > 0) {
                        newnum = num + Integer.parseInt(orderBean.getMinmpoqty());
                    } else {
                        newnum = num + 1;
                    }
                    et_ordinary_num.setText(Integer.toString(newnum));
                    orderBean.setQty(et_ordinary_num.getText().toString());
                }
            });

            //删除
            tv_ordinary_numSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.parseInt(et_ordinary_num.getText().toString());
                    int newnum = 0;
                    if (num >= Integer.parseInt(orderBean.getMinmpoqty())) {
                        if (Integer.parseInt(orderBean.getMinmpoqty()) > 0) {
                            newnum = num - Integer.parseInt(orderBean.getMinmpoqty());
                        } else {
                            //如果最小值了，就删除
                            newnum = num - 1;
                        }
                        et_ordinary_num.setText(Integer.toString(newnum));
                        orderBean.setQty(et_ordinary_num.getText().toString());
                    }
                    if (num == 0) {
                        delData(orderBean, position);
                    }

                }
            });

            //修改
            tv_ordinary_confirmProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //如果已有就更新
                    String newnum = orderBean.getQty();
                    String queryString = orderBean.getItemcode();
                    int updataFlag = (new WantOrderModelDao()).updateSingleDataNum(mContext, dbname, queryString, newnum);
                    if (updataFlag > 0) {
                        //刷新
                        refreshAmountCount();
                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            tv_ordinary_delProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delData(orderBean, position);
                }
            });
        }

        /**
         * 删除数据
         * @param orderBean
         * @param position
         */
        private void delData(final ItemBean orderBean, final int position) {
            //如果为
            final AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
            mDialog.setTitle("提示");
            mDialog.setMessage("确定删除本条数据?");
            mDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除本条数据，并保存到数据库

                            int delFlag = (new WantOrderModelDao()).delSingleData(mContext, dbname, orderBean.getItemcode());
                            if (delFlag > 0) {
                                orderList.remove(orderBean);
                                //删除动画
                                notifyItemRemoved(position);
                                notifyDataSetChanged();
                                //刷新总数和数量
                                refreshAmountCount();
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
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
    }

    /**
     * 刷新fragment中的总数和总价
     */
    private void refreshAmountCount(){
        amount.setText(Double.toString(getamount()));
        count.setText(Integer.toString(orderList.size()));
    }


    /**
     * 获取总价格
     */
    private double getamount(){
        double amount=0;
        if (orderList!= null && orderList.size() > 0){
            for(ItemBean f: orderList){
                amount =amount+Double.parseDouble(f.getPurprice())*Double.parseDouble(f.getQty());
            }
        }
        return amount;
    }


}
