package com.sk.pda.parts.want.bean;

/**
 * Created by Administrator on 2018/1/24.
 */

public class PostItemBean {
    private String itemCode;
    private String vendCode;
    private Double qty;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getVendCode() {
        return vendCode;
    }

    public void setVendCode(String vendCode) {
        this.vendCode = vendCode;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}
