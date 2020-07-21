package com.example.mangadrip.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.mangadrip.Classes.Manga;
import com.example.mangadrip.R;
import com.example.mangadrip.Adapter.RecyclerViewAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerViewAdapter myAdapter;
    List<Manga> lstManga;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstManga = new ArrayList<>();
        getWebsite();

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerViewAdapter(this, lstManga);
        myrv.setLayoutManager(new GridLayoutManager(this, 3));
        myrv.setAdapter(myAdapter);


    }

    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int k=0;k<1;k++) {
                    try {

                        Document doc = Jsoup.connect("https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page="+k).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").get();
                        Elements description = doc.select("div.list-truyen-item-wrap");
                        int length = description.size();
                        for (int i = 0; i < length; i++) {
                            String title = description.eq(i).select("a").attr("title");
                            String imgUrl = description.eq(i).select("a").select("img").attr("src");
                            int int_MangaLink = description.eq(i).select("a").eq(1).attr("abs:href").length();
                            String MangaLink = "";
                            for (int m = 0; m < int_MangaLink; m++) {
                                MangaLink += description.eq(i).select("a").eq(1).attr("abs:href").charAt(m);
                            }

                            Manga test = (new Manga(title, MangaLink, imgUrl));
                            lstManga.add(test);
                        }
                    } catch (IOException ignored) {
                        Log.d("Yuh","Something is not working");
                    }
                    runOnUiThread(new Runnable() { public void run() { myAdapter.notifyDataSetChanged(); }});
                }
            }
        }).start();
    }

}