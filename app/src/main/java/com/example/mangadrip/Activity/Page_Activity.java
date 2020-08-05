package com.example.mangadrip.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.mangadrip.Adapter.PageViewAdapter;
import com.example.mangadrip.Classes.Chapter;
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
    private String Chapter_URL;
    private TextView chapter_title;
    private PageViewAdapter myViewPager;
    private String Cookie1, Cookie2;
    private Document doc;
    private List<Chapter> list;
    ProgressDialog progressDialog;
    private TextView leftBubble;
    private TextView rightBubble;
    private int index;
    private ViewPager myrv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        Button next = (Button) findViewById(R.id.button2);
        Button previous = (Button) findViewById(R.id.button1);

        progressDialog = new ProgressDialog(Page_Activity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        Intent intent = getIntent();
        String Title = intent.getExtras().getString("Name");
        Chapter_URL = intent.getExtras().getString("Link");
        Cookie1 = intent.getExtras().getString("ci_session");
        Cookie2 = intent.getExtras().getString("__cfduid");
        list = (List<Chapter>) intent.getSerializableExtra("LIST");

        for (int i=0;i<list.size();i++) {
            if (list.get(i).getLink().equals(Chapter_URL)) {
                index = i;
                break;
            }
        }
        lstPages = new ArrayList<>();
        getMangaPages();

        myrv = (ViewPager) findViewById(R.id.view_page);
        myViewPager = new PageViewAdapter(this, lstPages);
        myrv.setAdapter(myViewPager);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == 0) {
                    Log.d("Newest Chapter Enabled","YUH");
                } else {
                    Chapter_URL = list.get(index - 1).getLink();
                    Log.d("Chapter Link Previous",Chapter_URL );
                    index = index-1;
                    lstPages = new ArrayList<>();
                    getMangaPages();
                    myViewPager = new PageViewAdapter(Page_Activity.this, lstPages);
                    myrv.setAdapter(myViewPager);
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index+1 == list.size()) {
                    Log.d("Newest Chapter Enabled", "YUH");
                } else {
                    Chapter_URL = list.get(index + 1).getLink();
                    index = index + 1;
                    Log.d("Chapter Link Next", Chapter_URL);
                    lstPages = new ArrayList<>();
                    getMangaPages();
                    myViewPager = new PageViewAdapter(Page_Activity.this, lstPages);
                    myrv.setAdapter(myViewPager);


                }
            }
        });

    }

    private void getMangaPages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LinkedHashMap<String, String> cookies = new LinkedHashMap<String, String>();
                    cookies.put("__cfduid",Cookie2);
                    cookies.put("ci_session",Cookie1);
//                    Log.d("page",cookies+"");
                    Log.d("Chapter_URL",Chapter_URL);
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
                        for (int i = 0; i < Mangakakalot.size(); i++) {
                            String image_url = Mangakakalot.eq(i).attr("src");
                            String reincarnatedURL = image_url.replaceAll(pattern, "//s8.mkklcdnv8.com/");

                            String Page_Number = String.valueOf(i+1);
                            lstPages.add(new Page(reincarnatedURL,Page_Number));
                        }
                    } else {
                        Elements Manganelo = doc.select("div.container-chapter-reader").select("img");
                        for (int i = 0; i < Manganelo.size(); i++) {
                            String image_url = Manganelo.eq(i).attr("src");
                            String reincarnatedURL = image_url.replaceAll(pattern, "//s8.mkklcdnv8.com/");
                            String Page_Number = String.valueOf(i+1);
                            lstPages.add(new Page(reincarnatedURL,Page_Number));
                        }
                    }

                    progressDialog.dismiss();


                } catch (IOException ignored) {
                    Log.d("Yuh","Something is not working");
                }
                runOnUiThread(new Runnable() { public void run() { myViewPager.notifyDataSetChanged(); }});
            }
        }).start();
    }

}