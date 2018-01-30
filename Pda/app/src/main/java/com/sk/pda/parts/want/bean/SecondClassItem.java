package com.sk.pda.parts.want.bean;

/**
 * 二级分类，相当于中间菜单
 * Created by hanj on 14-9-25.
 */
public class SecondClassItem {
    private String code;
    private String name;

    public SecondClassItem() {

    }

    public SecondClassItem(String code, String name) {
        this.code = code;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SecondClassItem{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
