package com.sk.pda.home.popwin;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sk.pda.R;
import com.sk.pda.parts.sale.SaleActivity;
import com.sk.pda.utils.popwinutils.DensityUtils;
import com.sk.pda.utils.popwinutils.FitPopupWindow;
import com.sk.pda.utils.popwinutils.ScreenUtils;


public class SaleFitPopupUtil {

    private View contentView;
    private Activity context;
    private FitPopupWindow mPopupWindow;
    private LinearLayout icon_new_sale;

    public SaleFitPopupUtil(Activity context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.popwin_home_sale, null);
        findViews();
        initListener();
    }

    private void findViews() {
        icon_new_sale = contentView.findViewById(R.id.icon_new_sale);

    }

    private void initListener() {
        icon_new_sale.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, SaleActivity.class));
                    }
                }
        );
        
    }

    /**
     * 弹出自适应位置的popupwindowd
     *
     * @param anchorView 目标view
     */
    public View showPopup(View anchorView) {
        if (mPopupWindow == null) {

//             mPopupWindow = new FitPopupWindow(context,
//                    ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(200),
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );

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
