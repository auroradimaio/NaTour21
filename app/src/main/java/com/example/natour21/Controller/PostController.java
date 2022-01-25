package com.example.natour21.Controller;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.natour21.API.Post.PostAPI;
import com.example.natour21.Adapter.PostAdapter;
import com.example.natour21.Fragment.HomeFragment;
import com.example.natour21.Item.ChatRoom;
import com.example.natour21.Item.PostItem;
import com.example.natour21.R;
import com.example.natour21.Volley.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.example.natour21.Dialog.Dialog.showMessageDialog;

public class PostController {



    public static void getPosts(Activity activity, HomeFragment homeFragment, ArrayList<PostItem> mPostList, PostAdapter mPostAdapter, RecyclerView mRecyclerView, RequestQueue mRequestQueue){

        PostAPI.getPosts(activity,homeFragment,mPostList,mPostAdapter,mRecyclerView,mRequestQueue,AuthenticationController.accessToken);
    }



    public static void InsertPost(Activity activity, String title, String description, String startpoint,
                                    double lat1, double lng1, double lat2, double lng2,
                                  String duration, Integer minutes, Integer difficulty){

        PostAPI.InsertPost(activity, title, description, startpoint,
                lat1,lng1,lat2,lng2,duration,minutes,difficulty, AuthenticationController.user_username, AuthenticationController.accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK"))
                    {
                        showMessageDialog(activity, "Post inserito con successo", null);
                    }else if(jsonObject.getString("status").equals("FAILED")){
                        showMessageDialog(activity,"Non è stato possibile inserire il post",null);
                    }else if(jsonObject.getString("status").equals("TOKEN_EXPIRED"))
                    {
                        AuthenticationController.logout(activity, true);
                    }
                }catch (JSONException jsonException){
                    showMessageDialog(activity,"Non è stato possibile inserire il post",null);

                }
            }

            @Override
            public void onError(String response) {
                showMessageDialog(activity,"Non è stato possibile inserire il post",null);
            }
        });

    }











}
