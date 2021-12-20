package com.example.natour21.Volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton instance = null;
    private RequestQueue requestQueue;
    private Context ctx;

    private VolleySingleton(Context ctx)
    {
        this.ctx = ctx;
        requestQueue = getRequestQueue(ctx);
    }

    public static synchronized VolleySingleton getInstance(Context ctx)
    {

        if(instance == null)
        {
            instance = new VolleySingleton(ctx);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(Context ctx)
    {
        if(requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request)
    {
        getRequestQueue(ctx).add(request);
    }
}