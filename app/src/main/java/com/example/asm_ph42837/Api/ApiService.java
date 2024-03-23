package com.example.asm_ph42837.Api;

import com.example.asm_ph42837.Modal.SinhVien;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    String DOMAIN = "http://192.168.0.125:3000/api/";

    ApiService apiService  = new Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);
    @GET("students")
    Call<List<SinhVien>> getData();
    @POST("students/add")
    Call<SinhVien> addStudent(@Body SinhVien sinhVien);


    @DELETE("students/delete/{id}")
    Call<SinhVien> deleteStudent(@Path("id") String idStudent);
    @DELETE("students/update/{id}")
    Call<SinhVien> updateStudent(@Path("id") String idStudent,
                                 @Body SinhVien sinhVien);


}
