package com.example.crdsilva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.slider.Slider;

import org.json.JSONException;
import org.json.JSONObject;

public class Led extends AppCompatActivity {

    private String url;
    private RequestPage requestPage;
    private Button mInicio, mProg, mLed, mConf, btn_menu;
    private CheckBox mSun;
    private RadioButton mRman, mRauto;
    private Slider mSlider;
    private Gerenciador gr = new Gerenciador();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_led);

        url = gr.Read(getApplicationContext(),"IP");

        requestPage = new RequestPage(this);

        mInicio = (Button) findViewById(R.id.txtInicio);
        mConf = (Button) findViewById(R.id.txtConf);
        mLed = (Button) findViewById(R.id.txtLed);
        mProg = (Button) findViewById(R.id.txtProg);
        btn_menu = (Button) findViewById(R.id.btnMenu);

        mSun = (CheckBox) findViewById(R.id.sun);
        mRman = (RadioButton) findViewById(R.id.radio_man);
        mRauto = (RadioButton) findViewById(R.id.radio_auto);
        mSlider = (Slider) findViewById(R.id.slider);
        RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                float x = mSlider.getValue();
                if(x > 10)
                    x = (float) ((x - 10)/0.01);
                sendStatus(x);
            }
        });

        mSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                float x = mSlider.getValue();
                if(x > 10)
                    x = (float) ((x - 10)/0.01);
                sendStatus(x);
            }
        });

        mSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                float x = slider.getValue();
                if(x > 10)
                    x = (float) ((x - 10)/0.01);
                sendStatus(x);
            }
        });



        requestPage.sendRequestJson(url + "/home.json", new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                mSun.setChecked(response.getBoolean("sun"));
                mSlider.setValue((float) ((response.getInt("delay") * 0.01) + 10));
                if (response.getString("Led").equals("sequencia")) {
                    mRauto.setChecked(true);
                } else {
                    mRman.setChecked(true);
                }
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
    }


    public void sendStatus(float x) {

        requestPage.sendRequestPost(
                url + "/led", "estado", mRauto.isChecked() ? "sequencia" : "manual",
                "speed", Float.toString(x), "sun", mSun.isChecked() ? "1" : "0"
        );

    }
}