package com.sk.pda.home.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.pda.R;
import com.sk.pda.app.MyApplication;
import com.sk.pda.base.BaseFragment;
import com.sk.pda.bean.StoreBean;
import com.sk.pda.home.adapter.StoreAdapter;
import com.sk.pda.home.adapter.UnitAdapter;
import com.sk.pda.home.popwin.GetFitPopupUtil;
import com.sk.pda.home.popwin.ReturnbackFitPopupUtil;
import com.sk.pda.home.popwin.SaleFitPopupUtil;
import com.sk.pda.home.popwin.WantFitPopupUtil;
import com.sk.pda.other.downdb.DbGetActivity;
import com.sk.pda.home.popwin.CheckFitPopupUtil;

import java.util.ArrayList;


public class HomeFragment extends BaseFragment {
    TextView tv_userName;
    TextView tv_storeName;
    TextView tv_changStore;
    ImageView icon_hide;
    LinearLayout ll_storeList;
    LinearLayout icon_update;
    LinearLayout icon_exit;

    RecyclerView recyclerView;
    GridView gridView;

    /**
     * 文字数组和图标数组
     */
    ArrayList<String> mNameList = new ArrayList<String>();
    ArrayList<Drawable> mDrawableList = new ArrayList<Drawable>();

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_home_index, null);
        tv_userName = view.findViewById(R.id.tv_username);
        tv_storeName = view.findViewById(R.id.tv_storename);
        tv_changStore = view.findViewById(R.id.tv_changeshop);
        ll_storeList =view.findViewById(R.id.ll_store_list);
        icon_hide = view.findViewById(R.id.icon_hide);
        icon_update = view.findViewById(R.id.icon_update);
        icon_exit = view.findViewById(R.id.icon_exit);
        gridView = view.findViewById(R.id.unitgridview);
        recyclerView = view.findViewById(R.id.rv_store_list);
        return view;
    }


    @Override
    public void initData() {
        super.initData();
        //初始化模块中的操作人员名字和店名
        MyApplication application = (MyApplication) getActivity().getApplication();
        tv_userName.setText(application.getUserName());

        StoreBean defaultStoreBean = application.getStoreList().get(0);
        application.setCurrentStoreBean(defaultStoreBean);

        String defualutStoreName = defaultStoreBean.getStorename();
        tv_storeName.setText(defualutStoreName);

        //换门店TV点击事件
        tv_changStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_storeList.setVisibility(View.VISIBLE);
            }
        });

        //隐藏换门店点击事件
        icon_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_storeList.setVisibility(View.GONE);
            }
        });

        //打开更新数据库窗体
        icon_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DbGetActivity.class));
            }
        });

        //退出
        icon_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder chooseDialog = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                chooseDialog.setTitle("选择");
                chooseDialog.setMessage("确定退出系统？");
                chooseDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                chooseDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                chooseDialog.show();
            }
        });


        //recyclview设置部分
        StoreAdapter storeAdapter = new StoreAdapter(mContext, application.getStoreList());
        storeAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StoreBean targetStoreBean = application.getStoreList().get(position);
                String targetStoreName = targetStoreBean.getStorename();

                final AlertDialog.Builder chooseDialog = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                chooseDialog.setTitle("选择");
                chooseDialog.setMessage("是否将当前门店更改为" + targetStoreName);

                chooseDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        application.setCurrentStoreBean(targetStoreBean);
                        String currentStoreName = application.getCurrentStoreBean().getStorename();
                        tv_storeName.setText(currentStoreName);
                        Toast.makeText(getActivity(), "当前门店已更改为" + currentStoreName, Toast.LENGTH_SHORT).show();
                    }
                });

                chooseDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                chooseDialog.show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(storeAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);


        //gridview设置部分
        //添加文字
        mNameList.add("要货");
        mNameList.add("收货");
        mNameList.add("退货");
        mNameList.add("盘点");
        mNameList.add("销售");
        //留白文字
        mNameList.add(" ");
        mNameList.add(" ");
        mNameList.add(" ");

        //添加图标
        mDrawableList.add(getResources().getDrawable(R.drawable.unit_want));
        mDrawableList.add(getResources().getDrawable(R.drawable.unit_get));
        mDrawableList.add(getResources().getDrawable(R.drawable.unit_returnback));
        mDrawableList.add(getResources().getDrawable(R.drawable.unit_check));
        mDrawableList.add(getResources().getDrawable(R.drawable.unit_sale));
        //留白图标,使gridview补足空格
        mDrawableList.add(getResources().getDrawable(R.drawable.blank));
        mDrawableList.add(getResources().getDrawable(R.drawable.blank));
        mDrawableList.add(getResources().getDrawable(R.drawable.blank));


        //适配器
        UnitAdapter unitadapter = new UnitAdapter(getActivity(), mNameList, mDrawableList);
        //设置适配器
        gridView.setAdapter(unitadapter);
        //设计item点击事件监听器
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case 0:
                        initWantPopup(gridView.getChildAt(0));
                        break;
                    case 1:
                        initGetPopup(gridView.getChildAt(1));
                        break;
                    case 2:
                        initReturnbackPopup(gridView.getChildAt(2));
                        break;
                    case 3:
                        initCheckPopup(gridView.getChildAt(3));
                        break;
                    case 4:
                       initSalePopup(gridView.getChildAt(4));
                        break;
                    default:
                        return;
                }
            }
        });

    }

    //初始化要货模块的弹窗
    private void initWantPopup(View anchorView) {
        WantFitPopupUtil wantFitPopupUtil = new WantFitPopupUtil(getActivity());
        wantFitPopupUtil.showPopup(anchorView);
    }

    //初始化收货模块的弹窗
    private void initGetPopup(View anchorView) {
        GetFitPopupUtil getFitPopupUtil = new GetFitPopupUtil(getActivity());
        getFitPopupUtil.showPopup(anchorView);
    }

    //初始化退货模块的弹窗
    private void initReturnbackPopup(View anchorView) {
        ReturnbackFitPopupUtil returnbackFitPopupUtil = new ReturnbackFitPopupUtil(getActivity());
        returnbackFitPopupUtil.showPopup(anchorView);
    }

    //初始化盘点模块的弹窗
    private void initCheckPopup(View anchorView) {
        CheckFitPopupUtil checkFitPopupUtil = new CheckFitPopupUtil(getActivity());
        checkFitPopupUtil.showPopup(anchorView);
    }

    //初始化销量模块的弹窗
    private void initSalePopup(View anchorView) {
       SaleFitPopupUtil saleFitPopupUtil = new SaleFitPopupUtil(getActivity());
        saleFitPopupUtil.showPopup(anchorView);
    }







}
