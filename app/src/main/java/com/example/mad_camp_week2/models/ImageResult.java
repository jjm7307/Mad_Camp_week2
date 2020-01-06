package com.example.mad_camp_week2.models;

import java.util.ArrayList;

// 이미지 객체 하나하나 결과로 받아올것임
public class ImageResult {
    private String tag; // "appData"
    private String uri; // Image's uri path

    public ImageResult() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
