package com.thl.gtwmob;

public interface Callback {

    void callback(int id,String subject,String content);
    //boolean callback(int id,int ind,int total);

    void callbackLocation(String subject,Double[] location);
    void callbackSignalStrength(int strength);

}
