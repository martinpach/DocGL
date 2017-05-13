package com.wdfeww.docgl.data.remote;

import com.wdfeww.docgl.data.model.Appointment;
import com.wdfeww.docgl.data.model.Doctor;
import com.wdfeww.docgl.data.model.FreeDateToAppoitnment;
import com.wdfeww.docgl.data.model.User;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @POST("auth/login")
    Call<User> userLogin(@Body RequestBody params);

    @POST("auth/registration")
    Call<User> userRegister(@Body RequestBody params);

    @GET("patients/{id}/appointments")
    Call<List<Appointment>> getPatientAppointments(@Path("id") int id);

    @PUT("patients/{id}/profile/password")
    Call<ResponseBody> changePassword(@Path("id") int id, @Body RequestBody params);

    @DELETE("appointments/{id}")
    Call<ResponseBody> cancelAppointment(@Path("id") int id);

    @PUT("patients/{id}/profile")
    Call<ResponseBody> updateProfile(@Path("id") int id, @Body RequestBody params);

    @GET("doctors")
    Call<List<Doctor>> searchDoctor(@Query("name") String name, @Query("spec") String spec);

    @GET("patients/{id}/favourite")
    Call<List<Doctor>> getFavouriteDoctors(@Path("id") int id);

    @POST("appointments/interval/times")
    Call<List<FreeDateToAppoitnment>> getFreeDateToAppoitnment(@Body RequestBody params);
}

