package com.example.natour21;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.natour21.Fragments.inserimentoItinerario;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.example.natour21.Volley.VolleySingleton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostAdaptor extends RecyclerView.Adapter<PostAdaptor.PostViewHolder> {
    private Context mContext;
    private ArrayList<PostItem> mPostList;
    private RequestQueue mRequestQueue;
    MapView mMap;

    public PostAdaptor(Context context, ArrayList<PostItem> postList){
        this.mContext=context;
        this.mPostList=postList;
    }




    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template,parent,false);
        PostViewHolder postViewHolder = new PostViewHolder(v);
        mRequestQueue = Volley.newRequestQueue(mContext);
        return postViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostItem currentItem = mPostList.get(position);


        String descrizione = currentItem.getDescrizione();
        String minuti = currentItem.getMinuti();
        String titolo = currentItem.getTitolo();




        holder.tvMinuti.setText(minuti);
        holder.tvTitolo.setText(titolo);
        holder.tvDescrizione.setText(descrizione);
        holder.mapView.setTag(2);
        holder.parseJson();



    }





    @Override
    public void onViewRecycled(@NonNull PostViewHolder holder) {
        PostAdaptor.PostViewHolder mapHolder = (PostAdaptor.PostViewHolder) holder;
        if(mapHolder!=null && mapHolder.map != null){
            mapHolder.map.clear();
            mapHolder.map.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback, RoutingListener {


        public TextView tvDescrizione;
        public TextView tvTitolo;
        public TextView tvMinuti;
        public MapView mapView;
        public GoogleMap map;
        public double lat1;
        public double lat2;
        public double lon1;
        public double lon2;






        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDescrizione =(TextView)itemView.findViewById(R.id.textView_Descrizione);
            tvTitolo =(TextView)itemView.findViewById(R.id.textView_Titolo);
            tvMinuti =(TextView)itemView.findViewById(R.id.textView_Minuti);
            mapView = itemView.findViewById(R.id.mapView2);

            if(mapView!=null){
                mapView.onCreate(null);
                mapView.getMapAsync(this);
            }
        }




        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(mContext);
            map=googleMap;
           // parseJson();


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
                polyOptions.color(R.color.green);
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
                        .key(mContext.getString(R.string.google_maps_api_key))
                        .build();
                routing.execute();
            } catch (Exception e) {
                Toast.makeText(mContext, "Sentiero non valido", Toast.LENGTH_SHORT).show();
            }
        }


        public void parseJson(){
            String url = "http://192.168.1.104:8080/api/posts";
            ArrayList<LatLng> pos;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject res = jsonArray.getJSONObject(i);

                            map.clear();
                            lat1 = res.getJSONObject("way").getDouble("lat1");
                            lat2 = res.getJSONObject("way").getDouble("lat2");
                            lon1 = res.getJSONObject("way").getDouble("lon1");
                            lon2 = res.getJSONObject("way").getDouble("lon2");

                            LatLng place1= new LatLng(lat1,lon1);
                            LatLng place2= new LatLng(lat2,lon2);
                            getRoutingPath(place1,place2);
                            CameraPosition camPos = new CameraPosition.Builder()
                                    .target(place1)
                                    .target(place2)
                                    .zoom(9)
                                    .build();
                            CameraUpdate camUp = CameraUpdateFactory.newCameraPosition(camPos);
                            map.animateCamera(camUp);
                            map.addMarker(new MarkerOptions().position(place1));
                            map.addMarker(new MarkerOptions().position(place2));
                            getRoutingPath(place1,place2);

                        }
                       /* LatLng place1= new LatLng(lat1,lon1);
                        LatLng place2= new LatLng(lat2,lon2);*/

                      //  getRoutingPath(place1,place2);
                       /* response = response.getJSONObject("result");
                        lat1 = response.getDouble("lat1");
                        lat2= response.getDouble("lat2");
                        lon1=response.getDouble("lon1");
                        lon2=response.getDouble("lon2");*/

                       // LatLng place1 = new LatLng(lat1,lon1);
                       // LatLng place2= new LatLng(lat2,lon2);

                      /*  CameraPosition camPos = new CameraPosition.Builder()
                                .target(place1)
                                .target(place2)
                                .zoom(9)
                                .build();
                        CameraUpdate camUp = CameraUpdateFactory.newCameraPosition(camPos);
                        map.animateCamera(camUp);
                        map.addMarker(new MarkerOptions().position(place1));
                        map.addMarker(new MarkerOptions().position(place2));
                        getRoutingPath(place1,place2);*/





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







    }



}
