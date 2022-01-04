package com.example.natour21.API.Review;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.natour21.API.Config;
import com.example.natour21.Adapter.ReviewAdapter;
import com.example.natour21.Item.ReviewItem;
import com.example.natour21.R;
import com.example.natour21.Volley.VolleyCallback;
import com.example.natour21.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class ReviewAPI {

    public static void getReviewsById(Activity activity, ArrayList<ReviewItem> mReviewList, ReviewAdapter mReviewAdapter, RecyclerView mRecyclerView, RequestQueue requestQueue, int id, RatingBar ratingBar, TextView textView, MotionLayout motionLayout){
        String url = Config.BASE_URL+Config.API+Config.GETREVIEWBYID+id;
        final ReviewAdapter[] mp = {mReviewAdapter};
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    float totalvalue=0;
                    int i;
                    JSONArray jsonArray = response.getJSONArray("result");

                    if(jsonArray.length()==0){
                       motionLayout.getTransition(R.id.Transazione).setEnable(false);
                    }

                    for(i=0;i<jsonArray.length();i++) {

                        JSONObject res = jsonArray.getJSONObject(i);

                        String description = res.getString("description");
                        float value = (float) res.getDouble("value");


                        //Log.i("VALORE","valore recensione"+value);

                        Log.i("VALORE","valore recensione"+value);
                        mReviewList.add(new ReviewItem(value, description));
                        totalvalue=totalvalue+value;

                    }
                    Log.i("VALORE","valore recensione totale"+totalvalue/i);
                    ratingBar.setRating(totalvalue/i);
                    textView.setText(String.valueOf(totalvalue/i));


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


    public static void insertReview(Activity activity, String description, double value, int id_post, VolleyCallback volleyCallback){

        String url = Config.BASE_URL+Config.API+Config.INSERTREVIEW;
        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("description",description);
            jsonBody.put("value",value);
            jsonBody.put("id_post",id_post);
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




