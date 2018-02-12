package com.sk.pda.parts.want.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sk.pda.R;
import com.sk.pda.base.bean.ItemBean;
import com.sk.pda.parts.want.sql.WantOrderModelDao;
import com.sk.pda.utils.ACache;
import com.sk.pda.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class TypeHotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;

    private String TAG = "rightadapter";

    /**
     * 商品列表数据
     */
    private List<ItemBean> productList;
    private int max_count = 20;//最大显示数
    private Boolean isFootView = false;//是否添加了FootView

    private final LayoutInflater mLayoutInflater;
    private String tempdbname;

    //两个final int类型表示ViewType的两种类型
    private final int NORMAL_TYPE = 0;
    private final int FOOT_TYPE = 1111;

    RecyclerView rv_hot;

    /**
     * 继承父类构造函数
     *
     * @param mContext            窗体上下文
     * @param ordinaryProductList 普通数据
     */

    public TypeHotAdapter(Context mContext, List<ItemBean> ordinaryProductList, final RecyclerView rvhot,String currentOrderDbName) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        productList = ordinaryProductList;
        this.rv_hot = rvhot;
        this.tempdbname = currentOrderDbName;
    }

    @Override
    public OrdinaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View normal_views = mLayoutInflater.inflate(R.layout.item_want_ordinary_hot, null);
        View foot_view = mLayoutInflater.inflate(R.layout.foot_view, null);
        if (viewType == FOOT_TYPE)
            return new OrdinaryViewHolder(foot_view, FOOT_TYPE, mContext);
        return new OrdinaryViewHolder(normal_views, NORMAL_TYPE, mContext);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //如果footview存在，并且当前位置ViewType是FOOT_TYPE
        if (getItemViewType(position) == FOOT_TYPE) {
            isFootView = true;
            // 刷新太快 所以使用Hanlder延迟两秒
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    max_count += 10;
                    rv_hot.scrollToPosition(position - 1);
                }
            }, 2000);
        } else {
            OrdinaryViewHolder ordinaryViewHolder = (OrdinaryViewHolder) holder;
            ordinaryViewHolder.setData(productList.get(position), position);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == max_count - 1) {
            return FOOT_TYPE;
        }
        return NORMAL_TYPE;
    }

    @Override
    public int getItemCount() {
        if (productList.size() < max_count) {
            return productList.size();
        }
        return max_count;
    }

    class OrdinaryViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;


        public TextView tvFootView;//footView的TextView属于独自的一个layout
        private ImageView iv_ordinary_right;
        private TextView tv_ordinary_itemname;
        private TextView tv_ordinary_barcode;
        private TextView tv_ordinary_id;
        private TextView tv_ordinary_price;
        private TextView tv_ordinary_min_num;

        private TextView tv_ordinary_spec;
        private TextView tv_ordinary_unit;
        private TextView tv_ordinary_had_add_num;

        private CheckBox cb_ordinary_hot;

        ACache aCache;


        public OrdinaryViewHolder(View itemView, int viewType, final Context mContext) {
            super(itemView);
            this.mContext = mContext;

            if (viewType == NORMAL_TYPE) {
                cb_ordinary_hot =itemView.findViewById(R.id.cb_hot);
                iv_ordinary_right = itemView.findViewById(R.id.want_iv_right);
                tv_ordinary_itemname = itemView.findViewById(R.id.want_tv_right_name);
                tv_ordinary_barcode = itemView.findViewById(R.id.want_tv_right_no);
                tv_ordinary_id = itemView.findViewById(R.id.want_tv_right_id);
                tv_ordinary_price = itemView.findViewById(R.id.want_tv_right_price);
                tv_ordinary_min_num = itemView.findViewById(R.id.want_tv_right_min);
                tv_ordinary_spec = itemView.findViewById(R.id.want_tv_right_spec);
                tv_ordinary_unit = itemView.findViewById(R.id.want_tv_right_unit);
                tv_ordinary_had_add_num = itemView.findViewById(R.id.want_tv_had_add);
            } else if (viewType == FOOT_TYPE) {
                tvFootView = (TextView) itemView;
            }

        }

        public void setData(final ItemBean itemBean, final int position) {

            aCache = ACache.get(mContext, "main");
            itemBean.setQty("0");

            //加载图片
            if (aCache.getAsBitmap(itemBean.getFigure()) != null) {
                iv_ordinary_right.setImageBitmap(aCache.getAsBitmap(itemBean.getFigure()));
                Log.e(TAG, "加载了本地缓存中的图片");
            } else {
                Glide.with(mContext)
                        .load(Constants.BASE_URl_IMAGE + itemBean.getFigure())
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                iv_ordinary_right.setImageBitmap(resource);
                                aCache.put(itemBean.getFigure(), resource);
                                Log.e(TAG, "加载了网络上的图片");
                            }
                        });
            }

            cb_ordinary_hot.setChecked(itemBean.isChildSelected());

            //设置名称
            tv_ordinary_itemname.setText(itemBean.getItemname());
            tv_ordinary_barcode.setText(itemBean.getBarcode());
            tv_ordinary_id.setText(itemBean.getItemcode());
            tv_ordinary_spec.setText(itemBean.getPacksize());
            tv_ordinary_unit.setText(itemBean.getStockunit());
            tv_ordinary_min_num.setText(itemBean.getMinmpoqty());


            //设置已添加数量

            ItemBean findBean = (new WantOrderModelDao()).querySingleData(mContext, tempdbname, itemBean.getItemcode());

            if (findBean != null) {
                tv_ordinary_had_add_num.setText(findBean.getQty());
            }

            tv_ordinary_min_num.setText(itemBean.getMinmpoqty());

            cb_ordinary_hot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    itemBean.setChildSelected(isChecked);
                }
            });

        }




    }
}
