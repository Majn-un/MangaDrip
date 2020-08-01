package com.example.mangadrip.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Manga_Activity extends AppCompatActivity {
    private RecyclerViewAdapter myAdapter;
    private Button button_for_chapters;
    SwipeRefreshLayout refreshLayout;
    private TextView manga_title, manga_description, manga_status, manga_author;
    private ImageView img;
    private String Manga_URL;
    String Cookie1;
    String Cookie2;
    Map<String, String> cookies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_);

        refreshLayout = findViewById(R.id.refreshLayout);

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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                manga_author.setText("");
                manga_description.setText("");
                manga_status.setText("");
                getMangaData();
                refreshLayout.setRefreshing(false);
            }
        });
        manga_title.setText(title);
//        manga_description.setText();
        Picasso.get().load(cover).into(img);

        button_for_chapters = (Button) findViewById(R.id.chapters_button);
        button_for_chapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Manga_Activity.this,Chapter_Activity.class);
                intent.putExtra("URL",Manga_URL);
                intent.putExtra("Cookie ci_session", Cookie1);
                intent.putExtra("Cookie __cfduid", Cookie2);
                startActivity(intent);
            }
        });

    }

    private void getMangaData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection.Response res = Jsoup
                            .connect(Manga_URL)
                            .method(Connection.Method.POST)
                            .execute();

                    cookies = res.cookies();
                    Cookie2 = res.cookie("__cfduid");
                    Cookie1 = res.cookie("ci_session");

                    Log.d("manga cookies",""+cookies);
                    Document doc = Jsoup.connect(Manga_URL)
//                            .cookies(cookies)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
//                            .referrer(Manga_URL)
                            .get();

                    final String author = doc.select("td.table-value").eq(1).text();
                    final String status =  doc.select("td.table-value").eq(2).text();
                    final String description = doc.select("div.panel-story-info-description").text();
                    setValues(description,author,status);
                    if (author.length() == 0) {
                        final String author_2 = doc.select("ul.manga-info-text").select("li").eq(1).select("a").eq(0).text() + " " + doc.select("ul.manga-info-text").select("li").eq(1).select("a").eq(1).text();
                        final String status_2 = doc.select("ul.manga-info-text").select("li").eq(2).text();
                        final Elements counter_description_2 = doc.select("div#noidungm").select("br");
                        setValues("There is no description",author_2,status_2);
                    }

                } catch (IOException ignored) {
                    Log.d("Yuh","Something is not working");
                }

            }
        }).start();
    }

    private void setValues(final String description, final String author, final String status) {
        runOnUiThread(new Runnable() {
            public void run() {
                manga_description.setText(description);
                manga_author.setText(author);
                manga_status.setText(status);
            }
        });
    }
}
