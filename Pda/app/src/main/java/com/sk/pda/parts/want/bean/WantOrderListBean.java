package com.sk.pda.parts.want.bean;

//订单bean
public class WantOrderListBean {
    //类型
    private String type;

    //下单时间
    private String orderTime;

    //要货时间
    private String needTime;

    //是否已经下单
    private String isOrdered;

    //订单数据表名字
    private String orderDbName;



    public String getNeedTime() {
        return needTime;
    }

    public void setNeedTime(String needTime) {
        this.needTime = needTime;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getIsOrdered() {
        return isOrdered;
    }

    public void setIsOrdered(String isOrdered) {
        this.isOrdered = isOrdered;
    }

    public String getOrderDbName() {
        return orderDbName;
    }

    public void setOrderDbName(String orderDbName) {
        this.orderDbName = orderDbName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
