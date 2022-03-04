package com.example.natour21.Controller;

import android.app.Activity;
import android.view.View;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.natour21.API.Post.PostAPI;
import com.example.natour21.Adapter.PostAdapter;
import com.example.natour21.FirebaseLog;
import com.example.natour21.Fragment.HomeFragment;
import com.example.natour21.Item.PostItem;
import com.example.natour21.R;
import com.example.natour21.Volley.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.natour21.Dialog.Dialog.showMessageDialog;

public class PostController {



    public static void getPosts(Activity activity, HomeFragment homeFragment, ArrayList<PostItem> mPostList, PostAdapter mPostAdapter, RecyclerView mRecyclerView, RequestQueue mRequestQueue){

        PostAPI.getPosts(activity,homeFragment,mPostList,mPostAdapter,mRecyclerView,mRequestQueue,AuthenticationController.accessToken);
    }



    public static void InsertPost(Activity activity, String title, String description, String startpoint,
                                    double lat1, double lng1, double lat2, double lng2,
                                  String duration, Integer minutes, Integer difficulty, View view){

        PostAPI.InsertPost(activity, title, description, startpoint,
                lat1,lng1,lat2,lng2,duration,minutes,difficulty, AuthenticationController.user_username, AuthenticationController.accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK"))
                    {
                        FirebaseLog.getInstance().stopTrace("insertPost");
                        showMessageDialog(activity, "Post inserito con successo", null);
                        Navigation.findNavController(view).navigate(R.id.action_inserimentoItinerario_to_navigation_home);
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
