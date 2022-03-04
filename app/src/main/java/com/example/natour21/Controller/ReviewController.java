package com.example.natour21.Controller;

import android.app.Activity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.natour21.API.Review.ReviewAPI;
import com.example.natour21.Adapter.ReviewAdapter;
import com.example.natour21.FirebaseLog;
import com.example.natour21.Item.ReviewItem;
import com.example.natour21.Volley.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import static com.example.natour21.Dialog.Dialog.showMessageDialog;

public class ReviewController {



    public static void getReviewsById(Activity activity, ArrayList<ReviewItem> mReviewList, ReviewAdapter mReviewAdapter, RecyclerView mRecyclerView, RequestQueue requestQueue, int id, RatingBar ratingBar, TextView textView, MotionLayout motionLayout){

        ReviewAPI.getReviewsById(activity,mReviewList,mReviewAdapter,mRecyclerView,requestQueue,id,ratingBar,textView,motionLayout,AuthenticationController.accessToken);

    }

    public static void insertReview(Activity activity, String description, double value, int id, View view){

        ReviewAPI.insertReview(activity, description, value, id,AuthenticationController.user_username,AuthenticationController.accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        FirebaseLog.getInstance().stopTrace("insertReview");
                        showMessageDialog(activity, "Recensione inserita con successo", null);
                        Navigation.findNavController(view).popBackStack();
                    }else if(jsonObject.getString("status").equals("FAILED")){
                        showMessageDialog(activity,"Hai già inserito una recensione per questo post",null);
                    }else if(jsonObject.getString("status").equals("TOKEN_EXPIRED"))
                    {
                        AuthenticationController.logout(activity, true);
                    }
                }catch (JSONException jsonException){
                    showMessageDialog(activity,"Non è stato possibile inserire la recensione",null);
                }
            }

            @Override
            public void onError(String response) {
                showMessageDialog(activity,"Non è stato possibile inserire la recensione",null);
            }
        });

    }

}
