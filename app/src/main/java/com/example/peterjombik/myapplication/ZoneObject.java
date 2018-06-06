package com.example.peterjombik.myapplication;

public class ZoneObject {
    private String id;
    private String imageResource;
    private String zone;
//        private String datavalue;
//        private String topic;

//        private String type;

    public ZoneObject(String id, String zone, String imageResource) {
        this.id = id;
        this.imageResource = imageResource;
        //this.datavalue = datavalue;
        this.zone = zone;
    }

    public String getId() {
        return id;
    }

    public void setId(String content) {
        this.id = content;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }


}
