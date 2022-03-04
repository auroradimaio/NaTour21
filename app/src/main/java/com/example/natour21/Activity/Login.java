package com.example.natour21.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.natour21.FirebaseLog;
import com.example.natour21.R;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.example.natour21.Controller.AuthenticationController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

import static com.example.natour21.Dialog.Dialog.showMessageDialog;

public class Login extends AppCompatActivity {

    //Facebook
    CallbackManager callbackFacebook = CallbackManager.Factory.create();
    //Google
    GoogleSignInClient googleSignInClient;

    ActivityResultLauncher<Intent> mLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AuthenticationController.checkLogin(Login.this);


        //Facebook init
        callbackFacebook = CallbackManager.Factory.create();

        //Google init
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("953040007013-vjkb4i3ep4od9076og3bv97n24lqcsce.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        LoginWithGoogle(task);
                    }
                });


        EditText username = findViewById(R.id.et_username);
        EditText password = findViewById(R.id.et_password);
        Button btnLoginWithFacebook = findViewById(R.id.btnLoginWithFacebook);
        Button btnLoginWithGoogle = findViewById(R.id.btnLoginWithGoogle);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        CheckBox rememberMe = findViewById(R.id.rememberMe);
        TextView forgetPassword = findViewById(R.id.passwordRecovery);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    FirebaseLog.getInstance().startTrace("login_natour21");
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    FirebaseLog.getInstance().startTrace("login_natour21");
                }
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgetPassword.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthenticationController.loginNATOUR21(Login.this, username.getText().toString().toLowerCase(Locale.ROOT), password.getText().toString(), rememberMe.isChecked());
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        // Callback registration
        btnLoginWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginWithFacebook();
            }
        });

        btnLoginWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInClient.signOut();
                Intent signInIntent = googleSignInClient.getSignInIntent();
                mLauncher.launch(signInIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackFacebook.onActivityResult(requestCode, resultCode, data);
    }

    private void LoginWithFacebook() {

        LoginManager.getInstance().registerCallback(callbackFacebook,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {
                                            String email = response.getJSONObject().getString("email");
                                            String id = response.getJSONObject().getString("id");
                                            AuthenticationController.loginWithFacebook(Login.this, email, id);

                                        } catch (JSONException e) {
                                            showMessageDialog(Login.this, "Errore durante l'autenticazione", null);
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        showMessageDialog(Login.this, "Errore durante l'autenticazione", null);
                    }
                });

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    private void LoginWithGoogle(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            AuthenticationController.loginWithGoogle(this, account.getEmail());
        } catch (ApiException e) {
            Log.e("ERROR", e.getMessage());
            if(e.getStatus().getStatusCode() != 12501) {
                showMessageDialog(Login.this, "Errore durante l'autenticazione ", null);
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}