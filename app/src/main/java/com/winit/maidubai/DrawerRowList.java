package com.winit.maidubai;

/**
 * Created by WINIT on 28-Jun-16.
 */
public class DrawerRowList {


    int imageid;
    String titles;
    String arbicTitle;

    public DrawerRowList(int imageid, String titles,String arbicTitle) {
        this.imageid = imageid;
        this.titles = titles;
        this.arbicTitle = arbicTitle;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getTitles() {
        return titles;
    }

    public String getArbicTitle() {
        return arbicTitle;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    @Override
    public String toString() {
        return "DrawerRowList{" +
                "imageid=" + imageid +
                ", titles='" + titles + '\'' +
                '}';
    }

}
