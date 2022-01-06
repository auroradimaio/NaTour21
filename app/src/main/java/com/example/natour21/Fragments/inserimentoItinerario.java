package com.example.natour21.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.natour21.Constants;
import com.example.natour21.Controller.PostController;
import com.example.natour21.Controller.WaypointsController;
import com.example.natour21.Dialog;
import com.example.natour21.R;
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import br.com.onimur.handlepathoz.HandlePathOz;
import br.com.onimur.handlepathoz.HandlePathOzListener;
import br.com.onimur.handlepathoz.model.PathOz;
import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Track;
import io.ticofab.androidgpxparser.parser.domain.TrackPoint;
import io.ticofab.androidgpxparser.parser.domain.TrackSegment;


public class inserimentoItinerario extends Fragment implements OnMapReadyCallback, RoutingListener, HandlePathOzListener.SingleUri {


    private MapView mMapView;
    int i = 0;
    double lat1=0;
    double lat2=0;
    double lng1=0;
    double lng2=0;
    String trigger = "no";

    MarkerOptions place1;
    MarkerOptions place2;
    GoogleMap map;
    GPXParser parser = new GPXParser();

    Gpx parsedGpx;
    private HandlePathOz handlePathOz;
    static final String TAG = inserimentoItinerario.class.getSimpleName();

