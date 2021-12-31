package com.example.natour21.API.Review;

import android.app.Activity;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.natour21.API.Config;
import com.example.natour21.PostAdapter;
import com.example.natour21.ReviewAdapter;
import com.example.natour21.ReviewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewAPI {

    public static void getReviewsById(Activity activity, ArrayList<ReviewItem> mReviewList, ReviewAdapter mReviewAdapter, RecyclerView mRecyclerView, RequestQueue requestQueue, int id){
        String url = Config.BASE_URL+Config.API+Config.GETREVIEWBYID+id;
        final ReviewAdapter[] mp = {mReviewAdapter};
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                  //  JSONArray jsonArray = response.getJSONArray("result");
                    JSONObject res = response.getJSONObject("result");

                  //  for (int i = 0; i < res.length(); i++) {
                       // JSONObject res = jsonArray.getJSONObject(i);

                        String description = res.getString("description");
                        float value = (float) res.getDouble("value");


                        mReviewList.add(new ReviewItem(value, description));
                  //  }

                    mp[0] = new ReviewAdapter(activity, mReviewList);
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

        requestQueue.add(request);

    }



}
