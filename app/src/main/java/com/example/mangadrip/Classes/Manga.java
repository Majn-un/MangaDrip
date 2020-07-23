package com.example.mangadrip.Classes;

import java.util.Map;

public class Manga {

    private String Title;
    private String Description;
    private String Thumbnail;
    private String Cookie1;
    private String Cookie2;

    public Manga () {
    }

    public String getCookie1() {
        return Cookie1;
    }

    public void setCookie1(String cookie1) {
        Cookie1 = cookie1;
    }

    public String getCookie2() {
        return Cookie2;
    }

    public void setCookie2(String cookie2) {
        Cookie2 = cookie2;
    }

    public Manga(String title, String description, String thumbnail, String cookie1, String cookie2) {
        Cookie1 = cookie1;
        Cookie2 = cookie2;
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
