package com.sk.pda.parts.want.sql;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sk.pda.parts.want.bean.WantOrderListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作类
 */
public class WantOrderListModelDao {

    private String TAG = "OrderListModelDao类";


    /**
     * 把对象数组插入数据库
     *
     * @param context       调用的窗体上下文
     * @param dbName        数据库名
     * @param orderListBean 数组对象
     */
    public Boolean insertOrderModelToDb(Context context, String dbName, WantOrderListBean orderListBean) {
        SQLiteDatabase db = null;

        try {
            WantOrderListDbHelper dbHelper = new WantOrderListDbHelper(context, dbName, null, 1);
            db = dbHelper.getWritableDatabase();

            //开始事务
            db.beginTransaction();
            String sql1 = "insert into order_list_info(" +
                    "id," +
                    "type," +
                    "order_time," +
                    "is_order," +
                    "order_db_name" +
                    ") values (?,?,?,?,?)";
            db.execSQL(sql1, new Object[]{
                    null,
                    orderListBean.getType(),
                    orderListBean.getOrderTime(),
                    orderListBean.getIsOrdered(),
                    orderListBean.getOrderDbName()
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //提交
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
        return true;
    }

    /**
     * 查询
     * @param context   上下文
     * @param dbName    数据库名称
     * @param querytype 查询类型
     * @return 查询结果
     */
    public WantOrderListBean querySigleData(Context context, String dbName, String querytype) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        WantOrderListBean orderList = new WantOrderListBean();
        try {
            //初始化数据库
            WantOrderListDbHelper dbHelper = new WantOrderListDbHelper(context, dbName, null, 1);

            //设置为读的数据库
            db = dbHelper.getReadableDatabase();

            cursor = db.query("order_list_info", null, "order_db_name = ?", new String[]{querytype}, null, null, "id asc");

            //将查询结果复制到返回对象
            if (cursor != null) {
                cursor.moveToFirst();
                orderList.setType(cursor.getString(cursor.getColumnIndex("type")));
                orderList.setOrderTime(cursor.getString(cursor.getColumnIndex("order_time")));
                orderList.setIsOrdered(cursor.getString(cursor.getColumnIndex("is_order")));
                orderList.setOrderDbName(cursor.getString(cursor.getColumnIndex("order_db_name")));
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
        return orderList;
    }

    /**
     * 请求数据
     * @param context 容器
     * @param dbName 数据库名字
     * @param type 类型（直送和配送）
     * @return
     */
    public List<WantOrderListBean> queryData(Context context, String dbName, String type) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<WantOrderListBean> data = new ArrayList<>();

        try {
            //初始化数据库
            WantOrderListDbHelper orderListDbHelper = new WantOrderListDbHelper(context, dbName, null, 1);

            //设置为读的数据库
            db = orderListDbHelper.getReadableDatabase();

            cursor = db.query("order_list_info", null, "type=?", new String[]{type}, null, null, null);

            //将查询结果复制到返回对象
            if (cursor != null) {
                int size = cursor.getCount();
                Log.e(TAG, "queryData: " + Integer.toString(size));
                //循环赋值
                while (true) {
                    //判断是否读完
                    if (size-- == 0)
                        break;

                    //进入下一条
                    cursor.moveToNext();

                    //实例化一个暂存的bean对象
                    WantOrderListBean bean = new WantOrderListBean();

                    //type具体赋值
                    if (cursor.getString(cursor.getColumnIndex("type")) != null) {
                        bean.setType(cursor.getString(cursor.getColumnIndex("type")));
                        Log.e(TAG, "queryData: " + bean.getType());
                    } else {
                        bean.setType("");
                    }

                    //ordertime具体赋值
                    if (cursor.getString(cursor.getColumnIndex("order_time")) != null) {
                        bean.setOrderTime(cursor.getString(cursor.getColumnIndex("order_time")));
                        Log.e(TAG, "queryData: " + bean.getOrderTime());
                    } else {
                        bean.setOrderTime("");
                    }

                    //isorder具体赋值
                    if (cursor.getString(cursor.getColumnIndex("is_order")) != null) {
                        bean.setIsOrdered(cursor.getString(cursor.getColumnIndex("is_order")));
                        Log.e(TAG, "queryData: " + bean.getIsOrdered());
                    } else {
                        bean.setIsOrdered("");
                    }

                    //orderdbname具体赋值
                    if (cursor.getString(cursor.getColumnIndex("order_db_name")) != null) {
                        bean.setOrderDbName(cursor.getString(cursor.getColumnIndex("order_db_name")));
                        Log.e(TAG, "queryData: " + bean.getOrderDbName());
                    } else {
                        bean.setOrderDbName("");
                    }

                    //插入到返回对象
                    data.add(bean);
                    Log.e(TAG, "queryData: data size" + Integer.toString(data.size()));
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

    /**
     * 删除单挑数据
     * @param context 容器
     * @param dbName 数据库名字
     * @param queryString 要删除的数据库名字
     * @return
     */
    public int delSingleData(Context context, String dbName, String queryString) {
        SQLiteDatabase db = null;
        int delFlag;
        WantOrderListDbHelper orderListDbHelper = new WantOrderListDbHelper(context, dbName, null, 1);
        db = orderListDbHelper.getWritableDatabase();
        delFlag = db.delete("order_list_info", "order_db_name=?", new String[]{queryString});
        return delFlag;
    }

    /**
     * 更新订单状态
     * @param context 容器
     * @param dbName 数据库名称
     * @param queryString 需更新的数据库名字
     * @param newString 更新后的isorder状态
     * @return 更新的数据数量
     */
    public int updateSingleDataOrderIsOrder(Context context, String dbName, String queryString, String newString) {
        SQLiteDatabase db = null;
        int updateFlag;
        WantOrderDbHelper orderDbHelper = new WantOrderDbHelper(context, dbName, null, 1);
        //修改SQL语句
        db = orderDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_order",newString);
        updateFlag =db.update("order_list_info",values,"order_db_name=?",new String[]{queryString});
        //执行SQL
        return updateFlag;
    }

    /**
     * 更新订单时间
     * @param context 容器
     * @param dbName 数据库名称
     * @param queryString 要修改的order数据库名字
     * @param newString 更改后的order_time
     * @return 更新的数据数量
     */
    public int updateSingleDataOrderOrderTime(Context context, String dbName, String queryString, String newString) {
        SQLiteDatabase db = null;
        int updateFlag;
        WantOrderDbHelper orderDbHelper = new WantOrderDbHelper(context, dbName, null, 1);
        //修改SQL语句
        db = orderDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_time",newString);
        updateFlag =db.update("order_list_info",values,"order_db_name=?",new String[]{queryString});
        //执行SQL
        return updateFlag;
    }
}
