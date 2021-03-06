package com.example.androidretrofit.Interface;

import com.example.androidretrofit.Model.LoginResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api
{
    @FormUrlEncoded
    @POST("createuser")
    Call<ResponseBody> createUser
            (
                    @Field("email") String email,
                    @Field("password") String password,
                    @Field("name") String name,
                    @Field("school") String school
            );

    @FormUrlEncoded
    @POST("userlogin")
    Call<LoginResponse> loginUser
            (
                    @Field("email") String email,
                    @Field("password") String password
            );
}
