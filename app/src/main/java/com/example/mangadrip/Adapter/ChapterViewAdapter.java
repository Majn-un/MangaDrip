package com.example.mangadrip.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mangadrip.Activity.Chapter_Activity;
import com.example.mangadrip.Classes.Chapter;
import com.example.mangadrip.R;

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
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View v){
//                Intent intent = new Intent(context, Chapter_Activity.class);
//                intent.putExtra("Name",Data.get(position).getName());
//                intent.putExtra("Link",Data.get(position).getLink());
//                context.startActivity(intent);
//            }
//        });
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

            chapter_title = (TextView) itemView.findViewById(R.id.chapter_title);
//            cardView = (CardView) itemView.findViewById(R.id.chapter);
        }
    }

}
