package com.sk.pda.home.popwin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sk.pda.R;
import com.sk.pda.popwinutils.DensityUtils;
import com.sk.pda.popwinutils.FitPopupWindow;
import com.sk.pda.popwinutils.ScreenUtils;


public class CheckFitPopupUtil {

    private View contentView;
    private Activity context;
    private FitPopupWindow mPopupWindow;
    private LinearLayout icon_new_check;

    public CheckFitPopupUtil(Activity context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.popwin_home_check, null);
        findViews();
        initListener();
    }

    private void findViews() {
        icon_new_check = contentView.findViewById(R.id.icon_new_check);

    }

    private void initListener() {
        icon_new_check.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"xx",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        
    }

    /**
     * 弹出自适应位置的popupwindow
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
