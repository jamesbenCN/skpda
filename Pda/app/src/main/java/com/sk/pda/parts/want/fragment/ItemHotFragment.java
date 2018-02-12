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
import com.sk.pda.base.bean.ItemBean;
import com.sk.pda.base.sql.ItemModelDao;
import com.sk.pda.parts.want.sql.WantOrderModelDao;
import com.sk.pda.utils.ACache;

import java.util.ArrayList;
import java.util.List;


public class ItemHotFragment extends BaseFragment {
    String TAG = "HOTAGFRAGMENT";
    private TextView want_tv_trans_type;

    private RecyclerView want_rv_right;


    private String currentTransType;
    private String currentOrderDbName;
    Button bt_select_all;
    Button bt_insert;


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

        WantGoodsListActivity ac = (WantGoodsListActivity) getActivity();
        currentTransType = ac.getCurrentTransType();
        currentOrderDbName =ac.getCurrentOrderDbName();

        Log.e("xxxx", "hotfragment中类型为"+currentTransType );
        Log.e("xxxx", "hotfragment中ORDER数据库为"+currentOrderDbName );

        //刷新右边数据
        refreshView();

        //初始化监听器
        initListener();

    }


    /**
     * 初始化监听器
     */
    private void initListener() {

        select_flag = true;
        bt_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_flag) {
                    select_flag = false;
                    bt_select_all.setText("取消全选");
                    for (ItemBean f : want_result) {
                        f.setChildSelected(true);
                    }
                } else {
                    select_flag = true;
                    bt_select_all.setText("全选");
                    for (ItemBean f : want_result) {
                        f.setChildSelected(false);
                    }
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


    /**
     * 刷新右边视图
     */
    private void refreshView() {
        //从数据库获取数据
        getDataFromDb();
//        //设置recycleview动画，解决频闪
        ((DefaultItemAnimator) want_rv_right.getItemAnimator()).setSupportsChangeAnimations(false);
        //解析右边数据
        hotAdapter = new TypeHotAdapter(getActivity(), want_result, want_rv_right,currentOrderDbName);
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
        if (currentTransType.equals("DC")) {
            want_result = itemModel.queryData("dcishot", null);
        } else {
            want_result = itemModel.queryData("dsishot", null);
        }
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


        for (ItemBean itemBean : selectItemBeanList) {
            List<ItemBean> insertItemBeanList = new ArrayList<ItemBean>();
            final ItemBean findBean = (new WantOrderModelDao()).querySingleData(mContext, currentOrderDbName, itemBean.getItemcode());
            if (findBean.getItemcode() == null) {
                insertItemBeanList.add(itemBean);
                boolean insertFlag = (new WantOrderModelDao()).insertOrderModelToDb(mContext, currentOrderDbName, insertItemBeanList);
                if (insertFlag) {
                    insertNum++;
                }
            }
        }

        Toast.makeText(getActivity(), "共添加了" + Integer.toString(insertNum) + "条数据", Toast.LENGTH_SHORT).show();

    }


}