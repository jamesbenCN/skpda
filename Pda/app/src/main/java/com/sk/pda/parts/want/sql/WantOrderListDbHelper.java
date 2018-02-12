package com.sk.pda.parts.want.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class WantOrderListDbHelper extends SQLiteOpenHelper {
    String TAG = "DbHelper";

    /**
     *
     * @param context 上下文
     * @param name 数据库名字
     * @param factory 工厂？
     * @param version 版本号
     */
    public WantOrderListDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行.
     * 重写onCreate方法，调用execSQL方法创建表
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "orderlistDb onCreate" );

        //产品大分类数据表
        String sql0="create table if not exists order_list_info(" +
                "id integer primary key AUTOINCREMENT," +
                "type varchar(20)," +
                "order_time varchar(20)," +
                "need_time varchar(20)," +
                "count varchar(20)," +
                "amount varchar(20)," +
                "is_order varchar(20)," +
                "order_db_name varchar(20)," +
                "usercode varchar(20)," +
                "no varchar(30)" +
                ")";
        db.execSQL(sql0);
        Log.e(TAG, "订单列表数据库创建成功" );
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