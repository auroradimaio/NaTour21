package com.example.natour21.API.Report;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.natour21.API.Config;
import com.example.natour21.Volley.VolleyCallback;
import com.example.natour21.Volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ReportAPI {


    public static void InsertReport(Activity activity, String title, String description, int post_id, String sender, int time, VolleyCallback volleyCallback){

        String url = Config.BASE_URL+Config.API+Config.INSERTREPORT;
        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("title",title);
            jsonBody.put("description",description);
            jsonBody.put("postId",post_id);
            jsonBody.put("sender",sender);
            jsonBody.put("time",time);
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
        }
        ){
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
