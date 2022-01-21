package com.example.crdsilva;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity{
    private Switch btnPump;
    private String url;
    private View pC;
    private Button btn_menu;
    private RequestPage requestPage;
    private Button mInicio, mProg, mLed, mConf;
    private Gerenciador gr = new Gerenciador();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        url = gr.Read(getApplicationContext(),"IP");
        requestPage = new RequestPage(this);

        btnPump = findViewById(R.id.btn_pump);
        pC = findViewById(R.id.led);
        btn_menu = (Button) findViewById(R.id.btnMenu);
        ColorPickerFrag mColorPicker = new ColorPickerFrag();
        mInicio = (Button) findViewById(R.id.txtInicio);
        mProg = (Button) findViewById(R.id.txtProg);
        mLed = (Button) findViewById(R.id.txtLed);
        mConf = (Button) findViewById(R.id.txtConf);

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

        mLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Led.class);
                finishAfterTransition();
                startActivity(intent);
            }
        });

        mConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Config.class);
                finishAfterTransition();
                startActivity(intent);
            }
        });

        pC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.tela).setVisibility(View.INVISIBLE);
                findViewById(R.id.menu).setVisibility(View.INVISIBLE);
                if(!mColorPicker.isAdded())
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.principal, mColorPicker).commit();


            }
        });




        btnPump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(btnPump.isChecked()){
                    if(!(url.isEmpty())) {
                        requestPage.sendRequestPost(url + "/pump", "setPump",
                                "on");
                    }
                }else {
                    if (!(url.isEmpty())) {
                        requestPage.sendRequestPost(url + "/pump", "setPump",
                                "off");
                    }
                }
            }
        });
        requestPage.sendRequestJson(url + "/home.json", new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    btnPump.setChecked(response.getBoolean("Pump"));
                    pC.setBackgroundColor(Color.rgb(response.getInt("cor_r"),
                            response.getInt("cor_g"), response.getInt("cor_b")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void onColorSend(int cor_r, int cor_g, int cor_b, String cor){
        pC.setBackgroundColor(Color.rgb(cor_r, cor_g, cor_b));

        requestPage.sendRequestPost(url + "/led", "cor", cor);

    }

}