package com.example.mangadrip.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangadrip.Database.DatabaseHelper;
import com.example.mangadrip.R;
import com.example.mangadrip.Adapter.RecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.regex.Matcher;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class Manga_Activity extends AppCompatActivity {
    private RecyclerViewAdapter myAdapter;
    private Button button_for_chapters;
    SwipeRefreshLayout refreshLayout;


    private TextView manga_title, manga_description, manga_status, manga_author;
    private ImageView img;
    private String Manga_URL;
    private String cookies1;
    private String cookies2;
    ProgressDialog progressDialog;
    DatabaseHelper myDB;
    private String dbMangaTitle;
    private String dbMangaImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_);

        progressDialog = new ProgressDialog(Manga_Activity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDB = new DatabaseHelper(this);

        refreshLayout = findViewById(R.id.refreshLayout);

        manga_status = (TextView) findViewById(R.id.status);
        manga_author = (TextView) findViewById(R.id.author);
        manga_title = (TextView) findViewById(R.id.depth_title);
        manga_description = (TextView) findViewById(R.id.depth_description);
        img = (ImageView) findViewById(R.id.manga_thumbnail);

        Intent intent = getIntent();
        Manga_URL = Objects.requireNonNull(intent.getExtras()).getString("URL");
        dbMangaImg = Objects.requireNonNull(intent.getExtras()).getString("Thumbnail");
        dbMangaTitle = Objects.requireNonNull(intent.getExtras()).getString("Name");

        getMangaData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMangaData();
                refreshLayout.setRefreshing(false);
            }
        });

        button_for_chapters = (Button) findViewById(R.id.chapters_button);
        button_for_chapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Manga_Activity.this,Chapter_Activity.class);
                intent.putExtra("URL",Manga_URL);
                intent.putExtra("__cfduid",cookies1);
                intent.putExtra("ci_session",cookies2);
                startActivity(intent);
            }
        });

        Button button_for_favorite = (Button) findViewById(R.id.favorite_button);
        button_for_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddData(Manga_URL, dbMangaTitle, dbMangaImg);
            }
        });

//        Button button_for_resume = (Button) findViewById(R.id.resume_button);
//        button_for_resume.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


    }

    private void AddData(String manga_url, String title, String thumbnail) {
        boolean insertData = myDB.addData(manga_url, title, thumbnail);

        if (insertData==true) {
            Toast.makeText(Manga_Activity.this,"Successfully Entered Data!",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(Manga_Activity.this,"Yuh it no work",Toast.LENGTH_LONG).show();

        }

    }

    private void getMangaData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Random rand = new Random();
                    int n = rand.nextInt(2000);
                    Thread.sleep(n);

                    Connection.Response res = Jsoup
                            .connect(Manga_URL)
                            .method(Connection.Method.POST)
                            .execute();

                    cookies1 = res.cookie("__cfduid");
                    cookies2 = res.cookie("ci_session");

                    Map<String, String> cookies = res.cookies();
//                    Log.d("Manga cookies",cookies +"");
                    Document doc = Jsoup.connect(Manga_URL)
                            .cookies(cookies)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
                            .referrer(Manga_URL)
                            .get();
                    Pattern Manganelo = Pattern.compile("//manganelo.com/");
                    Pattern Mangakakalot = Pattern.compile("//mangakakalot.com/");

                    Matcher Manganelo_Searcher = Manganelo.matcher(Manga_URL);
                    Matcher Mangakaklot_Searcher = Mangakakalot.matcher(Manga_URL);

                    if (Manganelo_Searcher.find()) {
                         String author = doc.select("td.table-value").eq(1).text();
                         String status =  doc.select("td.table-value").eq(2).text();
                         String description = doc.select("div.panel-story-info-description").text();
                         String img_URL = doc.select("span.info-image").select("img").attr("src");
                         String title = doc.select("div.story-info-right").select("h1").text();
                        setValues(description,author,status,title,img_URL);
                    } else if (Mangakaklot_Searcher.find()) {
                         String author_2 = doc.select("ul.manga-info-text").select("li").eq(1).select("a").eq(0).text() + " " + doc.select("ul.manga-info-text").select("li").eq(1).select("a").eq(1).text();
                         String status_2 = doc.select("ul.manga-info-text").select("li").eq(2).text();
                         String description_2 = doc.select("div#noidungm").text();
                         String title_2 = doc.select("ul.manga-info-text").select("h1").eq(0).text();
                         String img_URL = doc.select("div.manga-info-pic").select("img").attr("src");
                        setValues(description_2,author_2,status_2,title_2,img_URL);
                    } else {
                        Log.d("Not Found","Could not find the source");
                    }

                } catch (IOException | InterruptedException ignored) {
                    Log.d("Yuh","Something is not working");
                }

            }
        }).start();
    }

    private void setValues(final String description, final String author, final String status, final String title, final String img_URL) {
        runOnUiThread(new Runnable() {
            public void run() {
                dbMangaTitle = title;
                dbMangaImg = img_URL;
                manga_description.setText(description);
                manga_author.setText("Author: " +author);
                manga_status.setText("Status: " +status);
                manga_title.setText(title);
                Picasso.get().load(img_URL).into(img);
                progressDialog.dismiss();
            }
        });
    }
}
