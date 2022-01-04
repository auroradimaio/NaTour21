package com.example.natour21.Controller;

import android.app.Activity;
import android.widget.Toast;

import com.example.natour21.API.Post.PostAPI;
import com.example.natour21.Volley.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class PostController {


    public static void UpdatePost(Activity activity, String difficulty, String minutes, int id){
        PostAPI.ModifyPost(activity, difficulty, minutes, id, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
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



    public static void InsertPost(Activity activity, String title, String description, String time, String difficulty, String startpoint){

        PostAPI.InsertPost(activity, title, description, time, difficulty, startpoint, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
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
