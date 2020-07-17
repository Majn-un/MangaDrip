package com.example.mangadrip.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mangadrip.R;
import com.example.mangadrip.Adapter.RecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Manga_Activity extends AppCompatActivity {
    private RecyclerViewAdapter myAdapter;
    private Button button_for_chapters;

    private TextView manga_title, manga_description, manga_status, manga_author;
    private ImageView img;
    private String Manga_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_);


        manga_status = (TextView) findViewById(R.id.status);
        manga_author = (TextView) findViewById(R.id.author);
        manga_title = (TextView) findViewById(R.id.depth_title);
        manga_description = (TextView) findViewById(R.id.depth_description);
        img = (ImageView) findViewById(R.id.manga_thumbnail);

        Intent intent = getIntent();
        String title = intent.getExtras().getString("Title");
        Manga_URL = intent.getExtras().getString("URL");
        String cover = intent.getExtras().getString("Thumbnail");

        getMangaData();
        manga_title.setText(title);
//        manga_description.setText();
        Picasso.get().load(cover).into(img);

        button_for_chapters = (Button) findViewById(R.id.chapters_button);
        button_for_chapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Manga_Activity.this,Chapter_Activity.class);
                intent.putExtra("URL",Manga_URL);
                startActivity(intent);
        }
        });
    }

    private void getMangaData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(Manga_URL).get();
                    final String author = doc.select("p.detail-info-right-say").text();
                    final String status =  doc.select("span.detail-info-right-title-tip").text();
                    final String description = doc.select("p.detail-info-right-content").text();
                    Elements tag_detail = doc.select("p.detail-info-right-tag-list");
                    int length = tag_detail.size();
                    String tags = "";
                    for (int i = 0; i < length; i++) {
                        tags += tag_detail.eq(i).text() + " ";
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            manga_description.setText(description);
                            manga_author.setText(author);
                            manga_status.setText(status);
                        }
                    });

                } catch (IOException ignored) {
                    Log.d("Yuh","Something is not working");
                }

            }
        }).start();
    }

}

// Code for Fetching Manga Detail for MangaHere
//try {
//        Document doc = Jsoup.connect(Manga_URL).get();
//final String author = doc.select("p.detail-info-right-say").text();
//final String status =  doc.select("span.detail-info-right-title-tip").text();
//final String description = doc.select("p.detail-info-right-content").text();
//        Elements tag_detail = doc.select("p.detail-info-right-tag-list");
//        int length = tag_detail.size();
//        String tags = "";
//        for (int i = 0; i < length; i++) {
//        tags += tag_detail.eq(i).text() + " ";
//        }
//        runOnUiThread(new Runnable() {
//public void run() {
//        manga_description.setText(description);
//        manga_author.setText(author);
//        manga_status.setText(status);
//        }
//        });