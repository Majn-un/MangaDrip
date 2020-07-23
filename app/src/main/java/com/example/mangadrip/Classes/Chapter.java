package com.example.mangadrip.Classes;

import java.util.Map;

public class Chapter {
    private String Name;
    private String Link;
    private String Cookie1;
    private String Cookie2;
//    private Map<String, String> Cookies;
    public Chapter () {
    }

    public Chapter(String Chapter_Title, String Chapter_Link, String cookie1, String cookie2) {
        this.Name = Chapter_Title;
        this.Link = Chapter_Link;
        this.Cookie1 = cookie1;
        this.Cookie2 = cookie2;

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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
