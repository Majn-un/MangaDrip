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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Page_Activity extends AppCompatActivity {

    List<Page> lstPages;
    private TextView chapter_title;
    private String Chapter_URL;
    Timer timer;
    private PageViewAdapter myViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);


        chapter_title = (TextView) findViewById(R.id.txt_chapter_name);

        Intent intent = getIntent();
        String Title = intent.getExtras().getString("Name");
        Chapter_URL = intent.getExtras().getString("Link");
        chapter_title.setText(Title);
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
                    timer = new Timer();
                    Log.d("Chapter Link",Chapter_URL);
                    Document doc = Jsoup.connect(Chapter_URL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").get();
                    Elements description = doc.select("div.container-chapter-reader").select("img");
                    int length = description.size();
                    for (int i = 0; i < length; i++) {
                        String Link = description.eq(0).attr("src");
                        String Page_Number = String.valueOf(i+1);
                        lstPages.add(new Page(Link,Page_Number));
                        Log.d("Testing",Link);

                    }

                } catch (IOException ignored) {
                    Log.d("Yuh","Something is not working");
                }
                runOnUiThread(new Runnable() { public void run() { myViewPager.notifyDataSetChanged(); }});
            }
        }).start();
    }



}