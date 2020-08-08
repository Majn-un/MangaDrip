package com.example.mangadrip.Classes;

import java.util.Map;

public class Manga {

    private String Title;
    private String Description;
    private String Thumbnail;

    public Manga () {
    }


    public Manga(String title, String description, String thumbnail) {

        Title = title;
        Description = description;
        Thumbnail = thumbnail;
    }


    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }
}