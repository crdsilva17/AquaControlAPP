package com.example.crdsilva;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Config extends AppCompatActivity {

    private Button mInicio, mLed, mConf, btn_menu, mProg, mSaveIp, mBtnCancel, mBtnConfirma, mBtnDf, mBtnR;
    private EditText mEditIp, mServerNTP,mPortNTP, mTimeZoneNTP;
    private Gerenciador gr = new Gerenciador();
    private String url;
    private RequestPage requestPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_config);
        mInicio = (Button) findViewById(R.id.txtInicio);
        mConf = (Button) findViewById(R.id.txtConf);
        mLed = (Button) findViewById(R.id.txtLed);
        mProg = (Button) findViewById(R.id.txtProg);
        btn_menu = (Button) findViewById(R.id.btnMenu);
        mSaveIp = (Button) findViewById(R.id.btn_save_ip);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel_ntp);
        mBtnConfirma = (Button) findViewById(R.id.btn_confirma_ntp);
        mBtnDf = (Button) findViewById(R.id.btn_default_reset);
        mBtnR = (Button) findViewById(R.id.btn_reinicia);
        mEditIp = (EditText) findViewById(R.id.edtIP);
        mServerNTP = (EditText) findViewById(R.id.serverNtp);
        mPortNTP = (EditText) findViewById(R.id.portNtp);
        mTimeZoneNTP = (EditText) findViewById(R.id.timezoneNtp);
        url = "null";
        url = gr.Read(getApplicationContext(),"IP");
        try {
            if (!url.equals("null"))
                mEditIp.setText(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestPage = new RequestPage(getApplicationContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        requestPage.sendRequestJson(url + "/conf.json", new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                mServerNTP.setText(response.getString("ntpServer"));
                mPortNTP.setText(response.getString("ntpPort"));
                mTimeZoneNTP.setText(response.getString("ntpZone"));
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPage.sendRequestJson(url + "/conf.json", new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                        mServerNTP.setText(response.getString("ntpServer"));
                        mPortNTP.setText(response.getString("ntpPort"));
                        mTimeZoneNTP.setText(response.getString("ntpZone"));
                    }
                });
            }
        });
        mBtnConfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPage.sendRequestPost(url+"/clock", "ntpServer", String.
                        valueOf(mServerNTP.getText()),"port", String.valueOf(mPortNTP.getText()),
                        "ntpZone",String.valueOf(mTimeZoneNTP.getText()));
            }
        });

        mBtnDf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //requestPage.sendRequestPost(url+"/reset", "reset", "default");
                builder.setMessage("Deseja formatar dispositivo?");
                builder.setTitle("Formatar");
                builder.setCancelable(true);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPage.sendRequestPost(url+"/reset","reset",
                                "default");

                        Toast.makeText(getApplicationContext(),"Formatou!",Toast.LENGTH_SHORT).
                                show();
                    }
                });
                builder.create().show();
            }
        });

        mBtnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //requestPage.sendRequestPost(url+"/reset", "reset", "again");
                builder.setMessage("Deseja reiniciar o dispositivo?");
                builder.setTitle("Reiniciar");
                builder.setCancelable(true);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPage.sendRequestPost(url+"/reset","reset",
                                "again");

                        Toast.makeText(getApplicationContext(),"Reiniciou!",Toast.LENGTH_SHORT).
                                show();
                    }
                });
                builder.create().show();
            }
        });

        mSaveIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c = String.valueOf(mEditIp.getText());
                gr.write(getApplicationContext(), c);
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout.LayoutParams params;
                LinearLayout l_menu = findViewById(R.id.list_menu);
                if(l_menu.getLayoutParams().height == LinearLayout.LayoutParams.WRAP_CONTENT){
                    params = new LinearLayout.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, 0);
                }else{
                    params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                }

                l_menu.setLayoutParams(params);
            }
        });

        mInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finishAfterTransition();
                startActivity(intent);
            }
        });
        mProg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Program.class);
                finishAfterTransition();
                startActivity(intent);
            }
        });

        mConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, 0);;
                LinearLayout l_menu = findViewById(R.id.list_menu);
                l_menu.setLayoutParams(params);
            }
        });

        mLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Led.class);
                finishAfterTransition();
                startActivity(intent);
            }
        });
    }
}