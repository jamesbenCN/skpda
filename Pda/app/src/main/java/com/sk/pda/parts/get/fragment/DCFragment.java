package com.sk.pda.parts.get.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sk.pda.R;
import com.sk.pda.parts.get.adapter.GetOrderListAdapter;
import com.sk.pda.parts.get.base.BaseFragment;
import com.sk.pda.parts.get.bean.GetOrderListBean;
import com.sk.pda.parts.get.sql.GetOrderListModelDao;

import java.util.List;

/**
 * Created by Administrator on 2018/1/10.
 */

public class DCFragment extends BaseFragment {

    RecyclerView want_rv_orderlist;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_get_dc, null);
        want_rv_orderlist = view.findViewById(R.id.rv_get_dc_orderlist);
        initData();
        return view;
    }

    public void initData() {
        List<GetOrderListBean> mData = getData();
        GetOrderListAdapter orderlistAdapter = new GetOrderListAdapter(getActivity(),mData);
        want_rv_orderlist.setAdapter(orderlistAdapter);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        want_rv_orderlist.setLayoutManager(manager);
    }

    /**
     * getData获取数据
     *
     * @return 数据
     */
    private List<GetOrderListBean> getData() {
        List<GetOrderListBean> data;
        data = (new GetOrderListModelDao()).queryData(getActivity(), "getorderlist", "1");
        Log.e("xxx", "dc getData: " + data.size());
        return data;
    }
}
