package com.example.natour21.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.natour21.Activity.*;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.example.natour21.API.User.UserAPI;
import com.example.natour21.Enumeration.Auth;
import com.example.natour21.Pusher.PusherManager;
import com.example.natour21.Volley.VolleyCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import static com.example.natour21.Dialog.Dialog.showMessageDialog;

public class AuthenticationController {

    public static String accessToken;
    public static String user_username;
    public static String auth;
    public static boolean isOnHomePage=false;


    public static void checkLogin(AppCompatActivity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("rememberMe", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("remember","").equals("true")) {

            accessToken = sharedPreferences.getString("accessToken", "");
            user_username = sharedPreferences.getString("username", "");
            auth = sharedPreferences.getString("auth", "");

            if(isAccessTokenExpired())
            {
                logout(activity, true);
            }
            else
            {
                activity.startActivity(new Intent(activity, homePage.class));
                activity.finish();
            }
        }

    }

    public static Boolean isAccessTokenExpired(){

        String[] accessTokenPart = accessToken.split("\\.");
        String payload = accessTokenPart[1];

        try {

            byte[] decodedPayload = Base64.decode(payload, Base64.DEFAULT);
            payload = new String(decodedPayload,"UTF-8");
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            JSONObject obj = new JSONObject(payload);
            long expireDate = obj.getLong("exp");
            Timestamp timestampExpireDate = new Timestamp(expireDate * 1000);
            long time = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(time);
            return timestamp.after(timestampExpireDate);

        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static void loginNATOUR21(Activity activity, String username, String password, boolean rememberMe)
    {
        if (isNetworkConnected(activity.getApplication().getApplicationContext())) {
            if (username.length() > 0 && password.length() > 0) {
                if(!username.equals(" ") && username.length() < 32) {
                    ProgressDialog progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("Accesso in corso...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);

                    UserAPI.checkAuth(activity, username, new VolleyCallback() {
                        @Override
                        public void onSuccess(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("OK")) {
                                    if (jsonObject.getString("result").equals(Auth.NATOUR21.toString())) {
                                        auth = jsonObject.getString("result");
                                        UserAPI.login(activity, username, password, new VolleyCallback() {
                                            @Override
                                            public void onSuccess(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    if (jsonObject.getString("status").equals("OK")) {
                                                        accessToken = jsonObject.getJSONObject("result").getString("accessToken");
                                                        AuthenticationController.user_username = jsonObject.getJSONObject("result").getString("username");
                                                        if (rememberMe) {
                                                            SharedPreferences sharedPreferences = activity.getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString("remember", "true");
                                                            editor.putString("username", AuthenticationController.user_username);
                                                            editor.putString("accessToken", accessToken);
                                                            editor.putString("auth", auth);
                                                            editor.apply();
                                                        }
                                                        progressDialog.dismiss();
                                                        activity.startActivity(new Intent(activity, homePage.class));
                                                    } else if (jsonObject.getString("status").equals("FAILED")) {
                                                        progressDialog.dismiss();
                                                        showMessageDialog(activity, jsonObject.getString("result"), null);
                                                    }
                                                } catch (JSONException jsonException) {
                                                    progressDialog.dismiss();
                                                    showMessageDialog(activity, "Errore nel recupero delle credenziali", null);
                                                }
                                            }

                                            @Override
                                            public void onError(String response) {
                                                progressDialog.dismiss();
                                                showMessageDialog(activity, "Errore nel recupero delle credenziali, riprovare più tardi", null);
                                            }
                                        });
                                    } else {
                                        progressDialog.dismiss();
                                        showMessageDialog(activity, "Account non registrato tramite autenticazione NaTour21", null);
                                    }
                                } else if (jsonObject.getString("status").equals("FAILED")) {
                                    progressDialog.dismiss();
                                    showMessageDialog(activity, jsonObject.getString("result"), null);
                                }
                            } catch (JSONException jsonException) {
                                progressDialog.dismiss();
                                showMessageDialog(activity, "Errore nel recupero delle credenziali", null);
                            }
                        }

                        @Override
                        public void onError(String response) {
                            progressDialog.dismiss();
                            showMessageDialog(activity, "Errore nel recupero delle credenziali, riprovare più tardi", null);
                        }
                    });
                }
            } else {
                showMessageDialog(activity, "Inserire le credenziali di accesso", null);
            }
        }else
        {
            showMessageDialog(activity, "Per favore controlla la tua connessione di rete", null);
        }
    }

