package com.example.mangadrip.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import com.example.mangadrip.Classes.Manga;
import com.example.mangadrip.R;
import com.example.mangadrip.Adapter.RecyclerViewAdapter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerViewAdapter myAdapter;
    List<Manga> lstManga;
    Map<String,String> cookie;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Manga> newList = new ArrayList<>();
                for (Manga manga : lstManga) {
                    String title = manga.getTitle().toLowerCase();
                    if (title.contains(newText)) {
                        newList.add(manga);
                    }
                }
                myAdapter.setFilter(newList);
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Random rand = new Random();
                    int n = rand.nextInt(2000);
                    Thread.sleep(n);

                    Connection.Response res = Jsoup
                            .connect("https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=1")
                            .method(Connection.Method.POST)
                            .execute();

                    cookie = res.cookies();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }


                for (int k=0;k<1;k++) {
                    try {
                        Random rand = new Random();
                        int n = rand.nextInt(2000);
                        Thread.sleep(n);
                        Document doc = Jsoup.connect("https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=1")
                                .cookies(cookie)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
                                .referrer("https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=1")
                                .get();
                        Elements description = doc.select("div.list-truyen-item-wrap");
                        int length = description.size();
                        for (int i = 0; i < length; i++) {
                            String title = description.eq(i).select("a").attr("title");
                            String imgUrl = description.eq(i).select("a").select("img").attr("src");
                            int int_MangaLink = description.eq(i).select("a").eq(1).attr("abs:href").length();
                            String MangaLink = "";
                            for (int m = 0; m < int_MangaLink; m++) {
                                MangaLink += description.eq(i).select("a").eq(1).attr("abs:href").charAt(m);///
                            }

                            Manga test = (new Manga(title, MangaLink, imgUrl));
                            lstManga.add(test);
                        }
                    } catch (IOException | InterruptedException ignored) {
                        Log.d("Yuh","Something is not working");////
                    }
                    runOnUiThread(new Runnable() { public void run() { myAdapter.notifyDataSetChanged(); }});
                }
            }
        }).start();
    }


}