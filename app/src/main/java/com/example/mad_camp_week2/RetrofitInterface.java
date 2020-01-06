package com.example.mad_camp_week2;

import com.example.mad_camp_week2.models.ImageResult;
import com.facebook.login.LoginResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/pull")
    Call<ArrayList<ImageResult>> executePull(@Body HashMap<String,String> map); // tag만 담을 것임
    @POST("/push")
    Call<Void> executePush(@Body HashMap<String, String> map); // tag와 uri 문자열을 담은 객체를 올릴거임

}
