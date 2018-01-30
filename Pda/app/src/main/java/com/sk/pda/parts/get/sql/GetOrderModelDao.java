package com.sk.pda.parts.get.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sk.pda.parts.get.bean.GetItemBean;

import java.util.ArrayList;
import java.util.List;

public class GetOrderModelDao {
    private String TAG = "get.OrderModelDao类";


    public List<GetItemBean> addItemJsonObjectToList(String jsonString) {

        //实例化返回的数组对象
        List<GetItemBean> itemBeanList = new ArrayList<>();

        //开始转换
        try {
            itemBeanList = JSON.parseArray(jsonString, GetItemBean.class);
        } catch (Exception e) {
            Log.e(TAG, "错误：" + e.getMessage());
        }
        //返回数组
        return itemBeanList;
    }

    public Boolean insertOrderModelToDb(Context context, String dbName, List<GetItemBean> orderModelList) {
        SQLiteDatabase db = null;

        try {
            GetOrderDbHelper orderdbHelper = new GetOrderDbHelper(context, dbName, null, 1);
            db = orderdbHelper.getWritableDatabase();

            //开始事务
            db.beginTransaction();
            Log.e(TAG, orderModelList.size() + "条订单数据数据，准备插入");
            String sql1 = "insert into getorder(" +
                    "id," +
                    "itemcode," +
                    "barcode," +
                    "itemname," +
                    "capacity," +
                    "stockunit," +
                    "packsize," +
                    "rprice," +
                    "purprice," +
                    "qty," +
                    "num" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?)";
            for (GetItemBean f : orderModelList) {
                db.execSQL(sql1, new Object[]{
                        null,
                        f.getItemcode(),
                        f.getBarcode(),
                        f.getItemname(),
                        f.getCapacity(),
                        f.getStockunit(),
                        f.getPacksize(),
                        f.getRprice(),
                        f.getPurprice(),
                        f.getQty(),
                        null
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
     * 查询数据库单个item对象
     *
     * @param context
     * @param dbName
     * @param barcode
     * @return
     */
    public GetItemBean querySigleItemData(Context context, String dbName, String barcode) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        GetItemBean getItemBean = new GetItemBean();
        try {
            //初始化数据库
            GetOrderDbHelper dbHelper = new GetOrderDbHelper(context, dbName, null, 1);

            //设置为读的数据库
            db = dbHelper.getReadableDatabase();

            cursor = db.query("getorder", null, "barcode = ?", new String[]{barcode}, null, null, null);

            //将查询结果复制到返回对象
            if (cursor != null) {
                cursor.moveToFirst();
                getItemBean.setItemcode(cursor.getString(cursor.getColumnIndex("itemcode")));
                getItemBean.setBarcode(cursor.getString(cursor.getColumnIndex("barcode")));
                getItemBean.setItemname(cursor.getString(cursor.getColumnIndex("itemname")));
                getItemBean.setCapacity(cursor.getString(cursor.getColumnIndex("capacity")));
                getItemBean.setStockunit(cursor.getString(cursor.getColumnIndex("stockunit")));
                getItemBean.setPacksize(cursor.getFloat(cursor.getColumnIndex("packsize")));
                getItemBean.setRprice(cursor.getFloat(cursor.getColumnIndex("rprice")));
                getItemBean.setPurprice(cursor.getFloat(cursor.getColumnIndex("purprice")));
                getItemBean.setQty(cursor.getFloat(cursor.getColumnIndex("qty")));
                getItemBean.setNum(cursor.getFloat(cursor.getColumnIndex("num")));
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
        return getItemBean;
    }


    public int queryItemCount(Context context, String dbName) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int num = 0;
        try {
            //初始化数据库
            GetOrderDbHelper dbHelper = new GetOrderDbHelper(context, dbName, null, 1);

            //设置为读的数据库
            db = dbHelper.getReadableDatabase();

            cursor = db.query("getorder", null, null, null, null, null, null);

            //将查询结果复制到返回对象
            if (cursor != null) {
                num = cursor.getCount();
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
        return num;
    }


    /**
     * 更新单个商品的数量
     * @param context 容器
     * @param dbName 数据库名
     * @param queryString 条形码
     * @param newNum 新的数量
     * @return True,False
     */
    public int updateItemNum(Context context, String dbName, String queryString, float newNum) {
        SQLiteDatabase db = null;
        int updateFlag;
        GetOrderDbHelper orderDbHelper = new GetOrderDbHelper(context, dbName, null, 1);
        //修改SQL语句
        db = orderDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("num", newNum);
        updateFlag = db.update("getorder", values, "barcode=?", new String[]{queryString});
        //执行SQL
        return updateFlag;
    }



    /**
     * 请求数据
     *
     * @param context 容器
     * @param dbName  数据库名字
     * @param type    类型（直送和配送）
     * @return
     */
    public List<GetItemBean> queryData(Context context, String dbName, String type) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<GetItemBean> data = new ArrayList<>();

        try {
            //初始化数据库
            GetOrderDbHelper orderDbHelper = new GetOrderDbHelper(context, dbName, null, 1);

            //设置为读的数据库
            db = orderDbHelper.getReadableDatabase();

            cursor = db.query("getorder", null, null, null, null, null, null);

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
                    GetItemBean bean = new GetItemBean();


                    if (cursor.getString(cursor.getColumnIndex("itemcode")) != null) {
                        bean.setItemcode(cursor.getString(cursor.getColumnIndex("itemcode")));
                    } else {
                        bean.setItemcode("");
                    }

                    if (cursor.getString(cursor.getColumnIndex("barcode")) != null) {
                        bean.setBarcode(cursor.getString(cursor.getColumnIndex("barcode")));
                    } else {
                        bean.setBarcode("");
                    }

                    if (cursor.getString(cursor.getColumnIndex("itemname")) != null) {
                        bean.setItemname(cursor.getString(cursor.getColumnIndex("itemname")));
                    } else {
                        bean.setItemname("");
                    }

                    if (cursor.getString(cursor.getColumnIndex("capacity")) != null) {
                        bean.setCapacity(cursor.getString(cursor.getColumnIndex("capacity")));
                    } else {
                        bean.setCapacity("");
                    }

                    if (cursor.getString(cursor.getColumnIndex("stockunit")) != null) {
                        bean.setStockunit(cursor.getString(cursor.getColumnIndex("stockunit")));
                    } else {
                        bean.setStockunit("");
                    }

                    if (cursor.getString(cursor.getColumnIndex("packsize")) != null) {
                        bean.setPacksize(cursor.getFloat(cursor.getColumnIndex("packsize")));
                    } else {
                        bean.setPacksize(0f);
                    }

                    if (cursor.getString(cursor.getColumnIndex("rprice")) != null) {
                        bean.setRprice(cursor.getFloat(cursor.getColumnIndex("rprice")));
                    } else {
                        bean.setRprice(0f);
                    }

                    if (cursor.getString(cursor.getColumnIndex("purprice")) != null) {
                        bean.setPurprice(cursor.getFloat(cursor.getColumnIndex("purprice")));
                    } else {
                        bean.setPurprice(0f);
                    }

                    if (cursor.getString(cursor.getColumnIndex("qty")) != null) {
                        bean.setQty(cursor.getFloat(cursor.getColumnIndex("qty")));
                    } else {
                        bean.setQty(0f);
                    }

                    if (cursor.getString(cursor.getColumnIndex("num")) != null) {
                        bean.setNum(cursor.getFloat(cursor.getColumnIndex("num")));
                    } else {
                        bean.setNum(0f);
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
}
