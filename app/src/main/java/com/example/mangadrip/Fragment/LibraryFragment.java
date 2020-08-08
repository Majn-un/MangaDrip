package com.example.mangadrip.Fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangadrip.Activity.MainActivity;
import com.example.mangadrip.Adapter.RecyclerViewAdapter;
import com.example.mangadrip.Classes.Manga;
import com.example.mangadrip.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LibraryFragment extends Fragment {
    List<Manga> lstManga;
    private RecyclerViewAdapter myAdapter;
    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_library, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        lstManga = new ArrayList<>();
        getWebsite();


        RecyclerView myrv = view.findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerViewAdapter(getActivity(), lstManga);
        myrv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        myrv.setAdapter(myAdapter);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar_item, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
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

//                    cookie = res.cookies();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }


                for (int k=0;k<1;k++) {
                    try {
                        Random rand = new Random();
                        int n = rand.nextInt(2000);
                        Thread.sleep(n);
                        Document doc = Jsoup.connect("https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=1")
//                                .cookies(cookie)
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
                        Log.d("Yuh","Something is not working");
                    }
                    progressDialog.dismiss();
                    getActivity().runOnUiThread(new Runnable() { public void run() { myAdapter.notifyDataSetChanged(); }});
                }
            }
        }).start();
    }


}
