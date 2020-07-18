package com.example.mangadrip.Classes;

public class Page {
    private String Link;
    private String Name;

    public Page() {

    }

    public Page(String link, String name) {
        this.Link = link;
        this.Name = name;
    }
    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


}
