package com.sk.pda.other.activty;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;



import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sk.pda.R;
import com.sk.pda.utils.CallWebService;

public class TestActivity extends Activity {


    private static final String NAME_SPACE = "http://pdaService.pdawebservice";
    private static final String WDSL_LINK = "http://61.184.78.105:8090/axis2/services/pdawebservice?wsdl";
    private static final String METHOD_NAME = "userVaild";

    private Button call_soap;
    private EditText name_input;
    private TextView textView;

    private String resultStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        this.initUI();

        this.call_soap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                (new CallWebService()).userVaild("14822","4823C45E494707C5");
            }
        });
    }



    private void initUI() {
        call_soap = (Button) this.findViewById(R.id.soap_button);
        name_input = (EditText) this.findViewById(R.id.name_input);
        textView = (TextView) this.findViewById(R.id.textView);
    }



}