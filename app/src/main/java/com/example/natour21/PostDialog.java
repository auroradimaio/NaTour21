package com.example.natour21;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PostDialog extends AppCompatDialogFragment {

    private EditText editTextDurata;
    private Spinner spinnerDifficoltà;
    private Button confirmButton, cancelButton;
    private PostDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);


        editTextDurata = view.findViewById(R.id.editTextTime);
        spinnerDifficoltà = (Spinner) view.findViewById(R.id.spinner);
        String[] items = new String[]{"Facile", "Media", "Difficile"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinnerDifficoltà.setAdapter(adapter);

        confirmButton = view.findViewById(R.id.conferma_buttonDialog);

        cancelButton = view.findViewById(R.id.annulla_buttonDialog);




        builder.setView(view)
                .setTitle(Html.fromHtml("<font color='#BC6C25'>Modifica durata e/o difficoltà</font>"));

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String difficulty = spinnerDifficoltà.getSelectedItem().toString();
                String minutes = editTextDurata.getText().toString();
                listener.applyChanges(difficulty,minutes);



            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.close();
            }
        });


        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (PostDialogListener)getTargetFragment();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    public interface PostDialogListener{
        void applyChanges(String difficulty, String minutes);
        void close();
    }
}
