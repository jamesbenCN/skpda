package com.sk.pda.parts.want.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sk.pda.bean.StoreBean;
import com.sk.pda.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/21.
 */

public class StoreModelDao {
    String TAG = "storemodeldao";


    public List<StoreBean> transToLocalStore(List<StoreBean> storeBeanList) {
        String dbPath = Constants.ITEMINFO;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<StoreBean> data = new ArrayList<StoreBean>();
        try {
            //初始化数据库
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);


            for (StoreBean storeBean : storeBeanList) {
                cursor = db.rawQuery("select * from store where storecode=" + storeBean.getStorecode(), null);

                if (cursor != null) {
                    //实例化一个暂存的bean对象
                    StoreBean bean = new StoreBean();
                    cursor.moveToFirst();

                    //deptcode具体赋值
                    if (cursor.getString(cursor.getColumnIndex("storecode")) != null) {
                        bean.setStorecode(cursor.getString(cursor.getColumnIndex("storecode")));
                    } else {
                        bean.setStorecode("");
                    }
                    //deptname具体赋值
                    if (cursor.getString(cursor.getColumnIndex("storename")) != null) {
                        bean.setStorename(cursor.getString(cursor.getColumnIndex("storename")));
                    } else {
                        bean.setStorename("");
                    }

                    //插入到返回对象
                    data.add(bean);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //提交
//           db.setTransactionSuccessful();
//           db.endTransaction();
            if (db != null) {
                db.close();
            }
            //返回查询结果数组对象
        }
        return data;
    }
}
