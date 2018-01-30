package com.sk.pda.parts.want.fragment;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.pda.R;
import com.sk.pda.parts.want.WantGoodsListActivity;
import com.sk.pda.parts.want.adapter.TypeHotAdapter;

import com.sk.pda.parts.want.base.BaseFragment;
import com.sk.pda.bean.ItemBean;
import com.sk.pda.parts.want.sql.ItemModelDao;
import com.sk.pda.parts.want.sql.WantOrderModelDao;
import com.sk.pda.utils.ACache;

import java.util.ArrayList;
import java.util.List;


public class ItemHotFragment extends BaseFragment {
    String TAG = "HOTAGFRAGMENT";
    private TextView want_tv_trans_type;

    private RecyclerView want_rv_right;

    private String dbName;
    private String currentFatherType;
    private String currentTransType;
    Button bt_select_all;
    Button bt_insert;

    ACache aCache;


    private List<ItemBean> want_result = new ArrayList<>();



    private FrameLayout fl_list_container;
    private ListView lv_left;
    private RecyclerView rv_right;
    boolean select_flag;
    TypeHotAdapter hotAdapter;


    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_want_hot_goods_list, null);

        want_tv_trans_type = view.findViewById(R.id.tv_transtype);
        want_rv_right = view.findViewById(R.id.want_rv_right);
        bt_select_all = view.findViewById(R.id.want_hot_list_select_all);
        bt_insert = view.findViewById(R.id.want_hot_list_add);

        return view;
    }

    @Override
    public void initData() {


        //初始化缓存
        aCache = ACache.get(getActivity(), "main");
        WantGoodsListActivity ac= (WantGoodsListActivity) getActivity();
        currentTransType= ac.getCurrentTransType();
        Log.e(TAG, "hotfragment中"+currentTransType);

        //刷新右边数据
        refreshRightView();
        //首次加载
        select_all(want_result);

        //初始化监听器
        initListener();

    }


    /**
     * 初始化监听器
     *
     */
    private void initListener() {

        select_flag = true;
        bt_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_flag) {
                    select_flag = false;
                    bt_select_all.setText("取消全选");
                    unselect_all(want_result);
                } else {
                    select_flag = true;
                    bt_select_all.setText("全选");
                    select_all(want_result);
                }
                hotAdapter.notifyDataSetChanged();

            }
        });

        bt_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData(want_result);
            }
        });
    }


    private void select_all(List<ItemBean> itemBeanList) {
        for (ItemBean f : itemBeanList) {
            f.setChildSelected(true);
        }
    }

    private void unselect_all(List<ItemBean> itemBeanList) {
        for (ItemBean f : itemBeanList) {
            f.setChildSelected(false);
        }
    }


    /**
     * 刷新右边视图
     */
    private void refreshRightView() {
        //从数据库获取数据
        getDataFromDb();
//        //设置recycleview动画，解决频闪
        ((DefaultItemAnimator) want_rv_right.getItemAnimator()).setSupportsChangeAnimations(false);
        //解析右边数据
        hotAdapter = new TypeHotAdapter(getActivity(), want_result, want_rv_right);
        want_rv_right.setAdapter(hotAdapter);

        //常用分类设置为每行1个
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        //并设置布局
        want_rv_right.setLayoutManager(manager);
    }


    /**
     * 从数据库获取数据
     */
    private void getDataFromDb() {
        ItemModelDao itemModel = new ItemModelDao();
//        want_result = itemModel.queryData("list",null);
        want_result=itemModel.queryData("dcishot",null);
//        want_result=itemModel.queryData("dsishot",null);
    }


    private void insertData(List<ItemBean> itemBeanList) {
        //开始执行插入
        List<ItemBean> selectItemBeanList = new ArrayList<ItemBean>();
        int insertNum = 0;

        //从data中筛选出选择的内容
        for (ItemBean itemBean : itemBeanList) {
            if (itemBean.isChildSelected()) {
                selectItemBeanList.add(itemBean);
            }
        }
        final String tempdbname = aCache.getAsString("currentOrderDbName");

        for (ItemBean itemBean : selectItemBeanList) {
            List<ItemBean> insertItemBeanList = new ArrayList<ItemBean>();
            final ItemBean findBean = (new WantOrderModelDao()).querySingleData(mContext, tempdbname, itemBean.getItemcode());
            if (findBean.getItemcode() == null) {
                insertItemBeanList.add(itemBean);
                boolean insertFlag = (new WantOrderModelDao()).insertOrderModelToDb(mContext, tempdbname, insertItemBeanList);
                if (insertFlag) {
                    insertNum++;
                }
            }
        }

        Toast.makeText(getActivity(), "共添加了" + Integer.toString(insertNum) + "条数据", Toast.LENGTH_SHORT).show();

    }


}