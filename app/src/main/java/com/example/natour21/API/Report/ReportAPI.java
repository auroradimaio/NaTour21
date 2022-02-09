package com.example.natour21.API.Report;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.natour21.API.Config;
import com.example.natour21.Item.Report;
import com.example.natour21.Volley.VolleyCallback;
import com.example.natour21.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportAPI {

    public static void getReports(FragmentActivity activity, String username, String accessToken, VolleyCallback volleyCallback) {

        String url = Config.BASE_URL + Config.API + Config.GET_REPORTS;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyCallback.onSuccess(response);
            }}, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyCallback.onError(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> param = new HashMap<>();
                param.put("username", username);
                return param;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    public static void sendResponse(Activity activity, Long reportId, String from, String response, String accessToken, VolleyCallback volleyCallback) {

        String url = Config.BASE_URL + Config.API + Config.SEND_REPORT_RESPONSE;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("report_id", reportId);
            jsonBody.put("from", from);
            jsonBody.put("response_message", response);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyCallback.onSuccess(response);
            }}, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyCallback.onError(error.getMessage());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    public static void InsertReport(Activity activity, String title, String description, int post_id, String sender, String username, String accessToken, VolleyCallback volleyCallback){

        String url = Config.BASE_URL+Config.API+Config.INSERTREPORT;
        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("title",title);
            jsonBody.put("description",description);
            jsonBody.put("post_id",post_id);
            jsonBody.put("from",sender);
            jsonBody.put("post_username",username);
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
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }


        };

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }


    public static void getReportById(int postId, ImageView imageView, RequestQueue mRequestQueue, String accessToken){
        String url = Config.BASE_URL+Config.API+Config.GETREPORTBYPOSTID+postId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    if (jsonArray.length()==0){
                        imageView.setVisibility(View.GONE);

                    }
                    for(int i=0; i<jsonArray.length();i++){

                        JSONObject res = jsonArray.getJSONObject(i);
                        String responseMessage = res.getString("responseMessage");



                        if(responseMessage.equals("null")){
                            imageView.setVisibility(View.VISIBLE);
                        }else{
                            imageView.setVisibility(View.GONE);
                        }


                    }
                }catch (JSONException je){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }

    };
        mRequestQueue.add(request);

    }}
