package com.sk.pda.other.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.Toast;

import com.sk.pda.R;
import com.sk.pda.app.LoginActivity;
import com.sk.pda.app.MyApplication;
import com.sk.pda.base.fragment.BaseFragment;
import com.sk.pda.other.activty.HelpActivity;
import com.sk.pda.other.activty.TestActivity;
import com.sk.pda.other.activty.TestFuncActivity;
import com.sk.pda.other.activty.UpdateActivity;
import com.sk.pda.other.downdb.DbGetActivity;
import com.sk.pda.utils.FileHelper;
import com.sk.pda.utils.Unitl;


/**
 * Created by Administrator on 2017/10/30.
 */

public class OtherFragment extends BaseFragment {

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_other, null);
        return view;
    }

    @Override
    public void initData() {

        /**
         * 查看终端唯一标识
         */
        TableRow lookTerminalbutton =getActivity().findViewById(R.id.lookTerminalBtn);
        lookTerminalbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication app = (MyApplication) getActivity().getApplication();

                Toast.makeText(getActivity(),app.getTerminalId(),Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 查看测试SOAP页面
         */
        TableRow testbutton =getActivity().findViewById(R.id.testBtn);
        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TestActivity.class));
            }
        });

        /**
         * 查看应用授权
         */
        TableRow lookPermissionButton =getActivity().findViewById(R.id.lookPermissionBtn);
        lookPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Unitl.getAppDetailSettingIntent(getActivity());
            }
        });

        /**
         * 进入功能测试模块
         */
        TableRow funcbutton = getActivity().findViewById(R.id.netConfigBtn);
        funcbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TestFuncActivity.class));
            }
        });

        /**
         * 进入更新通配数据模块
         */
        TableRow getDBbutton = getActivity().findViewById(R.id.getDBBtn);
        getDBbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DbGetActivity.class));
            }
        });

        /**
         * 清除缓存功能
         */
        TableRow cleanCachebutton = getActivity().findViewById(R.id.cleanCacheBtn);
        cleanCachebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder chooseDialog = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                chooseDialog.setTitle("选择");
                chooseDialog.setMessage("您确定要清除缓存吗？");

                chooseDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //清除数据库
                        FileHelper.cleanDatabases(getContext());
                        //清除cache中的文件
                        FileHelper.cleanInternalCache(getContext(), "main");
                        FileHelper.cleanInternalCache(getContext(), "image_manager_disk_cache");
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

        /**
         * 进入升级APP页面
         */
        TableRow updatebutton = getActivity().findViewById(R.id.updataBtn);
        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateActivity.class));
            }
        });

        /**
         * 进入帮助页面
         */
        TableRow helpbutton = getActivity().findViewById(R.id.helpBtn);
        helpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HelpActivity.class));
            }
        });

        /**
         * 注销账号
         */
        Button logoutbutton = getActivity().findViewById(R.id.logoffBtn);
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        /**
         * 退出APP
         */
        Button closebutton = (Button) getActivity().findViewById(R.id.closeBtn);
        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

}

