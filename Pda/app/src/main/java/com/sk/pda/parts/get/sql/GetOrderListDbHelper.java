package com.sk.pda.parts.get.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2018/1/8.
 */

public class GetOrderListDbHelper extends SQLiteOpenHelper {

    String TAG =  "GetOrderListDbHelper";

    public GetOrderListDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate: ");
        //产品数据表
        String sql1= "create table if not exists order_list_info(" +
                "id integer primary key AUTOINCREMENT," +
                "type varchar(20)," +
                "order_time varchar(20)," +
                "is_order varchar(20)," +
                "order_db_name varchar(20)" +
                ")";
        db.execSQL(sql1);
        Log.e(TAG, "getorder数据表创建成功" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
