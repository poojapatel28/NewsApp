package com.pl.dell.news;

import android.graphics.drawable.Drawable;

/**
 * Created by DELL on 27-05-2017.
 */
public class logo {

    String name;
    Drawable logo;

    public logo(String name, Drawable logo) {
        this.name = name;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getLogo() {
        return logo;
    }

    public void setLogo(Drawable logo) {
        this.logo = logo;
    }
}
