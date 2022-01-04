package com.example.natour21;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.example.natour21.R;

public class Dialog {

    public static void showMessageDialog(Activity activity, String message, View.OnClickListener clickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customdialog, viewGroup, false);

        TextView messageTV = dialogView.findViewById(R.id.messagetxt);
        messageTV.setText(message);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        Button okButton = dialogView.findViewById(R.id.okButton);
        if(clickListener == null)
        {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        }else {
            okButton.setOnClickListener(clickListener);
        }
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

}