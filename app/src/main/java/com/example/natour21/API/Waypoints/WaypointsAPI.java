package com.example.natour21.API.Waypoints;

import android.app.Activity;
import android.app.VoiceInteractor;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.natour21.API.Config;
import com.example.natour21.Volley.VolleyCallback;
import com.example.natour21.Volley.VolleySingleton;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class WaypointsAPI {

    public static void insertWaypoints(Activity activity, double lat1, double lon1, double lat2, double lon2, VolleyCallback volleyCallback){


        String url = Config.BASE_URL+Config.API+Config.INSERTWAYPOINTS;

        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("lat1",lat1);
            jsonBody.put("lon1",lon1);
            jsonBody.put("lat2",lat2);
            jsonBody.put("lon2",lon2);

        }catch (JSONException jsonException){
            jsonException.printStackTrace();
        }

        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyCallback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyCallback.onError(error.getMessage());
            }

            }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }
}
