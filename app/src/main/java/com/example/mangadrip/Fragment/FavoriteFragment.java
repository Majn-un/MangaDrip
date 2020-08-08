package com.example.mangadrip.Fragment;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangadrip.Adapter.RecyclerViewAdapter;
import com.example.mangadrip.Classes.Manga;
import com.example.mangadrip.Database.DatabaseHelper;
import com.example.mangadrip.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private RecyclerViewAdapter myAdapter;
    ProgressDialog progressDialog;

    List<Manga> lstManga;
    DatabaseHelper myDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        lstManga = new ArrayList<>();
        myDB = new DatabaseHelper(getActivity());
        Cursor data = myDB.getListContents();

        if (data.getCount() == 0) {
            Toast.makeText(getActivity(), "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                Manga manga = new Manga(data.getString(1), data.getString(2),data.getString(3));
                lstManga.add(manga);
                myDB.close();
                RecyclerView myrv = view.findViewById(R.id.favorite_id);
                myAdapter = new RecyclerViewAdapter(getActivity(), lstManga);
                myrv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                myrv.setAdapter(myAdapter);
                progressDialog.dismiss();

            }
        }
        return view;

    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        getActivity().getMenuInflater().inflate(R.menu.toolbar_item, menu);
//        MenuItem searchViewItem = menu.findItem(R.id.action_search);
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) searchViewItem.getActionView();
//        searchView.setQueryHint("Search...");
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//        searchView.setIconifiedByDefault(false);
//
//        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                newText = newText.toLowerCase();
//                ArrayList<Manga> newList = new ArrayList<>();
//                for (Manga manga : lstManga) {
//                    String title = manga.getTitle().toLowerCase();
//                    if (title.contains(newText)) {
//                        newList.add(manga);
//                    }
//                }
//                myAdapter.setFilter(newList);
//                return true;
//            }
//        };
//
//        searchView.setOnQueryTextListener(queryTextListener);
//    }
}