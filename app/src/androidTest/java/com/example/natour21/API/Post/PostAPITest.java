package com.example.natour21.API.Post;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.natour21.API.Review.ReviewAPI;
import com.example.natour21.Activity.homePage;
import com.example.natour21.Activity.Login;
import com.example.natour21.Controller.AuthenticationController;
import com.example.natour21.Controller.PostController;
import com.example.natour21.Fragment.HomeFragment;
import com.example.natour21.Fragment.insertPostFragment;
import com.example.natour21.R;
import com.example.natour21.Volley.VolleyCallback;
import com.google.gson.JsonObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;


@RunWith(AndroidJUnit4.class)
public class PostAPITest {

    String accessTokenScaduto = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaWtlLmZvbnNldGEiLCJyb2xlIjpbIlJPTEVfQURNSU4iXSwiaXNzIjoiaHR0cDovLzE5Mi4xNjguMS45OjgwODAvYXBpL2xvZ2luIiwiZXhwIjoxNjQzMzY4Mzc0fQ.c6s5p5ciQpqdBxzMowcN8-rMaOzikqWXNvzkQzx2jlk";
    String  accessTokenValido= "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaWtlLmZvbnNldGEiLCJyb2xlIjpbIlJPTEVfQURNSU4iXSwiaXNzIjoiaHR0cDovLzE5Mi4xNjguMS45OjgwODAvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1MDMwNTI3fQ.zLj79Z1-1j5uXPzmOc-haj7-yM_fBGfnxERSQ3wDtII";
    String titoloValido ="Sentiero perfetto!";
    String titoloNullo = null;
    String descrizioneValida = "Sentiero ottimo per poter passare qualche oretta all'aperto";
    String descrizioneNulla = null;
    String startPointValido = "Via Manzoni";
    String startPointNullo = null;
    double lat1Valido = 41.01320745897941;
    double lat1Nullo = 0;
    double lat2Valido = 41.27849811209999;
    double lat2Nullo = 0;
    double lon1Valido=14.705472439527512;
    double lon1Nullo= 0;
    double lon2Valido=15.16836304217577;
    double lon2Nullo= 0;
    String durationValida ="5";
    String durationNulla = null;
    Integer minutesValidi = 20;
    Integer minutesNulli = null;
    String userNameValido = "mike.fonseta";
    String userNameNonRegistrato ="notRegisteredUser";
    String userNameNullo = null;
    Integer difficoltàValida = 3;
    Integer difficoltàNulla = null;


    @Rule
    public ActivityTestRule<Login> loginTestRule = new ActivityTestRule(Login.class);

    @Rule
    public ActivityTestRule<homePage> homePageTestRule = new ActivityTestRule(homePage.class);

    private homePage mHomePage = null;


    @Before
    public void setUp() throws Exception{
        mHomePage = homePageTestRule.getActivity();
    }

    @Test
    public void insertPostTokenScaduto() {

            PostAPI.InsertPost(mHomePage, titoloValido, descrizioneValida, startPointValido, lat1Valido, lon1Valido, lat2Valido, lon2Valido,
                    durationValida, minutesValidi,difficoltàValida , userNameValido, accessTokenScaduto, new VolleyCallback() {

                @Override
                public void onSuccess(String response) {
                    assertEquals("{\"result\":\"Access token scaduto\",\"status\":\"TOKEN_EXPIRED\"}", response);
                }

                @Override
                public void onError(String response) {
                }
            });
    }


    @Test
    public void insertPostValido(){

        PostAPI.InsertPost(mHomePage, titoloValido, descrizioneValida, startPointValido, lat1Valido, lon1Valido, lat2Valido,
                lon2Valido, durationValida, minutesValidi,difficoltàValida , userNameValido, accessTokenValido, new VolleyCallback() {

            @Override
            public void onSuccess(String response) {
                assertEquals("\"status\":\"OK\"}",response.substring(response.length()-14));
            }

            @Override
            public void onError(String response) {
            }
        });
    }


    @Test
    public void insertPostUsernameNonRegistrato(){

        PostAPI.InsertPost(mHomePage, titoloValido, descrizioneValida, startPointValido, lat1Valido, lon1Valido, lat2Valido,
                lon2Valido, durationValida, minutesValidi,difficoltàValida , userNameNonRegistrato, accessTokenValido, new VolleyCallback() {

            @Override
            public void onSuccess(String response) {
                assertEquals("{\"result\":\"Username non registrato\",\"status\":\"FAILED\"}", response);
            }

            @Override
            public void onError(String response) {
            }
        });

    }


    @Test
    public void insertPostTitleNull(){
        PostAPI.InsertPost(mHomePage, titoloNullo, descrizioneValida, startPointValido, lat1Valido, lon1Valido, lat2Valido, lon2Valido,
                durationValida, minutesValidi,difficoltàValida , userNameValido, accessTokenValido, new VolleyCallback() {

            @Override
            public void onSuccess(String response) {
                assertEquals("{\"result\":\"Campi non validi\",\"status\":\"FAILED\"}", response);
            }

            @Override
            public void onError(String response) {
            }
        });

    }


