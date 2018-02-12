package com.sk.pda.parts.want.fragment;


import android.graphics.drawable.PaintDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.pda.R;
import com.sk.pda.app.MyApplication;
import com.sk.pda.base.bean.DeptBean;
import com.sk.pda.base.bean.ItemBean;
import com.sk.pda.parts.want.WantGoodsListActivity;
import com.sk.pda.parts.want.adapter.FirstClassAdapter;
import com.sk.pda.parts.want.adapter.SecondClassAdapter;
import com.sk.pda.parts.want.adapter.TypeRightAdapter;
import com.sk.pda.parts.want.base.BaseFragment;
import com.sk.pda.parts.want.bean.FirstClassItem;
import com.sk.pda.parts.want.bean.SecondClassItem;
import com.sk.pda.base.sql.DeptModelDao;
import com.sk.pda.base.sql.ItemModelDao;
import com.sk.pda.utils.ACache;
import com.sk.pda.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;


public class ItemListFragment extends BaseFragment {
    String TAG = "ListFragment";
    private TextView want_tv_trans_type;

    private TextView want_tv_hot;

    private RecyclerView want_rv_right;
    private String dbName;

    private String currentTransType;
    private String currentOrderDbName;

    private int currentLeftPosition = 0;

    private List<ItemBean> want_result = new ArrayList<>();



    TextView want_tv_type_choose;
    TextView want_tv_current_type_name;
    /**
     * 左侧一级分类的数据
     */
    private List<FirstClassItem> firstList;
    /**
     * 中间二级分类的数据
     */
    private List<SecondClassItem> secondList;

    /**
     * 使用PopupWindow显示一级分类和二级分类、三级分类
     */
    private PopupWindow popupWindow;

    /**
     * 左侧和右侧两个ListView
     */
    private ListView leftLV, centerLV;
    //弹出PopupWindow时背景变暗
    private View darkView;

    private View lineView;

