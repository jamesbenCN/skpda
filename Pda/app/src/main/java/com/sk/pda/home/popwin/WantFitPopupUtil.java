package com.sk.pda.home.popwin;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sk.pda.R;
import com.sk.pda.parts.want.WantNewOrderActivity;
import com.sk.pda.parts.want.WantOrderListActivity;
import com.sk.pda.utils.popwinutils.DensityUtils;
import com.sk.pda.utils.popwinutils.FitPopupWindow;
import com.sk.pda.utils.popwinutils.ScreenUtils;


public class WantFitPopupUtil {

    private View contentView;
    private Activity context;
    private FitPopupWindow mPopupWindow;

    private LinearLayout icon_peisong_newOrder;
    private LinearLayout icon_zhisong_newOrder;
    private LinearLayout icon_Order;

    public WantFitPopupUtil(Activity context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.popwin_home_want, null);
        findViews();
        initListener();
    }

    private void findViews() {
        icon_peisong_newOrder = contentView.findViewById(R.id.icon_peisong_newOrder);
        icon_zhisong_newOrder = contentView.findViewById(R.id.icon_zhisong_newOrder);
        icon_Order = contentView.findViewById(R.id.icon_Order);

    }

    private void initListener() {
        //点击新建配送订单
        icon_peisong_newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WantNewOrderActivity.class);
                intent.putExtra("currentTransType", "DC");
                context.startActivity(intent);
            }
        });

        //点击新建直送订单
        icon_zhisong_newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WantNewOrderActivity.class);
                intent.putExtra("currentTransType", "DS");
                context.startActivity(intent);
            }
        });

        //点击查看订单
        icon_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WantOrderListActivity.class);
                context.startActivity(intent);
            }
        });
        
    }

    /**
     * 弹出自适应位置的popupwindow
     *
     * @param anchorView 目标view
     */
    public View showPopup(View anchorView) {
        if (mPopupWindow == null) {

             mPopupWindow = new FitPopupWindow(context,
                                ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(20),
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
        }
        mPopupWindow.setView(contentView, anchorView);
        mPopupWindow.show();
        return contentView;
    }

    
}
