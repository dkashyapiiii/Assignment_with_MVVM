package com.diksha.employeedata.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.diksha.employeedata.ModelClass.EmployeeModel;
import com.diksha.employeedata.retrofit.ApiRequest;
import com.diksha.employeedata.retrofit.RetrofitRequest;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleRepository {
    private static final String TAG = ArticleRepository.class.getSimpleName();
    private ApiRequest apiRequest;

    public ArticleRepository() {
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
    }

    public LiveData<EmployeeModel> getEmployeeModel() {
        final MutableLiveData<EmployeeModel> data = new MutableLiveData<>();
        apiRequest.getEmployeeModel()
                .enqueue(new Callback<EmployeeModel>() {

                    @Override
                    public void onResponse(Call<EmployeeModel> call, Response<EmployeeModel> response) {
                        Log.d(TAG, "onResponse response:: " + response);

                        if (response.body() != null) {
                            data.setValue(response.body());
                                                  }
                    }

                    @Override
                    public void onFailure(Call<EmployeeModel> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

}
