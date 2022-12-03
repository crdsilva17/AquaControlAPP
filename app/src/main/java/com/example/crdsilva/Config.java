package com.example.crdsilva;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Config extends AppCompatActivity {

    private EditText mEditIp, mServerNTP,mPortNTP, mTimeZoneNTP;
    private final Gerenciador gr = new Gerenciador();
    private String url;
    private RequestPage requestPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_config);
        Button mInicio = findViewById(R.id.txtInicio);
        Button mConf = findViewById(R.id.txtConf);
        Button mLed = findViewById(R.id.txtLed);
        Button mProg = findViewById(R.id.txtProg);
        Button btn_menu = findViewById(R.id.btnMenu);
        Button mSaveIp = findViewById(R.id.btn_save_ip);
        Button mBtnCancel = findViewById(R.id.btn_cancel_ntp);
        Button mBtnConfirma = findViewById(R.id.btn_confirma_ntp);
        Button mBtnDf = findViewById(R.id.btn_default_reset);
        Button mBtnR = findViewById(R.id.btn_reinicia);
        mEditIp = findViewById(R.id.edtIP);
        mServerNTP = findViewById(R.id.serverNtp);
        mPortNTP = findViewById(R.id.portNtp);
        mTimeZoneNTP = findViewById(R.id.timezoneNtp);
        url = "null";
        url = gr.read(getApplicationContext(),"IP");
        try {
            if (!url.equals("null"))
                mEditIp.setText(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestPage = new RequestPage(getApplicationContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        requestPage.sendRequestJson(url + "/set.htm", response -> {
            mServerNTP.setText(response.getString("ntpServer"));
            mPortNTP.setText(response.getString("ntpPort"));
            mTimeZoneNTP.setText(response.getString("ntpZone"));
        });

        mBtnCancel.setOnClickListener(view -> requestPage.sendRequestJson(url + "/set.htm", response -> {
            mServerNTP.setText(response.getString("ntpServer"));
            mPortNTP.setText(response.getString("ntpPort"));
            mTimeZoneNTP.setText(response.getString("ntpZone"));
        }));
        mBtnConfirma.setOnClickListener(view -> requestPage.sendRequestPost(url+"/clock", "ntpServer", String.
                valueOf(mServerNTP.getText()),"port", String.valueOf(mPortNTP.getText()),
                "ntpZone",String.valueOf(mTimeZoneNTP.getText())));

        mBtnDf.setOnClickListener(view -> {
            //requestPage.sendRequestPost(url+"/reset", "reset", "default");
            builder.setMessage("Deseja formatar dispositivo?");
            builder.setTitle("Formatar");
            builder.setCancelable(true);
            builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
                requestPage.sendRequestPost(url+"/reset","reset",
                        "default");

                Toast.makeText(getApplicationContext(),"Formatou!",Toast.LENGTH_SHORT).
                        show();
            });
            builder.create().show();
        });

        mBtnR.setOnClickListener(view -> {
            //requestPage.sendRequestPost(url+"/reset", "reset", "again");
            builder.setMessage("Deseja reiniciar o dispositivo?");
            builder.setTitle("Reiniciar");
            builder.setCancelable(true);
            builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
                requestPage.sendRequestPost(url+"/reset","reset",
                        "again");

                Toast.makeText(getApplicationContext(),"Reiniciou!",Toast.LENGTH_SHORT).
                        show();
            });
            builder.create().show();
        });

        mSaveIp.setOnClickListener(view -> {
            String c = String.valueOf(mEditIp.getText());
            gr.write(getApplicationContext(), c);
        });

        btn_menu.setOnClickListener(view -> {
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
        });

        mInicio.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finishAfterTransition();
            startActivity(intent);
        });
        mProg.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Program.class);
            finishAfterTransition();
            startActivity(intent);
        });

        mConf.setOnClickListener(view -> {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, 0);
            LinearLayout l_menu = findViewById(R.id.list_menu);
            l_menu.setLayoutParams(params);
        });

        mLed.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Led.class);
            finishAfterTransition();
            startActivity(intent);
        });
    }
}