package com.wdfeww.docgl.data.remote;




import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {

    @POST("auth/login")
    Call<ResponseBody> userLogin(@Body RequestBody params);
}