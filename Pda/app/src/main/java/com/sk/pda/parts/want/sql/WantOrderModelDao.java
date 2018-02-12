package com.sk.pda.parts.want.sql;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sk.pda.base.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作类
 */
public class WantOrderModelDao {

    private String TAG = "want.OrderModelDao类";

    /**
     * 把对象数组插入数据库
     *
     * @param context        调用的窗体上下文
     * @param dbName         数据库名
     * @param orderListModel 数组对象
     */
    public Boolean insertOrderModelToDb(Context context, String dbName, List<ItemBean> orderListModel) {
        SQLiteDatabase db = null;

        try {
            WantOrderDbHelper orderdbHelper = new WantOrderDbHelper(context, dbName, null, 1);
            db = orderdbHelper.getWritableDatabase();

            //开始事务
            db.beginTransaction();
            Log.e(TAG, orderListModel.size() + "条订单数据数据，准备插入");
            String sql1 = "insert into orderlist(" +
                    "id," +
                    "itemcode," +
                    "barcode," +
                    "itemname," +
                    "deptcode," +
                    "groupfmcode," +
                    "familycode," +
                    "subfmcode," +
                    "capacity," +
                    "supptype," +
                    "ishot," +
                    "rprice," +
                    "purprice," +
                    "minmpoqty," +
                    "packsize," +
                    "stockunit," +
                    "qty" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            for (ItemBean f : orderListModel) {
                db.execSQL(sql1, new Object[]{
                        null,
                        f.getItemcode(),
                        f.getBarcode(),
                        f.getItemname(),
                        f.getDeptcode(),
                        f.getGroupfmcode(),
                        f.getFamilycode(),
                        f.getSubfmcode(),
                        f.getCapacity(),
                        f.getSupptype(),
                        f.getIshot(),
                        f.getRprice(),
                        f.getPurprice(),
                        f.getMinmpoqty(),
                        f.getPacksize(),
                        f.getStockunit(),
                        f.getQty()
                });
            }
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
     * 更新单条数据数量
     *
     * @param context     容器窗体
     * @param dbName      数据库名字
     * @param queryString 查询的产品号
     * @param newNum      新的数值
     * @return 更新的数量
     */
    public int updateSingleDataNum(Context context, String dbName, String queryString, String newNum) {
        SQLiteDatabase db = null;
        int updateFlag;
        WantOrderDbHelper orderDbHelper = new WantOrderDbHelper(context, dbName, null, 1);
        //修改SQL语句
        db = orderDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("qty", newNum);
        updateFlag = db.update("orderlist", values, "itemcode=?", new String[]{queryString});
        //执行SQL
        return updateFlag;
    }

    public int delSingleData(Context context, String dbName, String queryString) {
        SQLiteDatabase db = null;
        int delFlag;
        WantOrderDbHelper orderDbHelper = new WantOrderDbHelper(context, dbName, null, 1);
        db = orderDbHelper.getWritableDatabase();
        delFlag = db.delete("orderlist", "itemcode=?", new String[]{queryString});
        return delFlag;
    }


    public ItemBean querySingleData(Context context, String dbName, String queryString) {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        ItemBean data = new ItemBean();
        try {
            //初始化数据库
            WantOrderDbHelper orderDbHelper = new WantOrderDbHelper(context, dbName, null, 1);

            //设置为读的数据库
            db = orderDbHelper.getReadableDatabase();
            cursor = db.query("orderlist", null, "itemcode=?", new String[]{queryString}, null, null, null);
            cursor.moveToFirst();

            if (cursor.getString(cursor.getColumnIndex("itemcode")) != null) {
                data.setItemcode(cursor.getString(cursor.getColumnIndex("itemcode")));
            } else {
                data.setItemcode("");
            }


            if (cursor.getString(cursor.getColumnIndex("barcode")) != null) {
                data.setBarcode(cursor.getString(cursor.getColumnIndex("barcode")));
            } else {
                data.setBarcode("");
            }


            if (cursor.getString(cursor.getColumnIndex("itemname")) != null) {
                data.setItemname(cursor.getString(cursor.getColumnIndex("itemname")));
            } else {
                data.setItemname("");
            }

            if (cursor.getString(cursor.getColumnIndex("deptcode")) != null) {
                data.setDeptcode(cursor.getString(cursor.getColumnIndex("deptcode")));
            } else {
                data.setDeptcode("");
            }

            if (cursor.getString(cursor.getColumnIndex("groupfmcode")) != null) {
                data.setGroupfmcode(cursor.getString(cursor.getColumnIndex("groupfmcode")));
            } else {
                data.setGroupfmcode("");
            }

            if (cursor.getString(cursor.getColumnIndex("familycode")) != null) {
                data.setFamilycode(cursor.getString(cursor.getColumnIndex("familycode")));
            } else {
                data.setFamilycode("");
            }


            if (cursor.getString(cursor.getColumnIndex("subfmcode")) != null) {
                data.setSubfmcode(cursor.getString(cursor.getColumnIndex("subfmcode")));
            } else {
                data.setSubfmcode("");
            }

            if (cursor.getString(cursor.getColumnIndex("capacity")) != null) {
                data.setCapacity(cursor.getString(cursor.getColumnIndex("capacity")));
            } else {
                data.setCapacity("");
            }


            if (cursor.getString(cursor.getColumnIndex("supptype")) != null) {
                data.setSupptype(cursor.getString(cursor.getColumnIndex("supptype")));
            } else {
                data.setSupptype("");
            }


            if (cursor.getString(cursor.getColumnIndex("ishot")) != null) {
                data.setIshot(cursor.getString(cursor.getColumnIndex("ishot")));
            } else {
                data.setIshot("");
            }


            if (cursor.getString(cursor.getColumnIndex("rprice")) != null) {
                data.setRprice(cursor.getString(cursor.getColumnIndex("rprice")));
            } else {
                data.setRprice("");
            }


            if (cursor.getString(cursor.getColumnIndex("purprice")) != null) {
                data.setPurprice(cursor.getString(cursor.getColumnIndex("purprice")));
            } else {
                data.setPurprice("");
            }


            if (cursor.getString(cursor.getColumnIndex("minmpoqty")) != null) {
                data.setMinmpoqty(cursor.getString(cursor.getColumnIndex("minmpoqty")));
            } else {
                data.setMinmpoqty("");
            }


            if (cursor.getString(cursor.getColumnIndex("packsize")) != null) {
                data.setPacksize(cursor.getString(cursor.getColumnIndex("packsize")));
            } else {
                data.setPacksize("");
            }


            if (cursor.getString(cursor.getColumnIndex("stockunit")) != null) {
                data.setStockunit(cursor.getString(cursor.getColumnIndex("stockunit")));
            } else {
                data.setStockunit("");
            }


            if (cursor.getString(cursor.getColumnIndex("qty")) != null) {
                data.setQty(cursor.getString(cursor.getColumnIndex("qty")));
            } else {
                data.setQty("");
            }


            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //提交
            if (db != null) {
                db.close();
            }
        }
        return data;
    }

    /**
     * 查询
     *
     * @param context     上下文
     * @param dbName      数据库名称
     * @param querytype   查询类型 typehot,type,ishot,no,
     * @param queryString 查询关键字
     * @return 查询结果
     */
    public List<ItemBean> queryData(Context context, String dbName, String querytype, String queryString) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<ItemBean> data = new ArrayList<>();

        try {
            //初始化数据库
            WantOrderDbHelper orderDbHelper = new WantOrderDbHelper(context, dbName, null, 1);

            //设置为读的数据库
            db = orderDbHelper.getReadableDatabase();

            switch (querytype) {
                //查询点单号
                case "itemcode":
                    cursor = db.query("orderlist", null, "itemcode=?", new String[]{queryString}, null, null, "id asc");
                    break;
                case "all":
                    cursor = db.query("orderlist", null, null, null, null, null, null);
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
                    ItemBean bean = new ItemBean();
                    //具体赋值

                    bean.setItemcode(cursor.getString(cursor.getColumnIndex("itemcode")));
                    bean.setBarcode(cursor.getString(cursor.getColumnIndex("barcode")));
                    bean.setItemname(cursor.getString(cursor.getColumnIndex("itemname")));
                    bean.setDeptcode(cursor.getString(cursor.getColumnIndex("deptcode")));
                    bean.setGroupfmcode(cursor.getString(cursor.getColumnIndex("groupfmcode")));
                    bean.setFamilycode(cursor.getString(cursor.getColumnIndex("familycode")));
                    bean.setSubfmcode(cursor.getString(cursor.getColumnIndex("subfmcode")));
                    bean.setCapacity(cursor.getString(cursor.getColumnIndex("capacity")));
                    bean.setSupptype(cursor.getString(cursor.getColumnIndex("supptype")));
                    bean.setIshot(cursor.getString(cursor.getColumnIndex("ishot")));
                    bean.setRprice(cursor.getString(cursor.getColumnIndex("rprice")));
                    bean.setPurprice(cursor.getString(cursor.getColumnIndex("purprice")));
                    bean.setMinmpoqty(cursor.getString(cursor.getColumnIndex("minmpoqty")));
                    bean.setPacksize(cursor.getString(cursor.getColumnIndex("packsize")));
                    bean.setStockunit(cursor.getString(cursor.getColumnIndex("stockunit")));
                    bean.setQty(cursor.getString(cursor.getColumnIndex("qty")));

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
}
