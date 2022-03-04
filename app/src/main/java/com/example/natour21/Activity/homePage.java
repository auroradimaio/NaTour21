package com.example.natour21.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.natour21.Enumeration.Auth;
import com.example.natour21.FirebaseLog;
import com.example.natour21.Fragment.ChatListFragment;
import com.example.natour21.Fragment.HomeFragment;
import com.example.natour21.Fragment.ReportsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.natour21.Controller.AuthenticationController;
import com.example.natour21.Pusher.PusherManager;
import com.example.natour21.R;

import static androidx.navigation.Navigation.findNavController;

public class homePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        FirebaseLog.getInstance().stopTrace("login_natour21");

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_chat_list,
                R.id.navigation_reports)
                .build();
        NavController navController = findNavController(this, R.id.nav_host_fragment);

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.navigation_logout).setOnMenuItemClickListener(menuItem -> {
            AuthenticationController.logout(this, false);
            return true;
        });
        


        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();

        PusherManager.initChatListner();
    }

    @Override
    public void onBackPressed() {
        if(!AuthenticationController.isOnHomePage)
        {
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
    }
}