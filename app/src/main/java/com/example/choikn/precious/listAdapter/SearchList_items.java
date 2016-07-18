package com.example.choikn.precious.listAdapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by ChoiKN on 2016-07-14.
 */
public class SearchList_items {
    private boolean favorite;
    private int pId;
    private String photo;
    private String titleStr;
    private String descStr;
    private Bitmap bitmap;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int id) {
        this.pId = id;
    }

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

    public String getDescStr() {
        return descStr;
    }

    public void setDescStr(String descStr) {
        this.descStr = descStr;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
