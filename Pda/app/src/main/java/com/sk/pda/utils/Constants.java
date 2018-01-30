package com.sk.pda.utils;

public class Constants {

    //本地调试ip
//    public static final String BASE = "http://192.168.2.109";


    //Webservice地址
    public static final String WEBSERVICE_WDSL_LINK = "http://61.184.78.105:8090/axis2/services/pdawebservice?wsdl";
    //Webservice命名空间
    public static final String WEBSERVICE_NAME_SPACE = "http://pdaService.pdawebservice";

    //远程服务器ip
    public static final String BASE = "http://skpda.syspaq.com";

    // 请求图片基本URL
    public static final String BASE_URl_IMAGE = BASE + "/newimg";


    public static final String BASE_URL_GETPRICE = BASE + "/index/index/getprice";
    public static final String BASE_URL_LOGIN = BASE + "/index/index/valiuser";
    //*******************************************************************************
    //产品数据更新地址
    public static final String BASE_URL_ITEM = BASE + "/item/getall";
    public static final String BASE_URL_VEND = BASE + "/vend/getall";
    public static final String BASE_URL_VENDPRICE = BASE + "/vendprice/getall";
    public static final String BASE_URL_BARCODE = BASE + "/barcode/getall";
    //版本更新地址
    public static final String BASE_URL_VERSION = BASE + "/version/";
    //帮助文件地址
    public static final String BASE_URL_HELP = BASE + "/help/";
    //****************************************************************************

    public static final String BASE_URL_SUBMIT_ORDER =BASE + "/index/index/postorder";

    public static final String ITEMINFO= "/data/data/com.sk.pda/files/aaa.db";



    //************************************************************
    public static final String BASE_URL_GET_DC_GETNO=BASE+"/newjson/getgetdc.json";
    public static final String BASE_URL_GET_DS_GETNO=BASE+"/newjson/getgetds.json";
    public static final String BASE_URL_GET_NO_DETAIL=BASE+"/newjson/getNoDetail.json";





}


