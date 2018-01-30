package com.sk.pda.parts.want.fragment;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sk.pda.R;
import com.sk.pda.parts.want.adapter.WantOrderListAdapter;
import com.sk.pda.parts.want.base.BaseFragment;
import com.sk.pda.parts.want.bean.WantOrderListBean;
import com.sk.pda.parts.want.sql.WantOrderListModelDao;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */

public class DSListFragment extends BaseFragment {
    String TAG = "ALLfragment";
    RecyclerView want_rv_orderlist;


    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_want_ds, null);
        want_rv_orderlist = view.findViewById(R.id.rv_orderlist_ds);
        initData();
        return view;
    }

    public void initData() {
        List<WantOrderListBean> upData = getData();
        WantOrderListAdapter orderlistAdapter = new WantOrderListAdapter(getActivity(), upData);
        want_rv_orderlist.setAdapter(orderlistAdapter);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        want_rv_orderlist.setLayoutManager(manager);
    }

    private List<WantOrderListBean> getData() {
        List<WantOrderListBean> data;
        data = (new WantOrderListModelDao()).queryData(getActivity(), "orderlistdb", "DS");
        return data;
    }


}
