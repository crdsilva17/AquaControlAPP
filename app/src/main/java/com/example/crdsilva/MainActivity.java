package com.example.crdsilva;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import org.json.JSONException;

import java.util.Objects;

public class MainActivity extends AppCompatActivity{
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch btnPump;
    private String url;
    private View pC;
    private RequestPage requestPage;
    private final Gerenciador gr = new Gerenciador();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        url = gr.read(getApplicationContext(),"IP");
        requestPage = new RequestPage(this);

        btnPump = findViewById(R.id.btn_pump);
        pC = findViewById(R.id.led);
        Button btn_menu = findViewById(R.id.btnMenu);
        ColorPickerFrag mColorPicker = new ColorPickerFrag();
        Button mInicio = findViewById(R.id.txtInicio);
        Button mProg = findViewById(R.id.txtProg);
        Button mLed = findViewById(R.id.txtLed);
        Button mConf = findViewById(R.id.txtConf);

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

        pC.setOnClickListener(view -> {
            findViewById(R.id.tela).setVisibility(View.INVISIBLE);
            findViewById(R.id.menu).setVisibility(View.INVISIBLE);
            if(!mColorPicker.isAdded())
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.principal, mColorPicker).commit();


        });




        btnPump.setOnCheckedChangeListener((compoundButton, b) -> {
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
        });
        requestPage.sendRequestJson(url + "/", response -> {
            try {
                btnPump.setChecked(response.getBoolean("Pump"));
                pC.setBackgroundColor(Color.rgb(response.getInt("cor_r"),
                        response.getInt("cor_g"), response.getInt("cor_b")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


    }

    public void onColorSend(int cor_r, int cor_g, int cor_b, String cor){
        pC.setBackgroundColor(Color.rgb(cor_r, cor_g, cor_b));

        requestPage.sendRequestPost(url + "/led", "cor", cor);

    }

}