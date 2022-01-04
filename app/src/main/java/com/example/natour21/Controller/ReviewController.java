package com.example.natour21.Controller;

import android.app.Activity;
import android.widget.Toast;

import com.example.natour21.API.Review.ReviewAPI;
import com.example.natour21.Volley.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewController {

    public static void insertReview(Activity activity, String description, double value, int id){

        ReviewAPI.insertReview(activity, description, value, id, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        Toast.makeText(activity,"OK",Toast.LENGTH_LONG).show();
                    }else if(jsonObject.getString("status").equals("FAILED")){
                        Toast.makeText(activity,"FAILED",Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException jsonException){
                    Toast.makeText(activity,"JSONEXC"+jsonException.toString(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String response) {

            }
        });

    }

}
