package com.pl.dell.news;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;



/**
 * Created by DELL on 24-05-2017.
 */
public class NewsModel {
    private int id;
   private String title;
    private String description;
    private String a_name;
    private String date;
    private String image;
    private String url;
    private String time;

    public int getImageView() {
        return imageView;
    }

    public void setImageView() {
        this.imageView = R.drawable.iconshare;
    }

    private int imageView;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        String ti=formattime(time);
        this.time = ti;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public NewsModel(int id, String title, String description, String a_name, String date, String image, String url, String time) {
        this.id=id;
        this.title = title;
        this.description = description;
        this.a_name = a_name;
        this.date = date;
        this.image = image;
        this.url=url;
        this.time=time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getA_name() {
        return a_name;
    }

    public void setA_name(String a_name) {
        if(a_name.equals("null"))
        {
            this.a_name="Anonymous";
        }
        else {
            this.a_name = a_name;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date_time) {
       String d= formatdate(date_time);
        this.date = d;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NewsModel()
    { }

   public String formatdate(String fdate)
    {
        String strCurrentDate = fdate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("MMM dd, yyyy");
        String date = format.format(newDate);

        return date;
    }

    public String formattime(String time)
    {
        String strCurrentDate = time;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("h:mm a");
        String t = format.format(newDate);

        return t;


    }
}
