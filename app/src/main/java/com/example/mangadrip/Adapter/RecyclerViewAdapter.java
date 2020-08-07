package com.example.mangadrip.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangadrip.Activity.Manga_Activity;
import com.example.mangadrip.Classes.Manga;
import com.example.mangadrip.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Manga> Data;

    public RecyclerViewAdapter(Context context, List<Manga> Data) {
        this.context = context;
        this.Data = Data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.item_manga,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.manga_title.setText(Data.get(position).getTitle());
        Picasso.get().load(Data.get(position).getThumbnail()).into(holder.manga_img);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(context, Manga_Activity.class);
                intent.putExtra("URL",Data.get(position).getDescription());
                context.startActivity(intent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView manga_title;
        ImageView manga_img;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            manga_title = (TextView) itemView.findViewById(R.id.manga_title_id);
            manga_img = (ImageView) itemView.findViewById(R.id.manga_cover_id);
            cardView = (CardView) itemView.findViewById(R.id.manga);
        }
    }

    public void setFilter (ArrayList<Manga> newList) {
        Data = new ArrayList<>();
        Data.addAll(newList);
        notifyDataSetChanged();
    }

}
