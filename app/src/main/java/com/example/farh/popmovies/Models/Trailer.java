package com.example.farh.popmovies.Models;

/**
 * Created by farh on 05/11/2016.
 */

public class Trailer {
    String name , key;

    public Trailer() {
    }

    public Trailer(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
