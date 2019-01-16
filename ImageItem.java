package com.example.zhangwenqiang.rjks_final_pro;

public class ImageItem {
    private String image;
    private String title;

    public ImageItem(String image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}