package com.example.natour21.Pusher;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.natour21.API.Post.PostAPI;
import com.example.natour21.Controller.PostController;
import com.example.natour21.Fragment.HomeFragment;
import com.google.android.material.snackbar.Snackbar;
import com.example.natour21.Controller.AuthenticationController;
import com.example.natour21.Controller.ChatController;
import com.example.natour21.Controller.ReportController;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import org.json.JSONException;
import org.json.JSONObject;

public class PusherManager {

    private static Pusher pusher;
    public static Activity activity;

   public static Channel channel_username;


    public static void initChatListner() {

        PusherOptions options = new PusherOptions();
        options.setCluster("eu");

        pusher = new Pusher("fbb723678ebc506b906c", options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "STATE: " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {

            }
        }, ConnectionState.ALL);

        if (AuthenticationController.user_username != null) {
            channel_username = pusher.subscribe(AuthenticationController.user_username);
        }


        if (channel_username != null) {

            channel_username.bind("newMessage", new SubscriptionEventListener() {
                @Override
                public void onEvent(PusherEvent event) {
                    if (event.getEventName().equals("newMessage")) {
                        try {
                            JSONObject jsonObject = new JSONObject(event.getData());
                            String from = jsonObject.getString("from");
                            if (ChatController.onChatList) {
                                ChatController.getChatList();
                            } else if (ChatController.onSingleChat) {
                                if (ChatController.chattingWith.equals(from)) {
                                    ChatController.updateSingleChat(from, jsonObject.getString("content"), jsonObject.getLong("time"));
                                } else {
                                    showToast(activity, "Nuovo messaggio da " + from);
                                }
                            } else {
                                showToast(activity, "Nuovo messaggio da " + from);
                            }
                        } catch (JSONException jsonException) {

                        }
                    }
                }
            });


            channel_username.bind("report", new SubscriptionEventListener() {
                @Override
                public void onEvent(PusherEvent event) {
                    if (event.getEventName().equals("report")) {
                        try {
                            JSONObject jsonObject = new JSONObject(event.getData());
                            String from = jsonObject.getString("from");
                            if (ReportController.onReportList) {
                                ReportController.getReportList();
                            } else {
                                showToast(activity, "Nuova segnalazione da " + from);
                            }
                        } catch (JSONException jsonException) {

                        }
                    }
                }
            });

            channel_username.bind("report_response", new SubscriptionEventListener() {
                @Override
                public void onEvent(PusherEvent event) {
                    if (event.getEventName().equals("report_response")) {
                        String from = event.getData().replaceAll("^\"+|\"+$", "");
                        if (ReportController.onReportList) {
                            ReportController.getReportList();
                        } else {
                            showToast(activity, from + " ha risposto alla tua segnalazione");
                        }
                    }
                }
            });
        }

    }
    private static void showToast(Activity activity, String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View parentLayout = activity.findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);
                final ViewGroup.LayoutParams params = snackbar.getView().getLayoutParams();
                if (params instanceof CoordinatorLayout.LayoutParams) {
                    ((CoordinatorLayout.LayoutParams) params).gravity = Gravity.TOP;
                } else {
                    ((FrameLayout.LayoutParams) params).gravity = Gravity.TOP;
                }
                snackbar.getView().setLayoutParams(params);
                snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
                snackbar.show();
            }
        });
    }

    public static void disconnect()
    {
        if(pusher != null) {
            pusher.disconnect();
        }
    }

}
