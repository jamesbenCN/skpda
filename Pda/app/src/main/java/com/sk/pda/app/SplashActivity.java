package com.sk.pda.app;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.sk.pda.R;
import com.sk.pda.utils.ShutdownAPP;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class SplashActivity extends AppCompatActivity {

    View textView;
    AnimationSet animationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        textView = findViewById(R.id.fullscreen_content);


        //设置动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


        animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(1500);


        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        requestPermissions();
    }

    /**
     * 权限列表
     */
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    /**
     * 请求权限
     */
    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .request(permissions)//多个权限用","隔开
                .subscribe(granted -> {
                    if (granted) {
                        //获得了所有权限
                       textView.setAnimation(animationSet);
                    } else {
                        //至少有一个权限没有获得
                        Toast.makeText(SplashActivity.this,"请给应用赋予全部权限，否则无法使用",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }



}





