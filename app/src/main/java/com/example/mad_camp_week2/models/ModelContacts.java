package com.example.mad_camp_week2.models;

public class ModelContacts {


    private String name,number;
    private String profile_url, facebook_id;
    private Boolean likeU;

    public ModelContacts(String name, String number, String facebook_id, Boolean likeU,String profile_url) {
        this.name = name;
        this.number = number;
        this.profile_url = profile_url;
        this.facebook_id = facebook_id;
        this.likeU = likeU;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public Boolean getLikeU() {
        return likeU;
    }

    public void setLikeU(Boolean likeU) {
        this.likeU = likeU;
    }
}
