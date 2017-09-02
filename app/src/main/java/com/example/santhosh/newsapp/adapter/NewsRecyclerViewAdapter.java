package com.example.santhosh.newsapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.santhosh.newsapp.R;
import com.example.santhosh.newsapp.adapter.viewitems.NewsTextViewHolder;
import com.example.santhosh.newsapp.adapter.viewitems.NewsThumbnailTextViewHolder;
import com.example.santhosh.newsapp.model.Docs;

import java.util.ArrayList;
import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<Docs> mDocsList;
    private static Context mContext;

    private final int TEXTONLY = 0, THUMBNAIL = 1;

    public NewsRecyclerViewAdapter(Context context, ArrayList<Docs> docsList) {
        this.mDocsList = docsList;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return this.mDocsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Docs newsDoc = mDocsList.get(position);
        if (TextUtils.isEmpty(newsDoc.getArticleThumbnailUrl())) {
            return TEXTONLY;
        }
        return THUMBNAIL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case TEXTONLY:
                View newsText = inflater.inflate(R.layout.grid_view_item_template_text,
                        viewGroup, false);
                viewHolder = new NewsTextViewHolder(mContext, newsText, mDocsList);
                break;
            case THUMBNAIL:
                View newsThumbnail = inflater.inflate(R.layout.grid_view_item_template_thumbnail,
                        viewGroup, false);
                viewHolder = new NewsThumbnailTextViewHolder(mContext, newsThumbnail, mDocsList);
                break;
            default:
                newsThumbnail = inflater.inflate(R.layout.grid_view_item_template_thumbnail,
                        viewGroup, false);
                viewHolder = new NewsThumbnailTextViewHolder(mContext, newsThumbnail, mDocsList);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TEXTONLY:
                NewsTextViewHolder newsTextViewHolder = (NewsTextViewHolder) viewHolder;
                configureTextArticleViewHolder(newsTextViewHolder, position);
                break;
            case THUMBNAIL:
                NewsThumbnailTextViewHolder newsThumbnailTextViewHolder = (NewsThumbnailTextViewHolder) viewHolder;
                configureThumbnailArticleViewHolder(newsThumbnailTextViewHolder, position);
                break;
            default:
                NewsThumbnailTextViewHolder thumbnailTextViewHolder = (NewsThumbnailTextViewHolder) viewHolder;
                configureThumbnailArticleViewHolder(thumbnailTextViewHolder, position);
                break;
        }
    }

    private void configureTextArticleViewHolder(NewsTextViewHolder viewHolder, int position) {
        Docs docs = mDocsList.get(position);
        viewHolder.newsTextView.setText(docs.getHeadline().getMain());
    }

    private void configureThumbnailArticleViewHolder(NewsThumbnailTextViewHolder viewHolder,
                                                     int position) {
        Docs docs = mDocsList.get(position);

        viewHolder.imageNews.setImageResource(0);
        if (!TextUtils.isEmpty(docs.getArticleThumbnailUrl())) {
            Glide.with(mContext).load(docs.getArticleThumbnailUrl())
                    .placeholder(R.mipmap.ic_holder)
                    .into(viewHolder.imageNews);
        }
        viewHolder.thumbnailTextNews.setText(docs.getHeadline().getMain());
    }
}