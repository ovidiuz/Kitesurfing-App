package com.example.ovidiu.kitesurfingapp;

import org.json.JSONException;
import org.json.JSONObject;

public class Spot {

    private String id;
    private String name;
    private String country;
    private String whenToGo;
    private String isFavorite;

    public Spot(JSONObject jsonObject) {

        try {

            this.id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
            this.country = jsonObject.getString("country");
            this.whenToGo = jsonObject.getString("whenToGo");
            this.isFavorite = jsonObject.getString("isFavorite");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.name + "\n" + this.country + "\r\n";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWhenToGo() {
        return whenToGo;
    }

    public void setWhenToGo(String whereToGo) {
        this.whenToGo = whereToGo;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }
}
