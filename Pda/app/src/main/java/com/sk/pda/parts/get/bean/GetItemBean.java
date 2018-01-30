package com.sk.pda.parts.get.bean;

/**
 * Created by Administrator on 2018/1/7.
 */

public class GetItemBean {
    private String itemcode;
    private String barcode;
    private String itemname;
    private String capacity;
    private String stockunit;
    private float packsize;
    private float rprice;
    private float purprice;
    private float qty;
    private float num;

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

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getStockunit() {
        return stockunit;
    }

    public void setStockunit(String stockunit) {
        this.stockunit = stockunit;
    }

    public float getPacksize() {
        return packsize;
    }

    public void setPacksize(float packsize) {
        this.packsize = packsize;
    }

    public float getRprice() {
        return rprice;
    }

    public void setRprice(float rprice) {
        this.rprice = rprice;
    }

    public float getPurprice() {
        return purprice;
    }

    public void setPurprice(float purprice) {
        this.purprice = purprice;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }
}
