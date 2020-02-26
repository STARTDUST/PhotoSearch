package com.example.photosearch;

public class Model {
    private Integer id;
    private String title;
    private String img;

    public Model(Integer id, String title, String img) {
        this.id = id;
        this.title = title;
        this.img = img;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
