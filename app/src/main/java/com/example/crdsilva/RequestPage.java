package com.example.crdsilva;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class RequestPage {
    private Context context = null;
    private String url = null;
    private RequestQueue queue;

    public RequestPage(Context context){
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
    }

    public void sendRequestJson(String urlRequest){
        this.url = urlRequest;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, this.url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        this.queue.add(request);
    }

    public void sendRequestJson(String urlRequest, final VolleyCallback callback){
        this.url = urlRequest;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, this.url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(), "Falha na Conexão!!!",
                        Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        this.queue.add(request);
    }
    public void sendRequestPost(String urlRequest, String pNome, String pValor){
        this.url = urlRequest;
        String nome = pNome;
        String valor = pValor;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.url,
                response -> {}, error -> {
            Toast.makeText(context.getApplicationContext(), "Falha na Conexão!!!",
                Toast.LENGTH_LONG).show();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put(nome, valor);
                return parametros;
            }
        };
        this.queue.add(stringRequest);
    }
    public void sendRequestPost(String urlRequest, String p1, String v1, String p2, String v2,
                                String p3, String v3){
        this.url = urlRequest;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.url,
                response -> {}, error -> {
            Toast.makeText(context.getApplicationContext(), "Falha na Conexão!!!",
                    Toast.LENGTH_LONG).show();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
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
