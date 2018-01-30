package com.sk.pda.parts.get.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2018/1/8.
 */

public class GetOrderDbHelper extends SQLiteOpenHelper {

    String TAG = "GET.OrderDbHelper";

    public GetOrderDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate: ");
        //产品数据表
        String sql1= "create table if not exists getorder(" +
                "id integer primary key AUTOINCREMENT," +
                "itemcode varchar(20)," +
                "barcode varchar(20)," +
                "itemname varchar(20)," +
                "capacity varchar(20)," +
                "stockunit varchar(20)," +
                "packsize varchar(20)," +
                "rprice varchar(20)," +
                "purprice varchar(20)," +
                "qty varchar(20)," +
                "num varchar(20)" +
                ")";
        db.execSQL(sql1);
        Log.e(TAG, "getorder数据表创建成功" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
