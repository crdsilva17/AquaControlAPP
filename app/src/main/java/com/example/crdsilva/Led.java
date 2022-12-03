package com.example.crdsilva;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

import java.util.Objects;

public class Led extends AppCompatActivity {

    private String url;
    private RequestPage requestPage;
    private CheckBox mSun;
    private RadioButton mRman, mRauto;
    private Slider mSlider;
    private final Gerenciador gr = new Gerenciador();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_led);

        url = gr.read(getApplicationContext(),"IP");

        requestPage = new RequestPage(this);

        Button mInicio = findViewById(R.id.txtInicio);
        Button mConf = findViewById(R.id.txtConf);
        Button mLed = findViewById(R.id.txtLed);
        Button mProg = findViewById(R.id.txtProg);
        Button btn_menu = findViewById(R.id.btnMenu);

        mSun = findViewById(R.id.sun);
        mRman = findViewById(R.id.radio_man);
        mRauto = findViewById(R.id.radio_auto);
        mSlider = findViewById(R.id.slider);
        RadioGroup mRadioGroup = findViewById(R.id.radio_group);

        mRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            float x = mSlider.getValue();
            if(x > 10)
                x = (float) ((x - 10)/0.01);
            sendStatus(x);
        });

        mSun.setOnCheckedChangeListener((compoundButton, b) -> {
            float x = mSlider.getValue();
            if(x > 10)
                x = (float) ((x - 10)/0.01);
            sendStatus(x);
        });

        mSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                float x = slider.getValue();
                if(x > 10)
                    x = (float) ((x - 10)/0.01);
                sendStatus(x);
            }
        });



        requestPage.sendRequestJson(url + "/", response -> {
            mSun.setChecked(response.getBoolean("sun"));
            mSlider.setValue((float) ((response.getInt("delay") * 0.01) + 10));
            if (response.getString("Led").equals("sequencia")) {
                mRauto.setChecked(true);
            } else {
                mRman.setChecked(true);
            }
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

        mLed.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Led.class);
            finishAfterTransition();
            startActivity(intent);
        });

        mConf.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Config.class);
            finishAfterTransition();
            startActivity(intent);
        });
    }


    public void sendStatus(float x) {

        requestPage.sendRequestPost(
                url + "/led", "estado", mRauto.isChecked() ? "sequencia" : "manual",
                "speed", Float.toString(x), "sun", mSun.isChecked() ? "1" : "0"
        );

    }
}