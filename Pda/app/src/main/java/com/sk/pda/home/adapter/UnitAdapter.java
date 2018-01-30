package com.sk.pda.home.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sk.pda.R;

import java.util.ArrayList;



public class UnitAdapter extends BaseAdapter {
    private ArrayList<String> mNameList = new ArrayList<String>();
    private ArrayList<Drawable> mDrawableList = new ArrayList<Drawable>();
    private LayoutInflater mInflater;
    private Context mContext;
    LinearLayout.LayoutParams params;

    public UnitAdapter(Context context, ArrayList<String> nameList, ArrayList<Drawable> drawableList) {
        mNameList = nameList;
        mDrawableList = drawableList;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
    }

    public int getCount() {
        return mNameList.size();
    }

    public Object getItem(int position) {
        return mNameList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewTag viewTag;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.unit_item, null);

            // construct an item tag
            viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.gridview_item_img), (TextView) convertView.findViewById(R.id.gridview_item_text));
            convertView.setTag(viewTag);
        } else
        {
            viewTag = (ItemViewTag) convertView.getTag();
        }

        // set name
        viewTag.mName.setText(mNameList.get(position));

        // set icon
        viewTag.mIcon.setBackgroundDrawable(mDrawableList.get(position));
        viewTag.mIcon.setLayoutParams(params);
        return convertView;
    }

    /**
     * 元素类
     */
    private class ItemViewTag
    {
        ImageView mIcon;
        TextView mName;


        /**
         * 元素类构造函数
         * @param icon 图标
         * @param name 文字
         */
        ItemViewTag(ImageView icon, TextView name)
        {
            this.mName = name;
            this.mIcon = icon;
        }
    }
}
