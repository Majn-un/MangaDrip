package com.example.mangadrip.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mangadrip.Adapter.PageViewAdapter;
import com.example.mangadrip.Classes.Page;
import com.example.mangadrip.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class Page_Activity extends AppCompatActivity {

    List<Page> lstPages;
    private TextView chapter_title;
    private String Chapter_URL;
    private PageViewAdapter myViewPager;
    private String Cookie1, Cookie2;
    private Document doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);



        Intent intent = getIntent();
        String Title = intent.getExtras().getString("Name");
        Chapter_URL = intent.getExtras().getString("Link");
        Cookie1 = intent.getExtras().getString("ci_session");
        Cookie2 = intent.getExtras().getString("__cfduid");


        lstPages = new ArrayList<>();
        getMangaPages();

        ViewPager myrv = (ViewPager) findViewById(R.id.view_page);
        myViewPager = new PageViewAdapter(this,lstPages);
        myrv.setAdapter(myViewPager);


    }

    private void getMangaPages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LinkedHashMap<String, String> cookies = new LinkedHashMap<String, String>();
                    cookies.put("__cfduid",Cookie2);
                    cookies.put("ci_session",Cookie1);
                    Log.d("page",cookies+"");

                    Document doc = Jsoup.connect(Chapter_URL)
//                            .cookies(cookies)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
                            .referrer(Chapter_URL)
                            .get();


                    Elements link_list = doc.select("div.container-chapter-reader").select("img");
                    int length = link_list.size();
                    String pattern = "\\//(.*?)\\/";
                    if (length==0) {
                        Elements Mangakakalot = doc.select("div.vung-doc").select("img");
                        Log.d("Manga",Mangakakalot+"");
                        for (int i = 0; i < Mangakakalot.size(); i++) {
                            String image_url = Mangakakalot.eq(i).attr("src");
                            Log.d("Mangakakalot",image_url);
                            String reincarnatedURL = image_url.replaceAll(pattern, "//s8.mkklcdnv8.com/");

                            String Page_Number = String.valueOf(i+1);
                            lstPages.add(new Page(reincarnatedURL,Page_Number));
                        }
                    } else {
                        Elements Manganelo = doc.select("div.container-chapter-reader").select("img");
                        for (int i = 0; i < Manganelo.size(); i++) {
                            String image_url = Manganelo.eq(i).attr("src");
                            String reincarnatedURL = image_url.replaceAll(pattern, "//s8.mkklcdnv8.com/");
                            Log.d("manganelo",image_url);
                            String Page_Number = String.valueOf(i+1);
                            lstPages.add(new Page(reincarnatedURL,Page_Number));
                        }
                    }


                } catch (IOException ignored) {
                    Log.d("Yuh","Something is not working");
                }
                runOnUiThread(new Runnable() { public void run() { myViewPager.notifyDataSetChanged(); }});
            }
        }).start();
    }

}