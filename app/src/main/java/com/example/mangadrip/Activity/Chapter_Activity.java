package com.example.mangadrip.Activity;

import androidx.appcompat.app.AppCompatActivity;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Chapter_Activity extends AppCompatActivity {
    private ChapterViewAdapter myAdapter;
    private TextView chapter_title;
    List<Chapter> lstChapter;
    private String Manga_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        Intent intent = getIntent();
//        Manga_URL = intent.getExtras().getString("Description");
//        Log.d("yuh",Manga_URL);

        chapter_title = (TextView) findViewById(R.id.chapter_title);
        lstChapter = new ArrayList<>();
        getChapters();

        RecyclerView myrv = (RecyclerView) findViewById(R.id.chapter_recycler);
        myAdapter = new ChapterViewAdapter(this, lstChapter);
        myrv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));;
        myrv.setAdapter(myAdapter);

    }

    private void getChapters() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(getIntent().getStringExtra("URL")).get();
                    Elements description = doc.select("p.title3");
                    int length = description.size();
                    for (int i = 0; i < length; i++) {
                        String Link = doc.select("ul.detail-main-list").select("li").eq(i).select("a").attr("abs:href").toString();
                        String Chapter_Title = doc.select("p.title3").eq(i).text();
                        String Date = doc.select("p.title2").eq(i).text();
                        Chapter chap = (new Chapter(Chapter_Title,Link));
                        lstChapter.add(chap);
                    }
                } catch (IOException ignored) {
                    Log.d("Yuh","Something is not working");
                }
                runOnUiThread(new Runnable() { public void run() { myAdapter.notifyDataSetChanged(); }});

            }
        }).start();
    }


}