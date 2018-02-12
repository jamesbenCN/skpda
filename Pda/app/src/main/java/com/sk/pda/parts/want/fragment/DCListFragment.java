package com.sk.pda.parts.want.fragment;


import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sk.pda.R;
import com.sk.pda.app.MyApplication;
import com.sk.pda.parts.want.adapter.WantOrderListAdapter;
import com.sk.pda.parts.want.base.BaseFragment;
import com.sk.pda.parts.want.bean.WantOrderListBean;
import com.sk.pda.parts.want.sql.WantOrderListModelDao;

import java.util.List;


public class DCListFragment extends BaseFragment {
    String TAG = "ALLfragment";
    RecyclerView want_rv_orderlist;

    String usercode;


    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_want_dc, null);
        want_rv_orderlist = view.findViewById(R.id.rv_orderlist_all);
        initData();
        return view;
    }

    public void initData() {

        MyApplication app = (MyApplication) getActivity().getApplication();
        usercode = app.getUserCode();

        List<WantOrderListBean> mData = getData();
        WantOrderListAdapter orderlistAdapter = new WantOrderListAdapter(getActivity(), mData);
        want_rv_orderlist.setAdapter(orderlistAdapter);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        //
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.little);
        want_rv_orderlist.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        want_rv_orderlist.setLayoutManager(manager);
    }

    private List<WantOrderListBean> getData() {
        List<WantOrderListBean> data;
        data = (new WantOrderListModelDao()).queryData(getActivity(), "orderlistdb", "DC",usercode);
        return data;
    }


    public class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        private int space;
        public SpaceItemDecoration(int space) {
            this.space = space;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if(parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }


}
