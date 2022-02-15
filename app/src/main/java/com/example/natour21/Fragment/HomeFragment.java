package com.example.natour21.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.*;
import com.android.volley.toolbox.Volley;
import com.example.natour21.Activity.homePage;


import com.example.natour21.Adapter.PostAdapter;
import com.example.natour21.Controller.AuthenticationController;
import com.example.natour21.Controller.ChatController;
import com.example.natour21.Controller.PostController;
import com.example.natour21.Controller.ReportController;
import com.example.natour21.Item.PostItem;
import com.example.natour21.Pusher.PusherManager;
import com.example.natour21.R;

import java.util.ArrayList;

import com.example.natour21.Utils.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment implements PostAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    public static RecyclerView mRecyclerView;
    public static PostAdapter mPostAdapter;
    public static ArrayList<PostItem> mPostList;
    public static RequestQueue mRequestQueue;
    ImageView userImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        AuthenticationController.isOnHomePage = true;
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        AuthenticationController.isOnHomePage = true;
        ChatController.chattingWith = "";
        ChatController.onChatList = false;
        ChatController.onSingleChat = false;
        ReportController.onReportList = false;
        PusherManager.activity = getActivity();


        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.green));


        mPostList = new ArrayList<>();

        userImage = view.findViewById(R.id.imgViewUser);
        userImage.setImageResource(ImagePicker.getImage(AuthenticationController.user_username));


        mRequestQueue = Volley.newRequestQueue(getActivity());


        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);

                PostController.getPosts(getActivity(),HomeFragment.this,mPostList,mPostAdapter,mRecyclerView,mRequestQueue);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

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
        ((homePage)getActivity()).getSupportActionBar().setTitle("NaTour21");
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        navView.setVisibility(navView.VISIBLE);

        AuthenticationController.isOnHomePage = true;
        ChatController.chattingWith = "";
        ChatController.onChatList = false;
        ChatController.onSingleChat = false;
        ReportController.onReportList = false;
        super.onResume();
    }




    @Override
    public void onItemClick(int position) {

        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();
        PostItem clickedItem = mPostList.get(position);
        bundle.putString("Titolo",clickedItem.getTitolo());
        bundle.putString("Descrizione",clickedItem.getDescrizione());
        bundle.putString("PuntoInizio",clickedItem.getStartpoint());
        bundle.putDouble("Lat1",clickedItem.getLat1());
        bundle.putDouble("Lat2",clickedItem.getLat2());
        bundle.putDouble("Lon1",clickedItem.getLon1());
        bundle.putDouble("Lon2",clickedItem.getLon2());
        bundle.putInt("Id",clickedItem.getId());
        bundle.putString("User",clickedItem.getUsername());

        fragment.setArguments(bundle);

        Navigation.findNavController(mRecyclerView).navigate(R.id.action_navigation_home_to_postDetailsFragment,bundle);

    }


    @Override
    public void onRefresh() {
        PostController.getPosts(getActivity(),HomeFragment.this,mPostList,mPostAdapter,mRecyclerView,mRequestQueue);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}

