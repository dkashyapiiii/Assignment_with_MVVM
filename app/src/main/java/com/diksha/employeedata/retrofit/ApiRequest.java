package com.diksha.employeedata.retrofit;

import com.diksha.employeedata.ModelClass.EmployeeModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiRequest {


@GET("getAllDetails")
Call<EmployeeModel> getEmployeeModel();

}
