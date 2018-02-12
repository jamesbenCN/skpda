package com.sk.pda.utils;

/**
 * Created by Administrator on 2018/2/6.
 */

public class ShutdownAPP {
    public static void go(){
        android.os.Process.killProcess(android.os.Process.myPid());  //获取PID
        System.exit(0);
    }
}
