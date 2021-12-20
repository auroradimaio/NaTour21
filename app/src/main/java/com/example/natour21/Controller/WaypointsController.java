package com.example.natour21.Controller;

import android.app.Activity;
import android.widget.Toast;

import com.example.natour21.API.Waypoints.WaypointsAPI;
import com.example.natour21.Volley.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class WaypointsController {

    public static void insertWaypoints(Activity activity, double lat1, double lon1, double lat2, double lon2){

        WaypointsAPI.insertWaypoints(activity, lat1, lon1, lat2, lon2, new VolleyCallback() {
            public void onSuccess(String response) {
                try {
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
