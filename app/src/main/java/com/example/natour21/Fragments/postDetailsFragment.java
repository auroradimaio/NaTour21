package com.example.natour21.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.natour21.API.Review.ReviewAPI;
import com.example.natour21.Constants;
import com.example.natour21.R;
import com.example.natour21.ReviewAdapter;
import com.example.natour21.ReviewItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class postDetailsFragment extends Fragment implements OnMapReadyCallback, RoutingListener {

    TextView descrizione_textView;
    TextView titolo_textView;
    private MapView mMapView;
    GoogleMap map;
    LatLng l1,l2;
    double lat1,lat2,lon1,lon2;
    int id;
    private RecyclerView mRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private ArrayList<ReviewItem> mReviewList;
    private RequestQueue mRequestQueue;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Dettagli sentiero");
        actionBar.setDisplayHomeAsUpEnabled(false);
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        navView.setVisibility(navView.GONE);
        actionBar.show();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post_details, container, false);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.dettagli_RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mReviewList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(getActivity());






        Bundle bundle = this.getArguments();
        String descrizione = bundle.getString("Descrizione");
        String titolo = bundle.getString("Titolo");
         lat1 = bundle.getDouble("Lat1");
         lon1 = bundle.getDouble("Lon1");
         lon2 = bundle.getDouble("Lon2");
         lat2 = bundle.getDouble("Lat2");
         id = bundle.getInt("Id");

       /* l1= new LatLng(lat1,lon1);
        l2= new LatLng(lat2,lon2);

        Log.i("valori latlon=","lat1="+lat1+"lon1"+lon1+"lon2"+lon2+"lat2"+lat2);*/


        descrizione_textView = (TextView)v.findViewById(R.id.descrizione_textView);
        descrizione_textView.setText(descrizione);
        titolo_textView = (TextView)v.findViewById(R.id.nomeSentiero_textView);
        titolo_textView.setText(titolo);

        ReviewAPI.getReviewsById(getActivity(),mReviewList,mReviewAdapter,mRecyclerView,mRequestQueue,id);









        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(Constants.MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) v.findViewById(R.id.dettaglio_mapView);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);




        return v;
    }


    @Override
    public void onMapReady(GoogleMap gmap) {
        map=gmap;


        l1= new LatLng(lat1,lon1);
        l2= new LatLng(lat2,lon2);

        Log.i("valori latlon=","lat1="+lat1+"lon1"+lon1+"lon2"+lon2+"lat2"+lat2);
        getRoutingPath(l1,l2);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(l1);
        builder.include(l2);
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),48));
        map.addMarker(new MarkerOptions().position(l1));
        map.addMarker(new MarkerOptions().position(l2));


    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(Constants.MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(Constants.MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

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
            polyOptions.color(getActivity().getResources().getColor(R.color.green));
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
    }
}