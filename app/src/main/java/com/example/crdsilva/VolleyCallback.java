package com.example.crdsilva;

import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject response) throws JSONException;
}
