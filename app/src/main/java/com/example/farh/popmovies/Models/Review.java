package com.example.farh.popmovies.Models;

/**
 * Created by farh on 06/11/2016.
 */

public class Review {
    private String author;
    private String content;

    public Review() {
    }

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
