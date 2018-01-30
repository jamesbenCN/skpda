package com.sk.pda.parts.want.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sk.pda.bean.DeptBean;
import com.sk.pda.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/21.
 */

public class DeptModelDao {
    String TAG = "deptmodeldao";

    public List<DeptBean> queryData(String querytype, String queryString) {
        String dbPath = Constants.ITEMINFO;
        SQLiteDatabase db = null;
        Cursor cursor;
        List<DeptBean> data = new ArrayList<DeptBean>();

        try {
            Log.e(TAG, "queryData: 开始打开数据库");
            //初始化数据库
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);

            switch (querytype) {
                //查询产品列表

                case "seconddept":
                    cursor = db.rawQuery("select * from dept where dlevel='3' and deptcode like '" + queryString + "%'", null);
                    break;
                default:
                    cursor = db.query("dept", null, null, null, null, null, null);
                    Log.e(TAG, "queryData: default");
            }


            //将查询结果复制到返回对象
            if (cursor != null) {
                int size = cursor.getCount();
                //循环赋值
                while (true) {
                    //判断是否读完
                    if (size-- == 0)
                        break;

                    //进入下一条
                    cursor.moveToNext();

                    //实例化一个暂存的bean对象
                    DeptBean bean = new DeptBean();

                    //deptcode具体赋值
                    if (cursor.getString(cursor.getColumnIndex("deptcode")) != null) {
                        bean.setDeptcode(cursor.getString(cursor.getColumnIndex("deptcode")));
                    } else {
                        bean.setDeptcode("");
                    }
                    //deptname具体赋值
                    if (cursor.getString(cursor.getColumnIndex("deptname")) != null) {
                        bean.setDeptname(cursor.getString(cursor.getColumnIndex("deptname")));
                    } else {
                        bean.setDeptname("");
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
        }

        //返回查询结果数组对象
        return data;
    }


    public List<DeptBean> transToLocalDept(List<DeptBean> deptBeanList) {
        String dbPath = Constants.ITEMINFO;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<DeptBean> data = new ArrayList<DeptBean>();
        try {
            //初始化数据库
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);


            for (DeptBean deptBean : deptBeanList) {
                cursor = db.rawQuery("select * from dept where deptcode=" + deptBean.getDeptcode(), null);

                if (cursor != null) {
                    //实例化一个暂存的bean对象
                    DeptBean bean = new DeptBean();
                    cursor.moveToFirst();

                    //deptcode具体赋值
                    if (cursor.getString(cursor.getColumnIndex("deptcode")) != null) {
                        bean.setDeptcode(cursor.getString(cursor.getColumnIndex("deptcode")));
                    } else {
                        bean.setDeptcode("");
                    }
                    //deptname具体赋值
                    if (cursor.getString(cursor.getColumnIndex("deptname")) != null) {
                        bean.setDeptname(cursor.getString(cursor.getColumnIndex("deptname")));
                    } else {
                        bean.setDeptname("");
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
