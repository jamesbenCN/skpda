package com.sk.pda.parts.want.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class WantOrderDbHelper extends SQLiteOpenHelper {
    String TAG = "WANT.OrderDbHelper";

    /**
     *
     * @param context 上下文
     * @param name 数据库名字
     * @param factory 工厂？
     * @param version 版本号
     */
    public WantOrderDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行.
     * 重写onCreate方法，调用execSQL方法创建表
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "OrderDb onCreate" );

        //产品数据表
        String sql1= "create table if not exists orderlist(" +
                "id integer primary key AUTOINCREMENT," +
                "itemcode varchar(20)," +
                "itemname varchar(20)," +
                "barcode varchar(20)," +
                "deptcode varchar(20)," +
                "groupfmcode varchar(20)," +
                "familycode varchar(20)," +
                "subfmcode varchar(20)," +
                "capacity varchar(20)," +
                "supptype varchar(20)," +
                "ishot varchar(20)," +
                "rprice varchar(20)," +
                "purprice varchar(20)," +
                "minmpoqty varchar(20)," +
                "packsize varchar(20)," +
                "stockunit varchar(20)," +
                "qty varchar(20)" +
                ")";
        db.execSQL(sql1);
        Log.e(TAG, "产品order数据表创建成功" );
    }

    /**
     * 升级数据库
     * @param db xx
     * @param oldVersion xx
     * @param newVersion xx
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}