    public static void loginWithFacebook(AppCompatActivity activity, String email, String id)
    {
        UserAPI.loginFacebook(activity,email,id, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK"))
                    {
                        jsonObject = jsonObject.getJSONObject("result");
                        if(jsonObject.getString("action").equals("LOGIN")) {
                            ProgressDialog progressDialog = new ProgressDialog(activity);
                            progressDialog.setMessage("Accesso in corso...");
                            progressDialog.show();
                            progressDialog.setCancelable(false);
                            UserAPI.login(activity, jsonObject.getString("data"), id, new VolleyCallback() {
                                @Override
                                public void onSuccess(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getString("status").equals("OK")) {
                                            accessToken = jsonObject.getJSONObject("result").getString("accessToken");
                                            user_username = jsonObject.getJSONObject("result").getString("username");
                                            auth = Auth.FACEBOOK.toString();
                                            SharedPreferences sharedPreferences = activity.getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("remember", "true");
                                            editor.putString("username", user_username);
                                            editor.putString("accessToken", accessToken);
                                            editor.putString("auth", auth);
                                            editor.apply();
                                            progressDialog.dismiss();
                                            activity.startActivity(new Intent(activity, homePage.class));
                                            
                                        } else if (jsonObject.getString("status").equals("FAILED")) {
                                            progressDialog.dismiss();
                                            showMessageDialog(activity, jsonObject.getString("result"), null);
                                        }
                                    } catch (JSONException jsonException) {
                                        progressDialog.dismiss();
                                        showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
                                    }
                                }

                                @Override
                                public void onError(String response) {
                                    showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
                                }
                            });
                        }else if(jsonObject.getString("action").equals("REGISTER"))
                        {
                            Intent intent = new Intent(activity, RegistrationServiceHandler.class);
                            intent.putExtra("email", email);
                            intent.putExtra("id", id);
                            intent.putExtra("isFacebook", true);
                            activity.startActivity(intent);
                            
                        }
                    }
                    else if(jsonObject.getString("status").equals("FAILED"))
                    {
                        showMessageDialog(activity, "Email già registrata", null);
                    }
                } catch (JSONException jsonException) {
                    showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
                }
            }

            @Override
            public void onError(String response) {
                showMessageDialog(activity, "Errore nel recupero delle informazioni, riprovare più tardi", null);
            }
        });
    }

    public static void loginWithGoogle(AppCompatActivity activity, String email)
    {
        UserAPI.loginGoogle(activity,email, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK"))
                    {
                        jsonObject = jsonObject.getJSONObject("result");
                        if(jsonObject.getString("action").equals("LOGIN")) {
                            ProgressDialog progressDialog = new ProgressDialog(activity);
                            progressDialog.setMessage("Accesso in corso...");
                            progressDialog.show();
                            progressDialog.setCancelable(false);
                            UserAPI.login(activity, jsonObject.getString("data"), email + jsonObject.getString("data") + "GOOGLE_AUTH", new VolleyCallback() {
                                @Override
                                public void onSuccess(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getString("status").equals("OK")) {
                                            accessToken = jsonObject.getJSONObject("result").getString("accessToken");
                                            user_username = jsonObject.getJSONObject("result").getString("username");
                                            auth = Auth.GOOGLE.toString();
                                            SharedPreferences sharedPreferences = activity.getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("remember", "true");
                                            editor.putString("username", user_username);
                                            editor.putString("accessToken", accessToken);
                                            editor.putString("auth", auth);
                                            editor.apply();
                                            progressDialog.dismiss();
                                            activity.startActivity(new Intent(activity, homePage.class));
                                            
                                        } else if (jsonObject.getString("status").equals("FAILED")) {
                                            progressDialog.dismiss();
                                            showMessageDialog(activity, jsonObject.getString("result"), null);
                                        }
                                    } catch (JSONException jsonException) {
                                        progressDialog.dismiss();
                                        showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
                                    }
                                }

                                @Override
                                public void onError(String response) {
                                    showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
                                }
                            });
                        }else if(jsonObject.getString("action").equals("REGISTER"))
                        {
                            Intent intent = new Intent(activity, RegistrationServiceHandler.class);
                            intent.putExtra("email", email);
                            intent.putExtra("isFacebook", false);
                            activity.startActivity(intent);
                            
                        }
                    }
                    else if(jsonObject.getString("status").equals("FAILED"))
                    {
                        showMessageDialog(activity, "Email già registrata", null);
                    }
                } catch (JSONException jsonException) {
                    showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
                }
            }

            @Override
            public void onError(String response) {
                showMessageDialog(activity, "Errore nel recupero delle informazioni, riprovare più tardi", null);
            }
        });
    }

    public static void registerNATOUR21(Activity activity, String username, String email, String password, String confirmPassword) {
        if(isNetworkConnected(activity.getApplication().getApplicationContext())) {
            if(username.length() > 0 && email.length() > 0 && password.length() > 0 && confirmPassword.length() > 0) {

                if(isUsernameValid(username)) {
                    if (isEmailValid(email)) {
                        if (password.length() >= 6) {
                            if (password.equals(confirmPassword)) {

                                ProgressDialog progressDialog = new ProgressDialog(activity);
                                progressDialog.setMessage("Registrazione in corso...");
                                progressDialog.show();
                                progressDialog.setCancelable(false);

                                UserAPI.registerNATOUR21(activity, username, email, password, new VolleyCallback() {
                                    @Override
                                    public void onSuccess(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getString("status").equals("OK")) {
                                                showMessageDialog(activity, "Registrazione effettuata con successo", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        activity.startActivity(new Intent(activity, Login.class));
                                                    }
                                                });

                                            } else if (jsonObject.getString("status").equals("FAILED")) {
                                                showMessageDialog(activity, jsonObject.getString("result"), null);
                                            }
                                            progressDialog.dismiss();
                                        } catch (JSONException jsonException) {
                                            progressDialog.dismiss();
                                            showMessageDialog(activity, "Errore durante la registrazione, riprovare più tardi", null);
                                        }
                                    }

                                    @Override
                                    public void onError(String response) {
                                        progressDialog.dismiss();
                                        showMessageDialog(activity, "Errore durante la registrazione, riprovare più tardi", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                activity.startActivity(new Intent(activity, Login.class));
                                            }
                                        });
                                    }
                                });
                            } else {
                                showMessageDialog(activity, "Le password non corrispondono", null);
                            }
                        } else {
                            showMessageDialog(activity, "La password deve essere composta da almeno 6 caratteri", null);
                        }
                    } else {
                        showMessageDialog(activity, "Inserire un indirizzo email valido", null);
                    }
                }else{
                    showMessageDialog(activity, "Il nome utente può contenere solo lettere minuscole, numeri e punti (\".\") \n" +
                            "Il nome utente deve essere composto da almeno 4 caratteri\n" +
                            "Il nome utente deve essere composto da massimo 32 caratteri", null);
                }
            }
            else
            {
                showMessageDialog(activity, "Per poter procedere con la registrazione è necessario fornire le informazioni richieste", null);
            }
        }
        else
        {
            showMessageDialog(activity, "Per favore controlla la tua connessione di rete", null);
        }
    }

    public static void registerFACEBOOK(Activity activity, String id, String email, String username) {

        if(isUsernameValid(username)) {
            UserAPI.registerFACEBOOK(activity, id, email, username , new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    if(jsonObject.getString("status").equals("OK"))
                    {
                        ProgressDialog progressDialog = new ProgressDialog(activity);
                        progressDialog.setMessage("Accesso in corso...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        UserAPI.login(activity,
                                jsonObject.getJSONObject("result").getString("username"),
                                id, new VolleyCallback() {
                            @Override
                            public void onSuccess(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("OK")) {
                                        accessToken = jsonObject.getJSONObject("result").getString("accessToken");
                                        user_username = jsonObject.getJSONObject("result").getString("username");
                                        auth = Auth.FACEBOOK.toString();
                                        SharedPreferences sharedPreferences = activity.getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("remember", "true");
                                        editor.putString("username", user_username);
                                        editor.putString("accessToken", accessToken);
                                        editor.putString("auth", auth);
                                        editor.apply();
                                        progressDialog.dismiss();
                                        activity.startActivity(new Intent(activity, homePage.class));
                                        
                                    } else if (jsonObject.getString("status").equals("FAILED")) {
                                        progressDialog.dismiss();
                                        showMessageDialog(activity, jsonObject.getString("result"), null);
                                    }
                                } catch (JSONException jsonException) {
                                    progressDialog.dismiss();
                                    showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
                                }
                            }

                            @Override
                            public void onError(String response) {
                                progressDialog.dismiss();
                                showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
                            }
                        });
                    }else if(jsonObject.getString("status").equals("FAILED"))
                    {
                        showMessageDialog(activity, jsonObject.getString("result"), null);
                    }
                } catch (JSONException e) {
                    showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
                }
            }

            @Override
            public void onError(String response) {
                showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
            }
        });
        }else{
            showMessageDialog(activity, "Il nome utente può contenere solo lettere minuscole, numeri e punti (\".\") \n" +
                    "Il nome utente deve essere composto da almeno 4 caratteri\n" +
                    "Il nome utente deve essere composto da massimo 32 caratteri", null);
        }
    }

    public static void registerGOOGLE(Activity activity, String email, String username) {
        if(isUsernameValid(username)) {
            UserAPI.registerGOOGLE(activity, email, username , new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    if(jsonObject.getString("status").equals("OK"))
                    {
                        ProgressDialog progressDialog = new ProgressDialog(activity);
                        progressDialog.setMessage("Accesso in corso...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        UserAPI.login(activity,
                                jsonObject.getJSONObject("result").getString("username"),
                                email + jsonObject.getJSONObject("result").getString("username") + "GOOGLE_AUTH", new VolleyCallback() {
                                    @Override
                                    public void onSuccess(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getString("status").equals("OK")) {
                                                accessToken = jsonObject.getJSONObject("result").getString("accessToken");
                                                user_username = jsonObject.getJSONObject("result").getString("username");
                                                auth = Auth.GOOGLE.toString();
                                                SharedPreferences sharedPreferences = activity.getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("remember", "true");
                                                editor.putString("username", user_username);
                                                editor.putString("accessToken", accessToken);
                                                editor.putString("auth", auth);
                                                editor.apply();
                                                progressDialog.dismiss();
                                                activity.startActivity(new Intent(activity, homePage.class));
                                                
                                            } else if (jsonObject.getString("status").equals("FAILED")) {
                                                progressDialog.dismiss();
                                                showMessageDialog(activity, jsonObject.getString("result"), null);
                                            }
                                        } catch (JSONException jsonException) {
                                            progressDialog.dismiss();
                                            showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
                                        }
                                    }

                                    @Override
                                    public void onError(String response) {
                                        progressDialog.dismiss();
                                        showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
                                    }
                                });
                    }else if(jsonObject.getString("status").equals("FAILED"))
                    {
                        showMessageDialog(activity, jsonObject.getString("result"), null);
                    }
                } catch (JSONException e) {
                    showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
                }
            }

            @Override
            public void onError(String response) {
                showMessageDialog(activity, "Errore nel recupero delle informazioni", null);
            }
        });
        }else{
            showMessageDialog(activity, "Il nome utente può contenere solo lettere minuscole, numeri e punti (\".\") \n" +
                    "Il nome utente deve essere composto da almeno 4 caratteri\n" +
                    "Il nome utente deve essere composto da massimo 32 caratteri", null);
        }
    }

    public static void logout(Activity activity, boolean isTokenExpired)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        PusherManager.disconnect();

        if(!isTokenExpired)
        {
            activity.startActivity(new Intent(activity, Login.class));
            activity.finish();
            if(auth.equals(Auth.FACEBOOK.toString()))
            {
                LoginManager.getInstance().logOut();
            }
            if(auth.equals(Auth.GOOGLE.toString()))
            {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("392196802809-sai0ku2334vtbt4e6p84lee9g1ne3po1.apps.googleusercontent.com")
                        .requestEmail()
                        .build();
                GoogleSignIn.getClient(activity, gso).signOut();
            }
        }else
        {
            showMessageDialog(activity, "Sessione scaduta, effettuare l'accesso", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.startActivity(new Intent(activity, Login.class));
                    
                }
            });
        }
    }

    public static void changePassword(Activity activity, String username, String password, String confirmPassword) {
        if (password.length() >= 6) {
            if (password.equals(confirmPassword)) {

                UserAPI.changePassword(activity,username,password, new VolleyCallback() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("status").equals("OK"))
                            {
                                showMessageDialog(activity, "Informazioni aggiornate correttamente", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        activity.startActivity(new Intent(activity,Login.class));
                                    }
                                });
                            }else if(jsonObject.getString("status").equals("FAILED"))
                            {
                                showMessageDialog(activity, "Reimpostazione password fallita", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        activity.startActivity(new Intent(activity,Login.class));
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            showMessageDialog(activity, "Reimpostazione password fallita", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    activity.startActivity(new Intent(activity,Login.class));
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(String response) {
                        showMessageDialog(activity, "Reimpostazione password fallita", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.startActivity(new Intent(activity,Login.class));
                            }
                        });
                    }
                });

            } else {
                showMessageDialog(activity, "Le password non corrispondono", null);
            }
        } else {
            showMessageDialog(activity, "La password deve essere composta da almeno 6 caratteri", null);
        }
    }

    public static void openVerifyOTP(Activity activity, String email) {

        if(isEmailValid(email) && email.length() > 0) {
            UserAPI.sendEmail(activity,email, new VolleyCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("status").equals("OK") && jsonObject.getJSONObject("result").getString("email").equals(email))
                        {
                            Intent intent = new Intent(activity, VerifyOTP.class);
                            intent.putExtra("code", jsonObject.getJSONObject("result").getString("code"));
                            intent.putExtra("email", jsonObject.getJSONObject("result").getString("email"));
                            intent.putExtra("username", jsonObject.getJSONObject("result").getString("username"));
                            activity.startActivity(intent);
                        }else if(jsonObject.getString("status").equals("FAILED"))
                        {
                            showMessageDialog(activity, email + " non è associata a nessun account", null);
                        }
                    } catch (JSONException e) {
                        showMessageDialog(activity,"Impossibile inviare email di recupero, riprovare più tardi", null);
                    }
                }

                @Override
                public void onError(String response) {
                    showMessageDialog(activity,"Impossibile inviare email di recupero, riprovare più tardi", null);
                }
            });
        }else
        {
            showMessageDialog(activity,"Inserire un indirizzo email valido", null);
        }
    }

    public static void openChangePassword(Activity activity, String username, String code, String userCode) {
        if(userCode.length() > 0){
            if(userCode.equals(code))
            {
                Intent intent = new Intent(activity, ChangePassword.class);
                intent.putExtra("username", username);
                activity.startActivity(intent);
            }else
            {
                showMessageDialog(activity,"Codice invalido", null);
            }
        }else
        {
            showMessageDialog(activity,"Inserire il codice per continuare", null);
        }
    }

    private static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isUsernameValid(String username) {
        return username.length() >= 4 && username.length() <= 32 && username.matches("[a-z.0-9]*") && !username.contains(" ");
    }


    private static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}

