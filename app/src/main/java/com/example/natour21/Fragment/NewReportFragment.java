package com.example.natour21.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.natour21.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewReportFragment extends Fragment {

    TextView pathName;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Inserisci Segnalazione");
        actionBar.setDisplayHomeAsUpEnabled(false);
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        navView.setVisibility(navView.GONE);
        actionBar.show();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report, container, false);

        Bundle bundle = this.getArguments();
        String title = bundle.getString("TitoloSentiero");

        pathName = view.findViewById(R.id.pathNameReport_textView);
        pathName.setText(title);



        return view;
    }
}