    Button btnIns;
    Button btnPubblica;
    EditText title,description,startPoint,time;
    Spinner time_spinner;







    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        navView.setVisibility(navView.GONE);
        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();







    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inserimento_itinerario, container, false);



        handlePathOz = new HandlePathOz(getActivity(),this);

        ActivityResultLauncher<Intent> activityFilePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode()==Activity.RESULT_OK){
                            Intent data = result.getData();

                            Uri uri = data.getData();


                            handlePathOz.getRealPath(uri);

                        }
                        else
                        {
                            Toast.makeText(getActivity(),"no",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        ImageButton imgBtnBack = (ImageButton) view.findViewById(R.id.imgBtn_back);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_inserimentoItinerario_to_navigation_home);
            }
        });



        time = view.findViewById(R.id.time_editText);
        title= view.findViewById(R.id.title_editText);
        description = view.findViewById(R.id.description_editText);
        startPoint = view.findViewById(R.id.startPoint_editText);

        time_spinner = (Spinner) view.findViewById(R.id.difficulty_spinner);
        String[] items = new String[]{"Facile", "Media", "Difficile"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        time_spinner.setAdapter(adapter);




        btnIns = (Button) view.findViewById(R.id.btnInsPath);
        btnIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activityFilePicker.launch(intent);



            }
        });


        btnPubblica = (Button)view.findViewById(R.id.btnPubblica);
        btnPubblica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((title.getText().toString().isEmpty() || description.getText().toString().isEmpty() || time.getText().toString().isEmpty() || time_spinner.getSelectedItem().toString()
                ==null || startPoint.getText().toString().isEmpty()) || (lat1 == 0 || lat2 == 0 || lng1 == 0 || lng2 == 0)){
                    Toast.makeText(getActivity(),"Inserire tutti i campi/Sentiero non valido",Toast.LENGTH_SHORT).show();
                }else{

                    PostController.InsertPost(getActivity(), title.getText().toString(), description.getText().toString(), time.getText().toString()
                            , time_spinner.getSelectedItem().toString(), startPoint.getText().toString());

                    Dialog dialog = new Dialog();

                    dialog.showMessageDialog(getActivity(), "Sentiero inserito corretamente", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            WaypointsController.insertWaypoints(getActivity(),lat1,lng1,lat2,lng2);

                        }
                    });

                    Toast.makeText(getActivity(), " valido", Toast.LENGTH_SHORT).show();
                }

                }


        });




        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(Constants.MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);


        return view;
    }


    @Override
    public void onPause() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        mMapView.onPause();
        super.onPause();
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
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
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
    public void onMapReady(GoogleMap gmap) {

        map = gmap;
        CameraPosition camPos = new CameraPosition.Builder()
                .target(new LatLng(40.853294,14.305573)) //Focus iniziale su Napoli
                .zoom(8)
                .build();
        CameraUpdate camUp = CameraUpdateFactory.newCameraPosition(camPos);
        map.animateCamera(camUp);



        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onMapClick(LatLng latLng) {

                if(trigger.equals("si")){
                    map.getUiSettings().setAllGesturesEnabled(false);
                }
                else {

                    i++;
                    if (i == 1) {
                        lat1 = latLng.latitude;
                        lng1 = latLng.longitude;
                        place1 = new MarkerOptions().position(new LatLng(lat1, lng1)).title("Inizio");
                        map.addMarker(place1);
                    }
                    if (i == 2) {
                        lat2 = latLng.latitude;
                        lng2 = latLng.longitude;
                        place2 = new MarkerOptions().position(new LatLng(lat2, lng2)).title("Destinazione");
                        map.addMarker(place2);

                        getRoutingPath(place1.getPosition(), place2.getPosition());
                        btnIns.setEnabled(false);
                        btnIns.setAlpha(.5f);



                    }
                    if (i > 2) {
                        Toast.makeText(getActivity(), "Puoi selezionare al massimo 2 punti", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });








        mMapView.onResume();
    }


    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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
    }



    @Override
    public void onRequestHandlePathOz(@NotNull PathOz pathOz, @Nullable Throwable throwable) {

        Double maxLat=null,minLat=null,minLon=null,maxLon=null;
        String extension = pathOz.getPath().substring(pathOz.getPath().lastIndexOf("."));
        if(extension.equals(".gpx")){




        Toast.makeText(getActivity(),pathOz.getPath(),Toast.LENGTH_LONG).show();

        ArrayList<LatLng> points = new ArrayList<>();

        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(R.color.black);
        polyOptions.width(10 + i * 3);


        try {

            InputStream in = new FileInputStream(pathOz.getPath());
            parsedGpx = parser.parse(in);
        }
        catch (IOException  | XmlPullParserException e ){
            e.printStackTrace();
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }

        if(parsedGpx==null){
            Toast.makeText(getActivity(),"parsedgpxnullo",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "parsedgpxfunziona", Toast.LENGTH_SHORT).show();
            List<Track> tracks = parsedGpx.getTracks();
            for (int i = 0; i < tracks.size(); i++) {
                Track track = tracks.get(i);
                Log.d(TAG, "track " + i + ":");
                List<TrackSegment> segments = track.getTrackSegments();
                for (int j = 0; j < segments.size(); j++) {
                    TrackSegment segment = segments.get(j);
                    Log.d(TAG, "  segment " + j + ":");
                    for (TrackPoint trackPoint : segment.getTrackPoints()) {
                        String msg = "    point: lat " + trackPoint.getLatitude() + ", lon " + trackPoint.getLongitude() + ", time " + trackPoint.getTime();
                        LatLng l1 = new LatLng(trackPoint.getLatitude(), trackPoint.getLongitude());

                        points.add(l1);

                        maxLat = maxLat != null ? Math.max(trackPoint.getLatitude(),maxLat) : trackPoint.getLatitude();
                        minLat = minLat != null ? Math.min(trackPoint.getLatitude(),minLat) : trackPoint.getLatitude();

                        maxLon = maxLon != null ? Math.max(trackPoint.getLongitude(),maxLon) : trackPoint.getLongitude();
                        minLon = minLon != null ? Math.min(trackPoint.getLongitude(),minLon) : trackPoint.getLongitude();




                        Log.d(TAG, msg);
                    }
                    polyOptions.addAll(points);
                    map.addPolyline(polyOptions);
                    lat1=minLat;
                    lat2=maxLat;
                    lng1=minLon;
                    lng2=maxLon;


                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(new LatLng(maxLat,maxLon));
                    builder.include(new LatLng(minLat,minLon));
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),48));
                    trigger = "si";



                }


            }
        }


    }

        else
    {
        Toast.makeText(getActivity(),"Seleziona un file .gpx",Toast.LENGTH_LONG).show();
    }



}







}