    //弹出PopupWindow时，背景变暗的动画
    private Animation animIn, animOut;



    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_want_goods_list, null);
        want_tv_trans_type = view.findViewById(R.id.tv_transtype);
        want_tv_type_choose = view.findViewById(R.id.tv_type_choose);
        want_rv_right = view.findViewById(R.id.want_rv_right);
        lineView = view.findViewById(R.id.drop_down_line);
        darkView = view.findViewById(R.id.main_darkview);
        want_tv_current_type_name = view.findViewById(R.id.want_tv_currentItemType);
        return view;
    }


    @Override
    public void initData() {



        //初始化缓存
        WantGoodsListActivity ac= (WantGoodsListActivity) getActivity();
        currentTransType= ac.getCurrentTransType();
        currentOrderDbName =ac.getCurrentOrderDbName();

        Log.e("xxxx", "listfragment中类型为"+currentTransType );
        Log.e("xxxx", "listfragment中ORDER数据库为"+currentOrderDbName );


        initListener();
        addTypeData();
        initPopup();
        //刷新右边数据
        refreshRView();

    }

    /**
     * POPWin中加载分类数据
     */
    private void addTypeData() {
        MyApplication application = (MyApplication) getActivity().getApplication();
        List<DeptBean> mDept = application.getDeptList();
        firstList = new ArrayList<FirstClassItem>();
        for (DeptBean dept : mDept) {
            //查找这个dept下的二级
            String code = dept.getDeptcode();
            List<DeptBean> mSecondedept = (new DeptModelDao()).queryData("seconddept", code);
            ArrayList<SecondClassItem> secondList = new ArrayList<SecondClassItem>();
            //有的话插入secondList
            for (DeptBean sdept : mSecondedept) {
                secondList.add(new SecondClassItem(sdept.getDeptcode(), sdept.getDeptname()));
            }
            //这个部门的deptcode,deptname,secondelist添加到firstList中
            firstList.add(new FirstClassItem(dept.getDeptcode(), dept.getDeptname(), secondList));
        }
    }


    /**
     * 初始化监听器
     */
    private void initListener() {
        want_tv_type_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.showAsDropDown(lineView);
                    popupWindow.setAnimationStyle(-1);
                    //背景变暗
                    darkView.startAnimation(animIn);
                    darkView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initPopup() {
        animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_anim);
        popupWindow = new PopupWindow(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_layout, null);
        leftLV = (ListView) view.findViewById(R.id.pop_listview_left);
        centerLV = (ListView) view.findViewById(R.id.pop_listview_center);

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setFocusable(true);

        popupWindow.setHeight(ScreenUtils.getScreenH(getActivity()) * 2 / 3);
        popupWindow.setWidth(ScreenUtils.getScreenW(getActivity()));

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkView.startAnimation(animOut);
                darkView.setVisibility(View.GONE);

                leftLV.setSelection(0);
                centerLV.setSelection(0);
            }
        });


        //为了方便扩展，这里的两个ListView均使用BaseAdapter.如果分类名称只显示一个字符串，建议改为ArrayAdapter.
        //加载一级分类
        final FirstClassAdapter firstAdapter = new FirstClassAdapter(getActivity(), firstList);
        leftLV.setAdapter(firstAdapter);

        //加载左侧第一行对应右侧二级分类
        secondList = new ArrayList<SecondClassItem>();
        secondList.addAll(firstList.get(0).getSecondList());
        final SecondClassAdapter secondAdapter = new SecondClassAdapter(getActivity(), secondList);
        centerLV.setAdapter(secondAdapter);

        //左侧ListView点击事件
        leftLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //二级数据
                List<SecondClassItem> list2 = firstList.get(position).getSecondList();
                //如果没有二级类，则直接跳转
                if (list2 == null || list2.size() == 0) {
                    popupWindow.dismiss();

                    String firstCode = firstList.get(position).getCode();
                    String firstName = firstList.get(position).getName();
                    handleResult(firstCode, firstName, "", "");
                    return;
                }

                FirstClassAdapter adapter = (FirstClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position) {
                    return;
                }

                //根据左侧一级分类选中情况，更新背景色
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();

                //显示右侧二级分类
                updateSecondListView(list2, secondAdapter);
            }
        });

        //中间ListView点击事件
        centerLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭popupWindow，显示用户选择的分类
                popupWindow.dismiss();

                int firstPosition = firstAdapter.getSelectedPosition();
                String firstCode = firstList.get(firstPosition).getCode();
                String firstName =firstList.get(firstPosition).getName();
                String secondCode = firstList.get(firstPosition).getSecondList().get(position).getCode();
                String secondName = firstList.get(firstPosition).getSecondList().get(position)
                        .getName();
                handleResult(firstCode, firstName, secondCode,secondName);
            }
        });
    }





    //刷新中间ListView
    private void updateSecondListView(List<SecondClassItem> list2, SecondClassAdapter secondAdapter) {
        secondList.clear();
        secondList.addAll(list2);
        secondAdapter.notifyDataSetChanged();
    }


    //处理点击结果
    private void handleResult(String firstCode, String firstName, String secondCode, String secondName) {
        want_tv_current_type_name.setText(firstName + "->" + secondName);
        Toast.makeText(getActivity(),firstCode+"ss"+secondCode,Toast.LENGTH_SHORT).show();
        getDataFromDb(secondCode);
    }



    /**
     * 刷新右边视图
     */
    private void refreshRView() {
        //从数据库获取数据
        getDefaultDataFromDb();
//        //设置recycleview动画，解决频闪
        ((DefaultItemAnimator) want_rv_right.getItemAnimator()).setSupportsChangeAnimations(false);
        //解析右边数据
        TypeRightAdapter rightAdapter = new TypeRightAdapter(getActivity(), want_result, want_rv_right,currentOrderDbName);
        want_rv_right.setAdapter(rightAdapter);

        //常用分类设置为每行1个
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        //并设置布局
        want_rv_right.setLayoutManager(manager);
    }

    /**
     * 从数据库获取数据
     */
    private void getDataFromDb(String groupfmcode) {
        ItemModelDao itemModel = new ItemModelDao();
        want_result.clear();

        if(currentTransType.equals("DC")){
            want_result = itemModel.queryData("deptdc",groupfmcode);
        }else {
            want_result = itemModel.queryData("deptds",groupfmcode);
        }

        //解析右边数据
        TypeRightAdapter rightAdapter = new TypeRightAdapter(getActivity(), want_result, want_rv_right,currentOrderDbName);
        want_rv_right.setAdapter(rightAdapter);
    }

    /**
     * 从数据库获取默认数据
     */
    private void getDefaultDataFromDb() {
        ItemModelDao itemModel = new ItemModelDao();
        want_result.clear();
        //设置默认加载的数据为生鲜
        if(currentTransType.equals("DC")){
            want_result = itemModel.queryData("defaultdc","41");
        }else {
            want_result = itemModel.queryData("defaultds","41");
        }

    }



}