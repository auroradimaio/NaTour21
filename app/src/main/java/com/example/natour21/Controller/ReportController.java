package com.example.natour21.Controller;

import android.app.Activity;
import android.widget.Toast;

import com.example.natour21.API.Report.ReportAPI;
import com.example.natour21.Volley.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportController {

    public static void InsertReport(Activity activity, String title, String description, String sender, int time, int id_post){

        ReportAPI.InsertReport(activity, title, description, id_post, sender, time, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
               try{
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equals("OK")) {
                    Toast.makeText(activity,"OK",Toast.LENGTH_SHORT).show();
                }else if(jsonObject.getString("status").equals("FAILED")){
                    Toast.makeText(activity,"FAILED",Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException jsonException){
                Toast.makeText(activity,jsonException.toString(),Toast.LENGTH_SHORT).show();
            }
                }

            @Override
            public void onError(String response) {

            }
        });

    }
}
