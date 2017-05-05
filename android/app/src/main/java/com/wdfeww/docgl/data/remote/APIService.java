package com.wdfeww.docgl.data.remote;


import com.wdfeww.docgl.data.model.User;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {

    @POST("auth/login")
    Call<User> userLogin(@Body RequestBody params);

    @POST("auth/registration")
    Call<ResponseBody> userRegister(@Body RequestBody params);
}