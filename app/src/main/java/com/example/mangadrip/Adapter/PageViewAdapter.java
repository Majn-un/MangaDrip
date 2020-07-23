package com.example.mangadrip.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.mangadrip.Classes.Page;
import com.example.mangadrip.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PageViewAdapter extends PagerAdapter {

    Context context;
    List<Page> PageList;
    LayoutInflater inflater;

    public PageViewAdapter(Context context, List<Page> PageList) {
        this.context = context;
        this.PageList = PageList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return PageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View image_layout = inflater.inflate(R.layout.view_pager_item,container,false);
        PhotoView page_image = (PhotoView) image_layout.findViewById(R.id.page_image);
        Picasso.get().load(PageList.get(position).getLink()).into(page_image);


        container.addView(image_layout);
        return image_layout;
    }
}