    @Test
    public void insertPostDescriptionNull(){
        PostAPI.InsertPost(mHomePage, titoloValido, descrizioneNulla, startPointValido, lat1Valido, lon1Valido, lat2Valido, lon2Valido,
                durationValida, minutesValidi,difficoltàValida , userNameValido, accessTokenValido, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                assertEquals("\"status\":\"OK\"}",response.substring(response.length()-14));
            }

            @Override
            public void onError(String response) {
            }
        });
    }


    @Test
    public void insertPostStartPointNull(){

        PostAPI.InsertPost(mHomePage, titoloValido, descrizioneValida, startPointNullo, lat1Valido, lon1Valido, lat2Valido, lon2Valido,
                durationValida, minutesValidi,difficoltàValida , userNameValido, accessTokenValido, new VolleyCallback() {

            @Override
            public void onSuccess(String response) {
                assertEquals("{\"result\":\"Campi non validi\",\"status\":\"FAILED\"}", response);
            }

            @Override
            public void onError(String response) {
            }
        });
    }


    @Test
    public void insertPostLat1Null(){
        PostAPI.InsertPost(mHomePage, titoloValido, descrizioneValida, startPointValido, lat1Nullo, lon1Valido, lat2Valido, lon2Valido,
                durationValida, minutesValidi,difficoltàValida , userNameValido, accessTokenValido, new VolleyCallback() {

            @Override
            public void onSuccess(String response) {
                assertEquals("\"status\":\"OK\"}",response.substring(response.length()-14));
            }

            @Override
            public void onError(String response) {
            }
        });
    }


    @Test
    public void insertPostLat2Null(){
        PostAPI.InsertPost(mHomePage, titoloValido, descrizioneValida, startPointValido, lat1Valido, lon1Valido, lat2Nullo, lon2Valido,
                durationValida, minutesValidi,difficoltàValida , userNameValido, accessTokenValido, new VolleyCallback() {

            @Override
            public void onSuccess(String response) {
                assertEquals("\"status\":\"OK\"}",response.substring(response.length()-14));
            }

            @Override
            public void onError(String response) {
            }
        });
    }

    @Test
    public void insertPostLon1Null(){
        PostAPI.InsertPost(mHomePage, titoloValido, descrizioneValida, startPointValido, lat1Valido, lon1Nullo, lat2Valido, lon2Valido,
                durationValida, minutesValidi,difficoltàValida , userNameValido, accessTokenValido, new VolleyCallback() {

            @Override
            public void onSuccess(String response) {
                assertEquals("\"status\":\"OK\"}",response.substring(response.length()-14));
            }

            @Override
            public void onError(String response) {
            }
        });
    }



    @Test
    public void insertPostLon2Null(){
        PostAPI.InsertPost(mHomePage, titoloValido, descrizioneValida, startPointValido, lat1Valido, lon1Valido, lat2Valido, lon2Nullo,
                durationValida, minutesValidi,difficoltàValida , userNameValido, accessTokenValido, new VolleyCallback() {

            @Override
            public void onSuccess(String response) {
                assertEquals("\"status\":\"OK\"}",response.substring(response.length()-14));
            }

            @Override
            public void onError(String response) {
            }
        });
    }


    @Test
    public void insertPostDurationNull(){

        PostAPI.InsertPost(mHomePage, titoloValido, descrizioneValida, startPointValido, lat1Valido, lon1Valido, lat2Valido, lon2Valido,
                durationNulla, minutesValidi,difficoltàValida , userNameValido, accessTokenValido, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                assertEquals("{\"result\":\"Campi non validi\",\"status\":\"FAILED\"}", response);
            }

            @Override
            public void onError(String response) {
            }
        });
    }

    @Test
    public void insertPostMinutesNull(){
        PostAPI.InsertPost(mHomePage, titoloValido, descrizioneValida, startPointValido, lat1Valido, lon1Valido, lat2Valido, lon2Valido,
                durationValida, minutesNulli,difficoltàValida , userNameValido, accessTokenValido, new VolleyCallback() {

            @Override
            public void onSuccess(String response) {
                assertEquals("{\"result\":\"Campi non validi\",\"status\":\"FAILED\"}", response);
            }

            @Override
            public void onError(String response) {
            }
        });
    }

    @Test
    public void insertPostDifficultyNull(){
        PostAPI.InsertPost(mHomePage, titoloValido, descrizioneValida, startPointValido, lat1Valido, lon1Valido, lat2Valido, lon2Valido,
                durationValida, minutesValidi,difficoltàNulla , userNameValido, accessTokenValido, new VolleyCallback() {

            @Override
            public void onSuccess(String response) {
                assertEquals("{\"result\":\"Campi non validi\",\"status\":\"FAILED\"}", response);
            }

            @Override
            public void onError(String response) {
            }
        });
    }


    @Test
    public void insertPostUsernameNull(){
        PostAPI.InsertPost(mHomePage, titoloValido, descrizioneValida, startPointValido, lat1Valido, lon1Valido, lat2Valido, lon2Valido,
                durationValida, minutesValidi,difficoltàValida , userNameNullo, accessTokenValido, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                assertEquals("{\"result\":\"Campi non validi\",\"status\":\"FAILED\"}", response);
            }

            @Override
            public void onError(String response) {
            }
        });
    }




    @After
    public void tearDown()throws Exception{
        mHomePage = null;
    }


}