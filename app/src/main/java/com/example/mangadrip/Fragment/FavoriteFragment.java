package com.example.mangadrip.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mangadrip.DatabaseHelper;
import com.example.mangadrip.R;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    DatabaseHelper myDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        ListView listView = view.findViewById(R.id.listView);
        myDB = new DatabaseHelper(getActivity());
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        if (data.getCount() == 0) {
            Toast.makeText(getActivity(), "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                theList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, theList);
                listView.setAdapter(listAdapter);
            }
        }
        return view;

    }
}