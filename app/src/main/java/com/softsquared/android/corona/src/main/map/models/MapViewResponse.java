package com.softsquared.android.corona.src.main.map.models;

import androidx.core.app.NotificationCompat;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MapViewResponse {

    @SerializedName("result")
    ArrayList<RouteResponse> arrayList;

    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    public ArrayList<RouteResponse> getArrayList() {
        return arrayList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
