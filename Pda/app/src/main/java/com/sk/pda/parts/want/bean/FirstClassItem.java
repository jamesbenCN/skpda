package com.sk.pda.parts.want.bean;

import java.util.List;

/**
 * 一级分类，相当于左侧菜单
 * Created by hanj on 14-9-25.
 */
public class FirstClassItem {
    private String code;
    private String name;
    private List<SecondClassItem> secondList;

    public FirstClassItem() {

    }

    public FirstClassItem(String code, String name, List<SecondClassItem> secondList) {
        this.code = code;
        this.name = name;
        this.secondList = secondList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SecondClassItem> getSecondList() {
        return secondList;
    }

    public void setSecondList(List<SecondClassItem> secondList) {
        this.secondList = secondList;
    }

    @Override
    public String toString() {
        return "FirstClassItem{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", secondList=" + secondList +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
