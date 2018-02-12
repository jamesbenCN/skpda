package com.sk.pda.utils;

public class Constants {

    //********************************************************************************
    //Webservice地址
    public static final String WEBSERVICE_WDSL_LINK = "http://61.184.78.105:8090/axis2/services/pdawebservice?wsdl";
    //Webservice命名空间
    public static final String WEBSERVICE_NAME_SPACE = "http://pdaService.pdawebservice";

    //******************************************************************************
    //获取本地通用数据库的目录
    public static final String LOCAL_DB_PATH = "/data/data/com.sk.pda/files/";


    //********************************************************************************
    //远程调试服务器ip
    public static final String BASE = "http://skpda.syspaq.com";
    // 请求图片基本URL
    public static final String BASE_URl_IMAGE = BASE + "/newimg";
    //版本更新地址
    public static final String BASE_URL_VERSION = BASE + "/version/";
    //帮助文件地址
    public static final String BASE_URL_HELP = BASE + "/help/";

    //******************************************************************************
    //获取远程通用数据库的路径
    public static final String REMOTE_DB_PATH = BASE + "/test/";


    private static String LOCAL_DB;
    private static String REMOTE_DB;

    public static String getLocalDb() {
        return LOCAL_DB;
    }
    public static void setLocalDb(String localDb) {
        LOCAL_DB = localDb;
    }

    public static String getRemoteDb() {
        return REMOTE_DB;
    }
    public static void setRemoteDb(String remoteDb) {
        REMOTE_DB = remoteDb;
    }


    //************************************************************
    public static final String BASE_URL_GET_DC_GETNO = BASE + "/newjson/getgetdc.json";
    public static final String BASE_URL_GET_DS_GETNO = BASE + "/newjson/getgetds.json";
    public static final String BASE_URL_GET_NO_DETAIL = BASE + "/newjson/getNoDetail.json";



}


