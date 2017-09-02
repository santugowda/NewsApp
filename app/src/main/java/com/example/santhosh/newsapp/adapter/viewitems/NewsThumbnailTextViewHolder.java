package com.example.santhosh.newsapp.adapter.viewitems;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.santhosh.newsapp.ArticleDetailedActivity;
import com.example.santhosh.newsapp.R;
import com.example.santhosh.newsapp.model.Docs;

import java.util.List;

public class NewsThumbnailTextViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
    public ImageView imageNews;
    public TextView thumbnailTextNews;
    private List<Docs> articles;
    private Context mContext;

    public NewsThumbnailTextViewHolder(Context context, View view, List<Docs> mArticles) {
        super(view);

        this.articles = mArticles;
        this.mContext = context;
        imageNews = (ImageView) view.findViewById(R.id.imageNews);
        thumbnailTextNews = (TextView) view.findViewById(R.id.thumbnailTextNews);
        view.setOnClickListener(this);
    }

    // Handles the row being being clicked
    @Override
    public void onClick(View view) {
        int position = getLayoutPosition();
        Docs article = articles.get(position);
        Toast.makeText(mContext, "Loading news..", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(mContext, ArticleDetailedActivity.class);
        i.putExtra("webUrl", article.webUrl);
        mContext.startActivity(i);
    }

    public ImageView getImageNews() {
        return imageNews;
    }

    public void setImageNews(ImageView imageNews) {
        this.imageNews = imageNews;
    }

    public TextView getThumbnailTextNews() {
        return thumbnailTextNews;
    }

    public void setThumbnailTextNews(TextView thumbnailTextNews) {
        this.thumbnailTextNews = thumbnailTextNews;
    }
}
