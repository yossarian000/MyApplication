package com.example.peterjombik.myapplication;

public class ItemObject {
    private String content;
    private String imageResource;
    private String datavalue;

    public ItemObject(String content, String datavalue, String imageResource) {
        this.content = content;
        this.imageResource = imageResource;
        this.datavalue = datavalue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDataValue() {
        return datavalue;
    }

    public void setDataValue(String datavalue) {
        this.datavalue = datavalue;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

}
