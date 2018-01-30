package com.sk.pda.parts.want.sql;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sk.pda.bean.ItemBean;
import com.sk.pda.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作类
 */
public class ItemModelDao {

    private String TAG = "want.ProductModelDao类";

    /**
     * Gson解析数组到对象中去
     *
     * @param jsonString 传入的json字符串
     * @return 返回数组对象
     */
    public List<ItemBean> addItemJsonObjectToList(String jsonString) {

        //实例化返回的数组对象
        List<ItemBean> listProductModel = null;

        //开始转换
        try {
            java.lang.reflect.Type type = new TypeToken<List<ItemBean>>() {
            }.getType();
            Gson gson = new Gson();
            listProductModel = gson.fromJson(jsonString, type);
        } catch (Exception e) {
            Log.e(TAG, "错误：" + e.getMessage());
        }
        //返回数组
        return listProductModel;
    }


    /**
     * 查询
     *
     * @param querytype   查询类型 typehot,type,ishot,no,
     * @param queryString 查询关键字
     * @return 查询结果
     */
    public List<ItemBean> queryData(String querytype, String queryString) {
        String dbPath = Constants.ITEMINFO;
        SQLiteDatabase db = null;
        Cursor cursor;
        List<ItemBean> data = new ArrayList<ItemBean>();

        try {
            Log.e(TAG, "queryData: 开始打开数据库" );
            //初始化数据库
           db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);

            switch (querytype) {
                //查询产品列表
                case "list":
                    cursor = db.query("item", null, null, null, null, null, null);
                    break;

                case "dept":
                    cursor = db.query("item", null, "groupfmcode=?", new String[]{queryString}, null, null, null);
                    break;

                case "default":
                    cursor = db.query("item", null, "deptcode=?", new String[]{queryString}, null, null, null);
                    break;
                case "dcishot":
                    cursor = db.query("item", null, "ishot=? & supptype=?", new String[]{"1","DC"}, null, null, null);
                    break;
                case "dsishot":
                    cursor = db.query("item", null, "ishot=? and supptype=?", new String[]{"1","DS"}, null, null, null);
                    break;

                case"supptype":
                    cursor = db.query("item", null, "supptype=?", new String[]{queryString}, null, null, null);
                    break;

                case "name":
                    cursor = db.query("item", null, "itemname like ?", new String[]{"%" + queryString + "%"}, null, null, null);
                    break;

                case "barcode":
                    cursor = db.query("item", null, "barcode =?", new String[]{queryString}, null, null, null);
                    break;
                default:
                    cursor = db.query("item", null, null, null, null, null, null);
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

                    //itemname具体赋值
                    if (cursor.getString(cursor.getColumnIndex("itemname")) != null) {
                        bean.setItemname(cursor.getString(cursor.getColumnIndex("itemname")));
                    } else {
                        bean.setItemname("");
                    }

                    //itemcode具体赋值
                    if (cursor.getString(cursor.getColumnIndex("itemcode")) != null) {
                        bean.setItemcode(cursor.getString(cursor.getColumnIndex("itemcode")));
                    } else {
                        bean.setItemcode("");
                    }

                    //barcode具体赋值
                    if (cursor.getString(cursor.getColumnIndex("barcode")) != null) {
                        bean.setBarcode(cursor.getString(cursor.getColumnIndex("barcode")));
                    } else {
                        bean.setBarcode("");
                    }

                    //deptcode具体赋值
                    if (cursor.getString(cursor.getColumnIndex("deptcode")) != null) {
                        bean.setDeptcode(cursor.getString(cursor.getColumnIndex("deptcode")));
                    } else {
                        bean.setDeptcode("");
                    }

                    //groupfmcode具体赋值
                    if (cursor.getString(cursor.getColumnIndex("groupfmcode")) != null) {
                        bean.setGroupfmcode(cursor.getString(cursor.getColumnIndex("groupfmcode")));
                    } else {
                        bean.setGroupfmcode("");
                    }
                    //familycode具体赋值
                    if (cursor.getString(cursor.getColumnIndex("familycode")) != null) {
                        bean.setFamilycode(cursor.getString(cursor.getColumnIndex("familycode")));
                    } else {
                        bean.setFamilycode("");
                    }

                    //subfmcode具体赋值
                    if (cursor.getString(cursor.getColumnIndex("subfmcode")) != null) {
                        bean.setSubfmcode(cursor.getString(cursor.getColumnIndex("subfmcode")));
                    } else {
                        bean.setSubfmcode("");
                    }

                    //ishot具体赋值
                    if (cursor.getString(cursor.getColumnIndex("ishot")) != null) {
                        bean.setIshot(cursor.getString(cursor.getColumnIndex("ishot")));
                    } else {
                        bean.setIshot("");
                    }

                    //supptype具体赋值
                    if (cursor.getString(cursor.getColumnIndex("supptype")) != null) {
                        bean.setSupptype(cursor.getString(cursor.getColumnIndex("supptype")));
                    } else {
                        bean.setSupptype("");
                    }

                    //rprice具体赋值
                    if (cursor.getString(cursor.getColumnIndex("rprice")) != null) {
                        bean.setRprice(cursor.getString(cursor.getColumnIndex("rprice")));
                    } else {
                        bean.setRprice("");
                    }

                    //purprice具体赋值
                    if (cursor.getString(cursor.getColumnIndex("purprice")) != null) {
                        bean.setPurprice(cursor.getString(cursor.getColumnIndex("purprice")));
                    } else {
                        bean.setPurprice("");
                    }

                    //capacity具体赋值
                    if (cursor.getString(cursor.getColumnIndex("capacity")) != null) {
                        bean.setCapacity(cursor.getString(cursor.getColumnIndex("capacity")));
                    } else {
                        bean.setCapacity("");
                    }

                    //packsize具体赋值
                    if (cursor.getString(cursor.getColumnIndex("packsize")) != null) {
                        bean.setPacksize(cursor.getString(cursor.getColumnIndex("packsize")));
                    } else {
                        bean.setPacksize("");
                    }

                    //stockunit具体赋值
                    if (cursor.getString(cursor.getColumnIndex("stockunit")) != null) {
                        bean.setStockunit(cursor.getString(cursor.getColumnIndex("stockunit")));
                    } else {
                        bean.setStockunit("");
                    }

                    //minmpoqty具体赋值
                    if (cursor.getString(cursor.getColumnIndex("minmpoqty")) != null) {
                        bean.setMinmpoqty(cursor.getString(cursor.getColumnIndex("minmpoqty")));
                    } else {
                        bean.setMinmpoqty("");
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
