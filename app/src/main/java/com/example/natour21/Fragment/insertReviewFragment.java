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
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.natour21.Controller.AuthenticationController;
import com.example.natour21.Controller.ReviewController;
import com.example.natour21.FirebaseLog;
import com.example.natour21.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import static com.example.natour21.Dialog.Dialog.showMessageDialog;


public class insertReviewFragment extends Fragment {

    Button publish;
    RatingBar ratingBar;
    TextView pathTitle;
    EditText commentEditText;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseLog.getInstance().startTrace("insertReview");
        AuthenticationController.isOnHomePage = false;
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
        View v = inflater.inflate(R.layout.fragment_insert_review, container, false);

        Bundle bundle = this.getArguments();
        int id = bundle.getInt("IdPost");
        String title = bundle.getString("TitoloSentiero");

        commentEditText = v.findViewById(R.id.comment_editTextTextMultiLine);

        ratingBar = v.findViewById(R.id.review_ratingBar);

        pathTitle = v.findViewById(R.id.pathNameValue_textView);
        pathTitle.setText(title);

        publish = v.findViewById(R.id.publishReview_button);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentEditText.getText().toString().isEmpty() || ratingBar.getRating()==0){
                    showMessageDialog(getActivity(),"Inserire una recensione valida",null);
                }else {

                    ReviewController.insertReview(getActivity(), commentEditText.getText().toString(), ratingBar.getRating(), id,v);

                }
            }
        });


        return v;
    }
}