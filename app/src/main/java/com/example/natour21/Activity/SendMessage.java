package com.example.natour21.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.natour21.Controller.ChatController;
import com.example.natour21.FirebaseLog;
import com.example.natour21.R;

import java.util.Locale;

public class SendMessage extends AppCompatActivity {

    private EditText username,messageContent;
    private Button sendMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_new_message);

        FirebaseLog.getInstance().startTrace("send_new_message");

        sendMessageButton = findViewById(R.id.btnSendNewMessage);
        username = findViewById(R.id.username_new_message);
        messageContent = findViewById(R.id.message_new_message);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatController.sendNewMessage(SendMessage.this, username.getText().toString().toLowerCase(Locale.ROOT), messageContent.getText().toString());
                username.setText("");
                messageContent.setText("");
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
    }
}