package com.example.natour21.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.natour21.Activity.homePage;


import com.example.natour21.Adapter.PostAdapter;
import com.example.natour21.Item.PostItem;
import com.example.natour21.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment implements PostAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private PostAdapter mPostAdapter;
    private ArrayList<PostItem> mPostList;
    private RequestQueue mRequestQueue;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();

        super.onCreate(savedInstanceState);




    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPostList = new ArrayList<>();




        mRequestQueue = Volley.newRequestQueue(getActivity());
        parseJSON();



        Button button = (Button)view.findViewById(R.id.btnInsertPath);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_inserimentoItinerario);



            }
        });





        return view;
    }



    @Override
    public void onResume() {
        ((homePage)getActivity()).setActionBarTitle("NaTour21");
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        navView.setVisibility(navView.VISIBLE);

        super.onResume();
    }

    private void parseJSON(){
        String url = "http://192.168.1.14:8080/api/posts";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    double lat1=0,lat2=0,lon1=0,lon2=0;
                    JSONArray jsonArray = response.getJSONArray("result");

                    for(int i = 0; i<jsonArray.length();i++){
                        JSONObject res = jsonArray.getJSONObject(i);
                        try {
                            JSONObject way = res.getJSONObject("way");

                            lat1 = way.getDouble("lat1");
                            lat2 = way.getDouble("lat2");
                            lon1 = way.getDouble("lon1");
                            lon2 = way.getDouble("lon2");
                        }catch (JSONException je){

                        }


                        String title = res.getString("title");
                        String description = res.getString("description");
                        String minutes = (String) res.get("minutes");
                        String min = res.getString("minutes");
                        String difficulty = res.getString("difficulty");
                        String startpoint = res.getString("startpoint");


                        int id = res.getInt("id");


                        mPostList.add(new PostItem(description, minutes, title, lat1, lat2, lon1, lon2,id,difficulty,min,startpoint));
                    }




                    mPostAdapter = new PostAdapter(getContext(),mPostList);
                    mRecyclerView.setAdapter(mPostAdapter);
                    mPostAdapter.setOnItemClickListener(HomeFragment.this::onItemClick);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }
        );

        mRequestQueue.add(request);

    }





    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(),"pos="+position,Toast.LENGTH_LONG);
        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();
        PostItem clickedItem = mPostList.get(position);
        bundle.putString("Titolo",clickedItem.getTitolo());
        bundle.putString("Descrizione",clickedItem.getDescrizione());
        bundle.putString("Durata",clickedItem.getDurata());
        bundle.putString("Difficoltà",clickedItem.getDifficoltà());
        bundle.putString("PuntoInizio",clickedItem.getStartpoint());
        bundle.putDouble("Lat1",clickedItem.getLat1());
        bundle.putDouble("Lat2",clickedItem.getLat2());
        bundle.putDouble("Lon1",clickedItem.getLon1());
        bundle.putDouble("Lon2",clickedItem.getLon2());
        bundle.putInt("Id",clickedItem.getId());

        fragment.setArguments(bundle);




        Log.i("Click","posizioine= "+position+ "descrizione="+clickedItem.getDescrizione() + "lat1"+clickedItem.getLat1()+"id="+clickedItem.getId()+"durata, diff e punto inizio"+clickedItem.getDurata()+clickedItem.getDifficoltà()+clickedItem.getStartpoint());
        Navigation.findNavController(mRecyclerView).navigate(R.id.action_navigation_home_to_postDetailsFragment,bundle);

    }
}

