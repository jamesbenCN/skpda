package com.sk.pda.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sk.pda.R;
import com.sk.pda.base.bean.StoreBean;

import java.util.List;



/**
 * 商店列表的适配器
 */
public class StoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<StoreBean> lists;
    private final LayoutInflater mLayoutInflater;

    private static OnItemClickListener mOnItemClickListener;

    //设置点击和长按事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position); //设置点击事件

        void onItemLongClick(View view, int position); //设置长按事件
    }

    public static void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public StoreAdapter(Context mContext, List<StoreBean> list) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.lists = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreViewHolder(mLayoutInflater.inflate(R.layout.item_store, null), mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StoreViewHolder storeViewHolder = (StoreViewHolder) holder;
        storeViewHolder.setData(lists.get(position), position);

        if (mOnItemClickListener != null) {
            storeViewHolder.itemView.setOnClickListener(new View.OnClickListener() { //itemview是holder里的所有控件，可以单独设置比如ImageButton Button等
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            storeViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() { //长按事件
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class StoreViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private TextView storename;
        private LinearLayout ll_root;

        public StoreViewHolder(View itemView, Context context) {
            super(itemView);
            this.mContext = context;
            storename = itemView.findViewById(R.id.item_store_text);
            ll_root = itemView.findViewById(R.id.ll_root);
        }

        public void setData(StoreBean storeBean, final int position) {
            storename.setText(storeBean.getStorename());
        }
    }
}
