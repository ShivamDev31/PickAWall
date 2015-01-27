package com.droidacid.pickawall.picasa.model;

/**
 * Created by shivam.chopra on 14-01-2015.
 * POJO class to get and set the values which we get from the JSON data
 */
public class Category {
    public String id, title;

    public Category() {

    }

    public Category(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
