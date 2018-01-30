package com.sk.pda.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.sk.pda.parts.want.bean.PostItemBean;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class CallWebService {

    private static final String NAME_SPACE = "http://pdaService.pdawebservice";
    private static final String WDSL_LINK = "http://61.184.78.105:8090/axis2/services/pdawebservice?wsdl";


    public void userVaild(String userId,String userPsw) {
        final String METHOD_NAME = "userVaild";
        Thread thr = new Thread() {
            public void run() {
                try {
                    JSONObject jsonObject=new  JSONObject();

                    jsonObject.put("userId",userId);
                    jsonObject.put("userPsw",userPsw);

                    String jsonStr=jsonObject.toString();

                    SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
                    Log.e("xxx", "run: "+ jsonStr );
                    request.addProperty("jsonRequest",jsonStr);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.bodyOut = request;
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE ht = new HttpTransportSE(WDSL_LINK);

                    ht.call("", envelope);

                    String ret = String.valueOf(envelope.getResponse());

                    Log.d("resultStr = ", ret);

                } catch (SoapFault e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        };
        thr.start();
    }


    public void sendMarketPo(String deptCode, String storeCode, String persCode, String needTime, String hTNo, List<PostItemBean> postItemBeanList) {
        final String METHOD_NAME = "sendMarketPo";
        Thread thr = new Thread() {
            public void run() {
                try {
                    JSONObject jsonObject=new  JSONObject();

                    jsonObject.put("deptCode",deptCode);
                    jsonObject.put("storeCode",storeCode);
                    jsonObject.put("persCode",persCode);
                    jsonObject.put("needTime",needTime);
                    jsonObject.put("hTNo",hTNo);
                    jsonObject.put("itemList",postItemBeanList);

                    String jsonStr=jsonObject.toString();

                    SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
                    Log.e("xxx", "run: "+ jsonStr );
                    request.addProperty("jsonRequest",jsonStr);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.bodyOut = request;
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE ht = new HttpTransportSE(WDSL_LINK);

                    ht.call("", envelope);

                    String ret = String.valueOf(envelope.getResponse());

                    Log.d("resultStr = ", ret);

                } catch (SoapFault e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        };
        thr.start();
    }
}
