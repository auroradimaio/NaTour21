package com.example.natour21.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.natour21.Controller.AuthenticationController;
import com.example.natour21.Controller.ReportController;
import com.example.natour21.FirebaseLog;
import com.example.natour21.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import static com.example.natour21.Dialog.Dialog.showMessageDialog;

public class NewReportFragment extends Fragment {

    TextView pathName;
    Button newReportBtn;
    EditText titleEd, descriptionEd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Inserisci Segnalazione");

        FirebaseLog.getInstance().startTrace("insertReport");
        AuthenticationController.isOnHomePage = false;
        actionBar.setDisplayHomeAsUpEnabled(false);
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        navView.setVisibility(navView.GONE);
        actionBar.show();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_new_report, container, false);

        Bundle bundle = this.getArguments();
        String title = bundle.getString("TitoloSentiero");
        String postUser = bundle.getString("PostUser");
        int id = bundle.getInt("IdPost");


        AuthenticationController.isOnHomePage = false;

        pathName = v.findViewById(R.id.pathNameReport_textView);
        pathName.setText(title);

        titleEd = v.findViewById(R.id.reportTitle_EditText);
        descriptionEd = v.findViewById(R.id.reportDescription_EditText);

        newReportBtn = v.findViewById(R.id.publishReport_Button);
        newReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(titleEd.getText().toString().isEmpty() || descriptionEd.getText().toString().isEmpty()){
                    showMessageDialog(getActivity(),"Inserire tutti i campi",null);
                }else {
                    ReportController.InsertReport(getActivity(), titleEd.getText().toString(), descriptionEd.getText().toString(), id, postUser, v);
                }
            }
        });



        return v;
    }
}