package com.example.crdsilva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class Program extends AppCompatActivity {
    private Button mInicio, mLed, mConf, btn_menu, mProg;
    private String url;
    private LinkedList<FragmentProgram>fragmentList = new LinkedList<>();
    private RequestPage requestPage;
    private int total = 0;
    private int[][] pgr = new int[5][7];
    private String[][] hr = new String[5][2];
    private FloatingActionButton button;
    private Gerenciador gr = new Gerenciador();

    public LinkedList<FragmentProgram> getFragmentList() {
        return fragmentList;
    }

    public int getTotal() {
        return total;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        url = gr.Read(getApplicationContext(),"IP");
        requestPage = new RequestPage(this);
        setContentView(R.layout.activity_program);
        mInicio = (Button) findViewById(R.id.txtInicio);
        mConf = (Button) findViewById(R.id.txtConf);
        mLed = (Button) findViewById(R.id.txtLed);
        mProg = (Button) findViewById(R.id.txtProg);
        btn_menu = (Button) findViewById(R.id.btnMenu);
        button = (FloatingActionButton) findViewById(R.id.btnAction);

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

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (total < 5) {
                    FragmentProgram frag =  new FragmentProgram();
                    frag.setId(total);
                    getSupportFragmentManager().beginTransaction().add(R.id.tela_hr,frag,Integer.toString(total)).commit();
                    fragmentList.add(frag);
                    total++;
                }
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


        mLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Led.class);
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
                Intent intent = new Intent(getApplicationContext(), Config.class);
                finishAfterTransition();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPage.sendRequestJson(url + "/prog.json", new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                total = response.getInt("count");
                JSONArray json = response.getJSONArray("pgr");
                JSONArray hrJson = response.getJSONArray("hr");
                for (int i = 0; i < json.length(); i++) {
                    JSONArray js = json.getJSONArray(i);
                    JSONArray jhr = hrJson.getJSONArray(i);
                    for (int j = 0; j < js.length(); j++) {
                        pgr[i][j] = js.getInt(j);
                    }
                    for (int k = 0; k < jhr.length(); k++) {
                        hr[i][k] = jhr.getString(k);
                    }
                }
                int i = 0;
                while (i < total) {
                    FragmentProgram frag = FragmentProgram.newInstance(hr[i],
                            pgr[i], i);
                    frag.setId(i);
                    fragmentList.add(frag);
                    i++;
                }
                for (FragmentProgram fragmentProgram : fragmentList) {
                    getSupportFragmentManager().beginTransaction().add(R.id.tela_hr,
                            fragmentProgram, fragmentProgram.getTag()).commit();
                }


            }

        });

    }

    public void deleteFrag(int id){
        fragmentList.get(id).onDetach();
        removeFromList(id);
        int[][] p = new int[5][7];
        String[][] h = {{"00:00","00:00"},{"00:00","00:00"},{"00:00","00:00"},
                {"00:00","00:00"},{"00:00","00:00"}};
        int t = total;
        int i = 0;
        for (FragmentProgram fr: fragmentList){
            p[i] = fr.getPg();
            h[i] = fr.getHr();
            i++;
        }
        String doc = "{\"pgr\":[";
        String doc1 = "{\"hr\":[";
        String doc2 = "{\"count\":";
        try {
            for(int k=0; k<5; k++){
                doc+="[";
                doc1+="[";
                for(int j=0;j<7;j++) {
                    doc += p[k][j];
                    if (j < 6) {
                        doc += ",";
                    }
                }
                for (int l=0;l<2;l++){
                    doc1+="\"";
                    doc1+=h[k][l];
                    doc1+="\"";
                    if(l<1)doc1+=",";
                }
                doc+="]";
                doc1+="]";
                if(k<4){
                    doc+=",";
                    doc1+=",";
                }
            }
            doc+="]}";
            doc1+="]}";
            doc2+=t-1+"}";

            sendRemove(doc, doc1, doc2);

            Intent intent = new Intent(getApplicationContext(), Program.class);
            finishAfterTransition();
            startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveFrag(){
        int[][] p = new int[5][7];
        String[][] h = {{"00:00","00:00"},{"00:00","00:00"},{"00:00","00:00"},
                {"00:00","00:00"},{"00:00","00:00"}};
        int t = total;
        int i = 0;
        for (FragmentProgram fr: fragmentList){
            p[i] = fr.getPg();
            h[i] = fr.getHr();
            i++;
        }
        String doc = "{\"pgr\":[";
        String doc1 = "{\"hr\":[";
        String doc2 = "{\"count\":";

        for(int k=0; k<5; k++){
            doc+="[";
            doc1+="[";
            for(int j=0;j<7;j++) {
                doc += p[k][j];
                if (j < 6) {
                    doc += ",";
                }
            }
            for (int l=0;l<2;l++){
                doc1+="\"";
                doc1+=h[k][l];
                doc1+="\"";
                if(l<1)doc1+=",";
            }
            doc+="]";
            doc1+="]";
            if(k<4){
                doc+=",";
                doc1+=",";
            }
        }
        doc+="]}";
        doc1+="]}";
        doc2+=t+"}";

        sendRemove(doc, doc1, doc2);

        Intent intent = new Intent(getApplicationContext(), Program.class);
        finishAfterTransition();
        startActivity(intent);
    }

    public void removeFromList(int i){
        fragmentList.remove(i);
    }
    public void sendRemove(String i1, String i2, String i3) {

        requestPage.sendRequestPost(url + "/programa", "prog", i1,
                "hora", i2, "count", i3);

    }
}