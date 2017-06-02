package com.pl.dell.news;

import java.util.ArrayList;

/**
 * Created by Jinesh Soni on 30/07/2016.
 */
public class User implements java.io.Serializable {
	private String mName;
	private String mEmail;
	private String mToken;
	private String mFbID;
	private String mPic;
	private String mUid;



    private ArrayList<String> pref;

	public User() {
	}


    public User(String mName, String mEmail, String mToken, String mFbID, String mPic, String mUid) {
        this.mName = mName;
        this.mEmail = mEmail;
        this.mToken = mToken;
        this.mFbID = mFbID;
        this.mPic = mPic;
        this.mUid = mUid;

    }

    public String getmUid() {
		return mUid;
	}

	public void setmUid(String mUid) {
		this.mUid = mUid;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmEmail() {
		return mEmail;
	}

	public void setmEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public String getmToken() {
		return mToken;
	}

	public void setmToken(String mToken) {
		this.mToken = mToken;
	}

	public String getmFbID() {
		return mFbID;
	}

	public void setmFbID(String mFbID) {
		this.mFbID = mFbID;
	}

	public String getmPic() {
		return mPic;
	}

	public void setmPic(String mPic) {
		this.mPic = mPic;
	}
}
