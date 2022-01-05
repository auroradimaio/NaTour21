package com.example.natour21.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.natour21.Controller.ReviewController;
import com.example.natour21.Dialog;
import com.example.natour21.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class insertReviewFragment extends Fragment {

    Button publish;
    RatingBar ratingBar;
    TextView pathTitle;
    EditText commentEditText;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Inserisci Recensione");
        actionBar.setDisplayHomeAsUpEnabled(false);
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        navView.setVisibility(navView.GONE);
        actionBar.show();
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_review, container, false);

        Bundle bundle = this.getArguments();
        int id = bundle.getInt("IdPost");
        String title = bundle.getString("TitoloSentiero");
        Toast.makeText(getActivity(),"Valore="+id,Toast.LENGTH_SHORT).show();

        commentEditText = view.findViewById(R.id.comment_editTextTextMultiLine);

        ratingBar = view.findViewById(R.id.review_ratingBar);

        pathTitle = view.findViewById(R.id.pathNameValue_textView);
        pathTitle.setText(title);

        publish = view.findViewById(R.id.publishReview_button);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Valore="+ratingBar.getRating(),Toast.LENGTH_SHORT).show();
                Dialog dialog = new Dialog();
                if(commentEditText.getText().toString().isEmpty() || ratingBar.getRating()==0){
                    Toast.makeText(getActivity(),"Inserire una recensione valida",Toast.LENGTH_SHORT).show();
                }else {


                    dialog.showMessageDialog(getActivity(), "Recensione inserita con successo", null);
                    ReviewController.insertReview(getActivity(), commentEditText.getText().toString(), ratingBar.getRating(), id);
                }
            }
        });


        return view;
    }
}