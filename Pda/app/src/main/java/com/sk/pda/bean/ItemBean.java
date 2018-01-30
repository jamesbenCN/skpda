package com.sk.pda.bean;

/**
 * Created by Administrator on 2017/12/11.
 */

//商品列表
public class ItemBean {
    //是否被选中
    private boolean isChildSelected;
    //商品编码
    private String itemcode;
    //商品主条形码
    private String barcode;
    //商品名称
    private String itemname;
    //商品所属部门编码
    private String deptcode;
    //商品大类编码
    private String groupfmcode;
    //商品种类编码
    private String familycode;
    //商品小类编码
    private String subfmcode;
    //规格,如：400ML
    private String capacity;
    //直送还是配送，配送DC,直送DS
    private String supptype;
    //商品是否热卖
    private String ishot;
    //商品建议售价
    private String rprice;
    //商品配送价格
    private String purprice;
    //主供应商
    private String vendcode;
    //最小要货量
    private String minmpoqty;
    //包装含量
    private String packsize;
    //单位：斤/袋
    private String stockunit;

    //要货的数量
    private String qty;

    //图片
    private String figure;

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getGroupfmcode() {
        return groupfmcode;
    }

    public void setGroupfmcode(String groupfmcode) {
        this.groupfmcode = groupfmcode;
    }

    public String getFamilycode() {
        return familycode;
    }

    public void setFamilycode(String familycode) {
        this.familycode = familycode;
    }

    public String getSubfmcode() {
        return subfmcode;
    }

    public void setSubfmcode(String subfmcode) {
        this.subfmcode = subfmcode;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getSupptype() {
        return supptype;
    }

    public void setSupptype(String supptype) {
        this.supptype = supptype;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public String getRprice() {
        return rprice;
    }

    public void setRprice(String rprice) {
        this.rprice = rprice;
    }

    public String getDoubleRprice(){
        float doublerprice=Float.parseFloat( this.rprice);
        String result =String.format("%.2f", doublerprice);
        return result;
    }
    public String getPurprice() {
        return purprice;
    }



    public void setPurprice(String purprice) {
        this.purprice = purprice;
    }

    public String getDoublePurprice(){
        float doublePurprice=Float.parseFloat( this.purprice);
        String result =String.format("%.2f", doublePurprice);
        return result;
    }

    public String getVendcode() {
        return vendcode;
    }

    public void setVendcode(String vendcode) {
        this.vendcode = vendcode;
    }

    public String getMinmpoqty() {
        return minmpoqty;
    }

    public void setMinmpoqty(String minmpoqty) {
        this.minmpoqty = minmpoqty;
    }

    public String getPacksize() {
        return packsize;
    }

    public void setPacksize(String packsize) {
        this.packsize = packsize;
    }

    public String getStockunit() {
        return stockunit;
    }

    public void setStockunit(String stockunit) {
        this.stockunit = stockunit;
    }


    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public boolean isChildSelected() {
        return isChildSelected;
    }

    public void setChildSelected(boolean childSelected) {
        isChildSelected = childSelected;
    }
}