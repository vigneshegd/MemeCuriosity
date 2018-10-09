package com.swcuriosity.memes.viewmodel;

import java.io.Serializable;

/**
 * Created by vigneswaran_467at17 on 06-11-2017.
 */

public class ImageUploadInfo implements Serializable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String isExisting;

    public String getIsExisting() {
        return isExisting;
    }

    public void setIsExisting(String isExisting) {
        this.isExisting = isExisting;
    }

    public String id;
    public String imageName;

    public String imageURL;

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String name, String url) {

        this.imageName = name;
        this.imageURL= url;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

}