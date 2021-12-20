package com.example.natour21.Fragments;

import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
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
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.natour21.API.Post.PostAPI;
import com.example.natour21.Activity.homePage;


import com.example.natour21.PostAdaptor;
import com.example.natour21.PostItem;
import com.example.natour21.R;
import com.example.natour21.Volley.VolleySingleton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.natour21.API.Waypoints.WaypointsAPI;
import com.example.natour21.Constants;
import com.example.natour21.R;
import com.example.natour21.Volley.VolleyCallback;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;

public class HomeFragment extends Fragment  {

    private RecyclerView mRecyclerView;
    private PostAdaptor mPostAdaptor;
    private ArrayList<PostItem> mPostList;
    private RequestQueue mRequestQueue;

    double lat1,lat2,lon1,lon2;
    GoogleMap map;
    MarkerOptions place1;
    MarkerOptions place2;

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
        PostAPI.getPosts(getActivity(),mPostList,mPostAdaptor,mRecyclerView,mRequestQueue);
       // parseJson2();


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
        super.onResume();
    }

    /*private void parseJSON(){
        String url = "http://192.168.1.104:8080/api/posts";
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
                        //Toast.makeText(getActivity(),title,Toast.LENGTH_LONG).show();

                        mPostList.add(new PostItem(description,minutes,title));

                    }



                    mPostAdaptor = new PostAdaptor(getContext(),mPostList);
                    mRecyclerView.setAdapter(mPostAdaptor);



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

    }*/

    /*public void parseJson2(){
        String url = "http://192.168.1.104:8080/api/waypoints/id?id=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject res = jsonArray.getJSONObject(i);

                        lat1 = res.getDouble("lat1");
                        lat2 = res.getDouble("lat2");
                        lon1 = res.getDouble("lon1");
                        lon2 = res.getDouble("lon2");

                    }
                    place1 = new MarkerOptions().position(new LatLng(lat1, lon1)).title("Inizio");
                    place2 = new MarkerOptions().position(new LatLng(lat2, lon2)).title("Destinazione");

                    getRoutingPath(place1.getPosition(),place2.getPosition());





                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );

        mRequestQueue.add(request);
    }


    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {


        Log.e("check", "onRoutingSuccess");

        List<Polyline> polylines = new ArrayList<>();


        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();

        for (int i = 0; i < route.size(); i++) {




            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(R.color.black);
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = map.addPolyline(polyOptions);
            polylines.add(polyline);




        }


    }

    @Override
    public void onRoutingCancelled() {

    }


    private void getRoutingPath(LatLng lt1, LatLng lt2) {


        try {

            Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.WALKING)
                    .withListener(this)
                    .waypoints(lt1, lt2)
                    .key(getString(R.string.google_maps_api_key))
                    .build();
            routing.execute();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Sentiero non valido", Toast.LENGTH_SHORT).show();
        }
    }*/
}

