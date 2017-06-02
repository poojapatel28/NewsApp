package com.pl.dell.news;

import java.util.ArrayList;

/**
 * Created by DELL on 02-06-2017.
 */
public class UserPrefrence {
    private String mUid;
    private ArrayList<String> mpref;

    public UserPrefrence() {
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public ArrayList<String> getMpref() {
        return mpref;
    }

    public void setMpref(ArrayList<String> mpref) {
        this.mpref = mpref;
    }

    public UserPrefrence(String mName, ArrayList<String> mpref) {
        this.mUid = mName;
        this.mpref = mpref;
    }
}
