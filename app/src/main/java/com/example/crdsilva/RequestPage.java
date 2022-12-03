package com.example.crdsilva;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestPage {
    private final Context context;
    private String url = null;
    private final RequestQueue queue;

    public RequestPage(Context context){
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
    }

    public void sendRequestJson(String urlRequest, final VolleyCallback callback){
        this.url = urlRequest;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, this.url, null,
                response -> {
                    try {
                        callback.onSuccess(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Toast.makeText(context.getApplicationContext(), "Falha na Conexão!!!",
                            Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                });
        this.queue.add(request);
    }
    public void sendRequestPost(String urlRequest, String pNome, String pValor){
        this.url = urlRequest;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.url,
                response -> {}, error -> Toast.makeText(context.getApplicationContext(), "Falha na Conexão!!!",
                    Toast.LENGTH_LONG).show()){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put(pNome, pValor);
                return parametros;
            }
        };
        this.queue.add(stringRequest);
    }
    public void sendRequestPost(String urlRequest, String p1, String v1, String p2, String v2,
                                String p3, String v3){
        this.url = urlRequest;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.url,
                response -> {}, error -> Toast.makeText(context.getApplicationContext(), "Falha na Conexão!!!",
                        Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new LinkedHashMap<>();
                parametros.put(p1, v1);
                parametros.put(p2, v2);
                parametros.put(p3, v3);
                return parametros;
            }
        };
        this.queue.add(stringRequest);
    }
}
