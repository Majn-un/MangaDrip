package com.example.mangadrip.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mangadrip.Activity.Page_Activity;
import com.example.mangadrip.Classes.Chapter;
import com.example.mangadrip.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class ChapterViewAdapter extends RecyclerView.Adapter<ChapterViewAdapter.MyViewHolder> {

    private Context context;
    private List<Chapter> Data;

    public ChapterViewAdapter(Context context, List<Chapter> Data) {
        this.context = context;
        this.Data = Data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.item_chapter,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.chapter_title.setText(Data.get(position).getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(context, Page_Activity.class);
                intent.putExtra("Name",Data.get(position).getName());
                intent.putExtra("Link",Data.get(position).getLink());
                intent.putExtra("ci_session", Data.get(position).getCookie1());
                intent.putExtra("__cfduid", Data.get(position).getCookie2());

                String list_string = "";
                for (int i =0;i<Data.size();i++) {
                    list_string += Data.get(i).getName() + " - " + Data.get(i).getLink() + " , ";
                }
                Log.d("list_string", list_string);
                intent.putExtra("Chapter_List",list_string);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView chapter_title;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
///
            chapter_title = (TextView) itemView.findViewById(R.id.chapter_title);
            cardView = (CardView) itemView.findViewById(R.id.chapter);
        }
    }

}