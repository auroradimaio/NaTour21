package com.example.natour21.API.Post;

import android.app.Activity;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.natour21.API.Config;
import com.example.natour21.Adapter.PostAdapter;
import com.example.natour21.Item.PostItem;
import com.example.natour21.Volley.VolleyCallback;
import com.example.natour21.Volley.VolleySingleton;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class PostAPI {


    public static void getPosts(Activity activity, ArrayList<PostItem> mPostList, PostAdapter mPostAdapter, RecyclerView mRecyclerView, RequestQueue mRequestQueue){
        String url = Config.BASE_URL+Config.API+Config.POST;
        final PostAdapter[] mp = {mPostAdapter};
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
                        String min = res.getString("minutes");
                        String difficulty = res.getString("difficulty");
                        String startpoint = res.getString("startpoint");

                        int id = res.getInt("id");
                        Double lat1 = res.getJSONObject("way").getDouble("lat1");
                        Double lat2 = res.getJSONObject("way").getDouble("lat2");
                        Double lon1 = res.getJSONObject("way").getDouble("lon1");
                        Double lon2 = res.getJSONObject("way").getDouble("lon2");
                        LatLng ll = new LatLng(lat1,lon1);
                        LatLng ll2 = new LatLng(lat2,lon2);
                        Toast.makeText(activity,lat1.toString(),Toast.LENGTH_LONG).show();


                        mPostList.add(new PostItem(description,minutes,title,lat1,lat2,lon1,lon2,id,difficulty,min,startpoint));

                    }



                    mp[0] = new PostAdapter(activity,mPostList);
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



    public static void ModifyPost(Activity activity,String difficulty, String minutes, int id,VolleyCallback volleyCallback){
        String url =Config.BASE_URL+Config.API+"/post/update?id="+id+"&difficulty="+difficulty+"&minutes="+minutes;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyCallback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyCallback.onError(error.getMessage());
            }
        });
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }



    public static void InsertPost(Activity activity, String title, String description, String time, String difficulty, String startpoint, VolleyCallback volleyCallback){

        String url = Config.BASE_URL+Config.API+Config.INSERTPOST;
        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("description",description);
            jsonBody.put("title",title);
            jsonBody.put("minutes",time);
            jsonBody.put("difficulty",difficulty);
            jsonBody.put("startpoint",startpoint);

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
