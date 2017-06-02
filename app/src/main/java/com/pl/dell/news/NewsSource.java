package com.pl.dell.news;

/**
 * Created by DELL on 30-05-2017.
 */
public class NewsSource {
    String sourceName;
    String id;
    public NewsSource()
    {

    }

    public NewsSource(String sourceName, String id) {
        this.sourceName = sourceName;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
