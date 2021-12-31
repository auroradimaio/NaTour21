package com.example.natour21;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context mContext;
    private ArrayList<PostItem> mPostList;
    private RequestQueue mRequestQueue;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public PostAdapter(Context context, ArrayList<PostItem> postList){
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

        if(currentItem!=null)
            holder.bindView(currentItem);


        String descrizione = currentItem.getDescrizione();
        String minuti = currentItem.getMinuti();
        String titolo = currentItem.getTitolo();




        holder.tvMinuti.setText(minuti);
        holder.tvTitolo.setText(titolo);
        holder.tvDescrizione.setText(descrizione);









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







        public PostViewHolder(@NonNull View itemView) {
            super(itemView);


            tvDescrizione = itemView.findViewById(R.id.textView_Descrizione);
            tvTitolo = itemView.findViewById(R.id.textView_Titolo);
            tvMinuti = itemView.findViewById(R.id.textView_Minuti);
            mapView = itemView.findViewById(R.id.mapView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                           // Log.i("Click","posizione= "+position);
                        }
                    }
                }
            });


            if(mapView!=null){
                mapView.onCreate(null);
                mapView.getMapAsync(this);
                mapView.onResume();
                mapView.onPause();
            }

        }




      @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(mContext);

            map=googleMap;
            setMapLocation();



        }


        private void setMapLocation(){
            if(map==null)
                return;
            PostItem postItem = (PostItem)mapView.getTag();
            if(postItem == null)
                return;
            LatLng loc1 = new LatLng(postItem.getLat1(),postItem.getLon1());
            LatLng loc2 = new LatLng(postItem.getLat2(),postItem.getLon2());
            getRoutingPath(loc1,loc2);
            MarkerOptions markerOptions = (new MarkerOptions()).position(loc1);
            MarkerOptions markerOptions2 = (new MarkerOptions()).position(loc2);
            /*CameraPosition camPos = new CameraPosition.Builder()
                    .target(loc1)
                    .zoom(9)
                    .build();
            CameraUpdate camUp = CameraUpdateFactory.newCameraPosition(camPos);
            map.animateCamera(camUp);*/
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(loc1);
            builder.include(loc2);
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),1));
            map.setMinZoomPreference(100);
            map.setMaxZoomPreference(100);

            map.addMarker(markerOptions);
            map.addMarker(markerOptions2);
            map.getUiSettings().setAllGesturesEnabled(false);
        }

        public void bindView(PostItem postItem){
            mapView.setTag(postItem);
            setMapLocation();
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
                polyOptions.color(mContext.getResources().getColor(R.color.green));
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



    }



}
