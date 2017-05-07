package com.wdfeww.docgl.data.remote;


import com.wdfeww.docgl.data.model.Apointment;
import com.wdfeww.docgl.data.model.User;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    @POST("auth/login")
    Call<User> userLogin(@Body RequestBody params);

    @POST("auth/registration")
    Call<User> userRegister(@Body RequestBody params);

    @GET("patients/{id}/appointments")
    Call<List<Apointment>> getPatientAppointments(@Path("id") int id);
}