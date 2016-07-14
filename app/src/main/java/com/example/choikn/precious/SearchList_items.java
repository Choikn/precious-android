package com.example.choikn.precious;

import android.graphics.drawable.Drawable;

/**
 * Created by ChoiKN on 2016-07-14.
 */
public class SearchList_items {
    private String photo;
    private String titleStr ;
    private int descStr ;
    private Drawable icon;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public int getDescStr() {
        return descStr;
    }

    public void setDescStr(int descStr) {
        this.descStr = descStr;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
