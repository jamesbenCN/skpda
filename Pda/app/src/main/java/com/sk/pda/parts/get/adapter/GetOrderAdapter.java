package com.sk.pda.parts.get.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sk.pda.R;
import com.sk.pda.parts.get.bean.GetItemBean;

import java.util.List;


public class GetOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = "getorderadapter";
    private Context mContext;
    private List<GetItemBean> getItemList;
    private final LayoutInflater mLayoutInflater;


    public GetOrderAdapter(Context mContext, List<GetItemBean> getItemBeanList) {
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.getItemList = getItemBeanList;

    }


    @Override
    public GetOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item_view = mLayoutInflater.inflate(R.layout.item_get_order, null);
        return new GetOrderViewHolder(item_view, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetOrderViewHolder itemViewHolder = (GetOrderViewHolder) holder;
        itemViewHolder.setData(getItemList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return getItemList.size();
    }

    class GetOrderViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private TextView tv_itemcode;
        private TextView tv_itembarcode;
        private TextView tv_itemname;
        private TextView tv_itemcapacity;
        private TextView tv_itemstockunit;
        private TextView tv_itempacksize;
        private TextView tv_itemrprice;
        private TextView tv_itempurprice;
        private TextView tv_itemqty;
        private TextView tv_itemnum;

        public GetOrderViewHolder(View itemView, final Context mContext) {
            super(itemView);
            this.mContext = mContext;

            tv_itemcode = itemView.findViewById(R.id.tv_item_code);
            tv_itembarcode = itemView.findViewById(R.id.tv_item_bar_code);
            tv_itemname = itemView.findViewById(R.id.tv_item_name);
            tv_itemcapacity = itemView.findViewById(R.id.tv_item_capacity);
            tv_itemstockunit = itemView.findViewById(R.id.tv_item_stockunit);
            tv_itempacksize = itemView.findViewById(R.id.tv_item_packsize);
            tv_itemrprice = itemView.findViewById(R.id.tv_item_rprice);
            tv_itempurprice = itemView.findViewById(R.id.tv_item_purprice);
            tv_itemqty = itemView.findViewById(R.id.tv_item_qty);
            tv_itemnum = itemView.findViewById(R.id.tv_item_num);

        }

        public void setData(final GetItemBean itemBean, final int position) {
            tv_itemcode.setText(itemBean.getItemcode());
            tv_itembarcode.setText(itemBean.getBarcode());
            tv_itemname.setText(itemBean.getItemname());
            tv_itemcapacity.setText(itemBean.getCapacity());
            tv_itemstockunit.setText(itemBean.getStockunit());
            tv_itempacksize.setText(Float.toString(itemBean.getPacksize()));
            tv_itemrprice.setText(Float.toString(itemBean.getRprice()));
            tv_itempurprice.setText(Float.toString(itemBean.getPurprice()));
            tv_itemqty.setText(Float.toString(itemBean.getQty()));
            tv_itemnum.setText(Float.toString(itemBean.getNum()));
            Log.e(TAG, "itemnum" + itemBean.getNum());
        }

    }
}
