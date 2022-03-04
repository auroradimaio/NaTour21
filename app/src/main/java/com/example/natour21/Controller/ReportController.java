package com.example.natour21.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import com.android.volley.RequestQueue;
import com.example.natour21.API.Report.ReportAPI;
import com.example.natour21.Activity.SingleReport;
import com.example.natour21.FirebaseLog;
import com.example.natour21.Item.Report;
import com.example.natour21.Fragment.ReportsFragment;
import com.example.natour21.Volley.VolleyCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.natour21.Dialog.Dialog.showMessageDialog;

public class ReportController {


    public static FragmentActivity reportListActivity;
    public static boolean onReportList = false;
    private static List<Report> reports = new ArrayList<>();

    public static void InsertReport(Activity activity, String title, String description, int id_post, String postUser, View view){

        ReportAPI.InsertReport(activity, title, description, id_post ,AuthenticationController.user_username,postUser, AuthenticationController.accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("OK")) {
                        FirebaseLog.getInstance().stopTrace("insertReport");
                        showMessageDialog(activity, "Segnalazione inviata con successo!", null);
                        Navigation.findNavController(view).popBackStack();
                    }
                    else if(jsonObject.getString("status").equals("FAILED")){
                        showMessageDialog(activity,"Hai già segnalato questo post",null);


                    }else if(jsonObject.getString("status").equals("TOKEN_EXPIRED"))
                    {
                        AuthenticationController.logout(activity, true);
                    }
                }catch (JSONException jsonException){
                    showMessageDialog(activity,"Hai già segnalato questo post",null);

                }
            }

            @Override
            public void onError(String response) {
                showMessageDialog(activity,"Hai già segnalato questo post",null);

            }
        });

    }

    public static void getReportList() {

        reports.clear();

        ReportAPI.getReports(reportListActivity, AuthenticationController.user_username, AuthenticationController.accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONArray current = (JSONArray) jsonArray.get(i);
                            Report report = new Report(new Long((Integer) current.get(0)),
                                    (String) current.get(1),
                                    (String) current.get(2),
                                    (String) current.get(3),
                                    (String) current.get(4),
                                    (String) current.get(5),
                                    null);

                            if (current.get(6).equals(null)) {
                                report.setResponse("");
                            } else {
                                report.setResponse((String) current.get(6));
                            }
                            reports.add(report);

                        }
                        ReportsFragment.updateUI(reports);
                    }else if(jsonObject.getString("status").equals("TOKEN_EXPIRED"))
                    {
                        AuthenticationController.logout(reportListActivity, true);
                    }


                } catch (JSONException jsonException) {
                    showMessageDialog(reportListActivity,"Impossibile visualizzare le segnalazioni", null);
                }
            }

            @Override
            public void onError(String response) {
                showMessageDialog(reportListActivity,"Impossibile visualizzare le segnalazioni", null);
            }
        });
    }

    public static void openSingleReport(Activity activity, Report report)
    {
        Intent intent = new Intent(activity, SingleReport.class);
        intent.putExtra("post_owner", report.getPostOwner());
        intent.putExtra("post_title", report.getPostTitle());
        intent.putExtra("report_id", report.getId());
        intent.putExtra("report_title", report.getTitle());
        intent.putExtra("report_description", report.getDescription());
        intent.putExtra("report_sender", report.getSender());
        intent.putExtra("response", report.getResponse());
        activity.startActivity(intent);
    }

    public static void sendResponse(Activity activity, Long reportId, String response_message) {

        response_message = response_message.trim();
        if(response_message.length() > 0) {
            String finalResponse_message = response_message;
            ReportAPI.sendResponse(activity, reportId, AuthenticationController.user_username, response_message, AuthenticationController.accessToken, new VolleyCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("status").equals("OK")) {
                            showMessageDialog(activity, "Risposta inviata", null);
                            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                            SingleReport.updateUI(finalResponse_message);
                        }else if(jsonObject.getString("status").equals("TOKEN_EXPIRED"))
                        {
                            AuthenticationController.logout(activity, true);
                        }

                    } catch (JSONException jsonException) {
                        showMessageDialog(activity, "Impossibile inviare la risposta", null);
                    }
                }

                @Override
                public void onError(String response) {
                    showMessageDialog(activity, "Impossibile inviare la risposta", null);
                }
            });
        }
    }

    public static void getReportById(int reportId, ImageView imageView, RequestQueue requestQueue){
        ReportAPI.getReportById(reportId,imageView,requestQueue,AuthenticationController.accessToken);
    }

}
