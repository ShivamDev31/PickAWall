package com.droidacid.pickawall;

/**
 * Created by shivam.chopra on 17-01-2015.
 * Pojo class for Navigation Drawer Item setters
 * This is a model class for navigation list item.
 */
public class NavDrawerItem {

    private String albumId, albumTitle;

    // boolean flag to check for recent item
    private boolean isRecentAlbum = false;

    public NavDrawerItem() {

    }

    public NavDrawerItem(String albumId, String albumTitle) {
        this.albumId = albumId;
        this.albumTitle = albumTitle;
    }

    public NavDrawerItem(String albumId, String albumTitle, boolean isRecentAlbum) {
        this.albumId = albumId;
        this.albumTitle = albumTitle;
        this.isRecentAlbum = isRecentAlbum;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public boolean isRecentAlbum() {
        return isRecentAlbum;
    }

    public void setRecentAlbum(boolean isRecentAlbum) {
        this.isRecentAlbum = isRecentAlbum;
    }
}
