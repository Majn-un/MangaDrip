package com.example.mangadrip.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mangadrip.Adapter.ChapterViewAdapter;
import com.example.mangadrip.Classes.Chapter;
import com.example.mangadrip.Classes.Manga;
import com.example.mangadrip.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Chapter_Activity extends AppCompatActivity {
    private ChapterViewAdapter myAdapter;
    private TextView chapter_title;
    List<Chapter> lstChapter;
    private String Manga_URL;
    SwipeRefreshLayout refreshLayout;
    private String Cookie1, Cookie2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        refreshLayout = findViewById(R.id.refreshLayout);

        Intent intent = getIntent();

        Manga_URL = intent.getExtras().getString("Description");
        Cookie1 = intent.getExtras().getString("Cookie ci_session");
        Cookie2 = intent.getExtras().getString("Cookie __cfduid");

        chapter_title = (TextView) findViewById(R.id.chapter_title);
        lstChapter = new ArrayList<>();
        getChapters();

        final RecyclerView myrv = (RecyclerView) findViewById(R.id.chapter_recycler);
        myAdapter = new ChapterViewAdapter(this, lstChapter);
        myrv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));;
        myrv.setAdapter(myAdapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChapters();
                myAdapter = new ChapterViewAdapter(Chapter_Activity.this, lstChapter);
                myrv.setLayoutManager(new LinearLayoutManager(Chapter_Activity.this, LinearLayoutManager.VERTICAL, false));;
                myrv.setAdapter(myAdapter);
                refreshLayout.setRefreshing(false);
            }
        });

    }

    private void getChapters() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LinkedHashMap<String, String> cookies = new LinkedHashMap<String, String>();
                    cookies.put("__cfduid",Cookie2);
                    cookies.put("ci_session",Cookie1);

                    Thread.sleep(1000);
                    Log.d("ChapterCookies",""+cookies);
                    Document doc = Jsoup.connect(getIntent().getStringExtra("URL")).cookies(cookies).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").get();;
                    Elements description = doc.select("div.panel-story-chapter-list").select("li");
                    int length = description.size();

                    if (length == 0) {
                        Elements des_2 = doc.select("div.chapter-list").select("div");
                        int des_2_length = des_2.size();
                        for (int i2 = 0; i2 < des_2_length; i2++) {
                            String Link = doc.select("div.chapter-list").select("div").eq(i2).select("a").attr("href");
                            String Chapter_Title = doc.select("div.chapter-list").select("div").eq(i2).select("a").attr("title");;
                            String Date = doc.select("div.chapter-list").select("div").eq(i2).eq(2).text();
                            Chapter chap = (new Chapter(Chapter_Title,Link, Cookie2, Cookie1));
                            lstChapter.add(chap);
                        }
//
                    } else {

                        for (int i = 0; i < length; i++) {

                            String Link = description.eq(i).select("li").select("a").attr("abs:href");
                            String Chapter_Title = description.eq(i).select("li").select("a").text();
                            String Date = description.eq(1).select("li").select("span").eq(1).text();
                            Chapter chap = (new Chapter(Chapter_Title, Link, Cookie2, Cookie1));
                            lstChapter.add(chap);
                            //                        Log.d("chapter",Link);
                        }

                    }
                } catch (IOException | InterruptedException ignored) {
                    Log.d("Yuh","Something is not working");
                }

                runOnUiThread(new Runnable() { public void run() { myAdapter.notifyDataSetChanged(); }});

            }
        }).start();
    }


}

// Code for fetching Chapter info for MangaHere
// try {
//         Document doc = Jsoup.connect(getIntent().getStringExtra("URL")).get();
//         Elements description = doc.select("p.title3");
//         int length = description.size();
//         for (int i = 0; i < length; i++) {
//        String Link = doc.select("ul.detail-main-list").select("li").eq(i).select("a").attr("abs:href").toString();
//        String Chapter_Title = doc.select("p.title3").eq(i).text();
//        String Date = doc.select("p.title2").eq(i).text();
//        Chapter chap = (new Chapter(Chapter_Title,Link));
//        lstChapter.add(chap);
//        }
//        } catch (IOException ignored) {
//        Log.d("Yuh","Something is not working");
//        }
//        runOnUiThread(new Runnable() { public void run() { myAdapter.notifyDataSetChanged(); }});