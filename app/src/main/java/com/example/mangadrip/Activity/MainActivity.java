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
                try {
                    Document doc = Jsoup.connect("https://www.mangahere.cc/ranking/").get();
                    Elements description = doc.select("p.manga-list-1-item-title");
                    int length = description.size();
                    String cont = description.eq(1).text();
                    for (int i = 0; i < length; i++) {
//                        Log.d("imgUrl", imgUrl);
                        String title = doc.select("img.manga-list-1-cover").eq(i).attr("alt");
                        String imgUrl = doc.select("img.manga-list-1-cover").eq(i).attr("src");
                        String MangaLink = doc.select("p.manga-list-1-item-title > a").eq(i).attr("abs:href");;
//                        Log.d("Yuh",des);
                        Manga test = (new Manga(title,MangaLink,imgUrl));
                        lstManga.add(test);
                    }
                } catch (IOException ignored) {
                    Log.d("Yuh","Something is not working");
                }
                runOnUiThread(new Runnable() { public void run() { myAdapter.notifyDataSetChanged(); }});

            }
        }).start();
    }


}
