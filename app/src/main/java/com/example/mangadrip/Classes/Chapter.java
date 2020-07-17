package com.example.mangadrip.Classes;

public class Chapter {
    private String Name;
    private String Link;

    public Chapter () {
    }

    public Chapter(String Chapter_Title, String Chapter_Link) {
        this.Name = Chapter_Title;
        this.Link = Chapter_Link;
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
