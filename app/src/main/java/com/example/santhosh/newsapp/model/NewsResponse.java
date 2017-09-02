package com.example.santhosh.newsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsResponse {
    @SerializedName("docs")
    @Expose
    private List<Docs> docs = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<Docs> getDocs() {
        return docs;
    }

    public void setArticles(List<Docs> docs) {
        this.docs = docs;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
