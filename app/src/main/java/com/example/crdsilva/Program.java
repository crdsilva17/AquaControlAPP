package com.example.crdsilva;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.util.LinkedList;
import java.util.Objects;

public class Program extends AppCompatActivity {
    private String url;
    private final LinkedList<FragmentProgram>fragmentList = new LinkedList<>();
    private RequestPage requestPage;
    private int total = 0;
    private final int[][] pgr = new int[5][7];
    private final String[][] hr = new String[5][2];
    private final Gerenciador gr = new Gerenciador();

    public LinkedList<FragmentProgram> getFragmentList() {
        return fragmentList;
    }

    public int getTotal() {
        return total;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        url = gr.read(getApplicationContext(),"IP");
        requestPage = new RequestPage(this);
        setContentView(R.layout.activity_program);
        Button mInicio = findViewById(R.id.txtInicio);
        Button mConf = findViewById(R.id.txtConf);
        Button mLed = findViewById(R.id.txtLed);
        Button mProg = findViewById(R.id.txtProg);
        Button btn_menu = findViewById(R.id.btnMenu);
        FloatingActionButton button = findViewById(R.id.btnAction);

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

        button.setOnClickListener(view -> {
            if (total < 5) {
                FragmentProgram frag =  new FragmentProgram();
                frag.setId(total);
                getSupportFragmentManager().beginTransaction().add(R.id.tela_hr,frag,Integer.toString(total)).commit();
                fragmentList.add(frag);
                total++;
            }
        });

        mInicio.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finishAfterTransition();
            startActivity(intent);
        });


        mLed.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Led.class);
            finishAfterTransition();
            startActivity(intent);
        });
        mProg.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Program.class);
            finishAfterTransition();
            startActivity(intent);
        });

        mConf.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Config.class);
            finishAfterTransition();
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPage.sendRequestJson(url + "/confi.htm", response -> {
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
        StringBuilder doc = new StringBuilder("{\"pgr\":[");
        StringBuilder doc1 = new StringBuilder("{\"hr\":[");
        String doc2 = "{\"count\":";
        try {
            for(int k=0; k<5; k++){
                doc.append("[");
                doc1.append("[");
                for(int j=0;j<7;j++) {
                    doc.append(p[k][j]);
                    if (j < 6) {
                        doc.append(",");
                    }
                }
                for (int l=0;l<2;l++){
                    doc1.append("\"");
                    doc1.append(h[k][l]);
                    doc1.append("\"");
                    if(l<1) doc1.append(",");
                }
                doc.append("]");
                doc1.append("]");
                if(k<4){
                    doc.append(",");
                    doc1.append(",");
                }
            }
            doc.append("]}");
            doc1.append("]}");
            doc2+=t-1+"}";

            sendRemove(doc.toString(), doc1.toString(), doc2);

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
        StringBuilder doc = new StringBuilder("{\"pgr\":[");
        StringBuilder doc1 = new StringBuilder("{\"hr\":[");
        String doc2 = "{\"count\":";

        for(int k=0; k<5; k++){
            doc.append("[");
            doc1.append("[");
            for(int j=0;j<7;j++) {
                doc.append(p[k][j]);
                if (j < 6) {
                    doc.append(",");
                }
            }
            for (int l=0;l<2;l++){
                doc1.append("\"");
                doc1.append(h[k][l]);
                doc1.append("\"");
                if(l<1) doc1.append(",");
            }
            doc.append("]");
            doc1.append("]");
            if(k<4){
                doc.append(",");
                doc1.append(",");
            }
        }
        doc.append("]}");
        doc1.append("]}");
        doc2+=t+"}";

        sendRemove(doc.toString(), doc1.toString(), doc2);

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