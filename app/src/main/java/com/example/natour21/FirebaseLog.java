package com.example.natour21;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

public class FirebaseLog {

    private Trace currentTrace;
    private String currentTraceName;
    private static FirebaseLog instance;

    private FirebaseLog(){}

    public static FirebaseLog getInstance(){
        if(instance == null){
            instance = new FirebaseLog();
        }
        return instance;
    }

    public void startTrace(String traceName){
        if(!traceName.equals(currentTraceName)){
            this.currentTrace = FirebasePerformance.getInstance().newTrace(traceName);
            this.currentTraceName = traceName;
            currentTrace.start();
        }
    }

    public void stopTrace(String traceName){
        if(traceName.equals(currentTraceName) && currentTrace != null){
            currentTrace.stop();
            currentTrace = null;
        }
    }

}
