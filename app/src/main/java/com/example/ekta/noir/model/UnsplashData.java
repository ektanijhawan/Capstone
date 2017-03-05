package com.example.ekta.noir.model;

import java.io.Serializable;

/**
 * Created by Ekta on 24-02-2017.
 */

public class UnsplashData implements Serializable {

    private String id, createdAt, color, urlFull, urlRegular, categoryID, categoryTitle, username;
    private int width, height;

    public UnsplashData(){

    }

    public UnsplashData(String id, int width, int height, String createdAt, String color, String urlFull, String urlRegular, String categoryID, String categoryTitle, String username){
        this.id = id;
        this.width = width;
        this.height = height;
        this.createdAt = createdAt;
        this.color = color;
        this.urlFull = urlFull;
        this.urlRegular = urlRegular;
        this.categoryID = categoryID;
        this.categoryTitle = categoryTitle;
        this.username = username;
    }
public UnsplashData(String id)
{
    this.id=id;
}
    public UnsplashData(String id, int width, int height, String createdAt, String color, String urlFull, String urlRegular, String username){
        this.id = id;
        this.width = width;
        this.height = height;
        this.createdAt = createdAt;
        this.color = color;
        this.urlFull = urlFull;
        this.urlRegular = urlRegular;
        this.username = username;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }

    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }
    public String getCreatedAt(){
        return createdAt;
    }

    public void setColor(String color){
        this.color = color;
    }
    public String getColor(){
        return color;
    }

    public void setUrlFull(String urlFull){
        this.urlFull = urlFull;
    }
    public String getUrlFull(){
        return urlFull;
    }

    public void setUrlRegular(String urlRegular){
        this.urlRegular = urlRegular;
    }
    public String getUrlRegular(){
        return urlRegular;
    }

    public void setCategoryID(String categoryID){
        this.categoryID = categoryID;
    }
    public String getCategoryID(){
        return categoryID;
    }

    public void setCategoryTitle(String categoryTitle){
        this.categoryTitle = categoryTitle;
    }
    public String getCategoryTitle(){
        return categoryTitle;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }

    public void setWidth(int width){
        this.width = width;
    }
    public int getWidth(){
        return width;
    }

    public void setHeight(int height){
        this.height = height;
    }
    public int getHeight(){
        return height;
    }
}
