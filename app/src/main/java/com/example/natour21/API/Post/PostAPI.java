package com.example.natour21.API.Post;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.natour21.API.Config;
import com.example.natour21.PostAdaptor;
import com.example.natour21.PostItem;
import com.example.natour21.Volley.VolleyCallback;
import com.example.natour21.Volley.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostAPI {


    public static void getPosts(Activity activity, ArrayList<PostItem> mPostList, PostAdaptor mPostAdaptor, RecyclerView mRecyclerView, RequestQueue mRequestQueue){
        String url = Config.BASE_URL+Config.API+Config.POST;
        final PostAdaptor[] mp = {mPostAdaptor};
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("result");

                    for(int i = 0; i<jsonArray.length();i++){
                        JSONObject res = jsonArray.getJSONObject(i);

                        String title = res.getString("title");
                        String description = res.getString("description");
                        String minutes = (String) res.get("minutes");
                        Double lat1 = res.getJSONObject("way").getDouble("lat1");
                        Double lat2 = res.getJSONObject("way").getDouble("lat2");
                        Double lon1 = res.getJSONObject("way").getDouble("lon1");
                        Double lon2 = res.getJSONObject("way").getDouble("lon2");
                        Toast.makeText(activity,lat1.toString(),Toast.LENGTH_LONG).show();


                        mPostList.add(new PostItem(description,minutes,title,lat1,lat2,lon1,lon2));

                    }



                    mp[0] = new PostAdaptor(activity,mPostList);
                    mRecyclerView.setAdapter(mp[0]);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity,e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(activity,error.toString(),Toast.LENGTH_LONG).show();
            }
        }
        );

        mRequestQueue.add(request);

    }



}
