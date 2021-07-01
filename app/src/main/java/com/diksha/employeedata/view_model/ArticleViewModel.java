package com.diksha.employeedata.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.diksha.employeedata.ModelClass.EmployeeModel;
import com.diksha.employeedata.repository.ArticleRepository;


public class ArticleViewModel extends AndroidViewModel {

    private ArticleRepository articleRepository;
    private LiveData<EmployeeModel> articleResponseLiveData;

    public ArticleViewModel(@NonNull Application application) {
        super(application);

        articleRepository = new ArticleRepository();
        this.articleResponseLiveData = articleRepository.getEmployeeModel();
    }

    public LiveData<EmployeeModel> getArticleResponseLiveData() {
        return articleResponseLiveData;
    }
}
