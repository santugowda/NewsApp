package com.example.santhosh.newsapp.adapter.viewitems;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.santhosh.newsapp.ArticleDetailedActivity;
import com.example.santhosh.newsapp.R;
import com.example.santhosh.newsapp.model.Docs;

import java.util.List;

public class NewsTextViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

    public  TextView newsTextView;
    private List<Docs> mNewsDocs;
    private Context mContext;

    public NewsTextViewHolder(Context context, View view, List<Docs> newsDocs) {
        super(view);

        this.mNewsDocs = newsDocs;
        this.mContext = context;
        newsTextView = (TextView) view.findViewById(R.id.newsTextView);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getLayoutPosition();
        Docs docs = mNewsDocs.get(position);
        Toast.makeText(mContext, "Loading news..", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(mContext, ArticleDetailedActivity.class);
        i.putExtra("webUrl", docs.webUrl);
        mContext.startActivity(i);
    }

    public TextView getNewsTextView() {
        return newsTextView;
    }

    public void setNewsTextView(TextView newsTextView) {
        this.newsTextView = newsTextView;
    }
}
