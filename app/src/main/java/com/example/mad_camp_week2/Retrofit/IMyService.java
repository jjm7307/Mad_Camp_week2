package com.example.mad_camp_week2.Retrofit;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("id") String id,
                                    @Field("name") String name,
                                    @Field("birthday") String birthday,
                                    @Field("gender") String gender,
                                    @Field("friends_list") String friends_list,
                                    @Field("profile_url") String profile_url,
                                    @Field("likeU") String likeU,
                                    @Field("number") String number);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("id") String id);

    @POST("readcontact")
    @FormUrlEncoded
    Observable<String> readcontact(@Field("id") String id);

    @POST("readcontactnum")
    @FormUrlEncoded
    Observable<String> readcontactnum(@Field("id") String id);

    @POST("getlikeU")
    @FormUrlEncoded
    Observable<String> getlikeU(@Field("id") String id);

    @POST("setlikeU")
    @FormUrlEncoded
    Observable<String> setlikeU(@Field("id") String id,
                                @Field("likeU") String likeU);

    @POST("getAge")
    @FormUrlEncoded
    Observable<String> getAge(@Field("id") String id);

    @POST("registerNumber")
    @FormUrlEncoded
    Observable<String> registerNumber(@Field("id") String id,
                                @Field("number") String number);
}
