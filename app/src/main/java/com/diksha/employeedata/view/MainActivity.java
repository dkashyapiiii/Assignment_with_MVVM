package com.diksha.employeedata.view;

import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.diksha.employeedata.ModelClass.EmployeeModel;
import com.diksha.employeedata.ModelClass.Maindata;
import com.diksha.employeedata.adapter.EmployeeAdapter;
import com.diksha.employeedata.view_model.ArticleViewModel;
import com.diksha.employeedata.R;
import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView my_recycler_view;
    private ProgressBar progress_circular_movie_article;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private EmployeeAdapter employeeAdapter;
    private ArrayList<Maindata> articleArrayList = new ArrayList<>();
    ArticleViewModel articleViewModel;
    private Button jsonfileupload;
    private static final int FILE_SELECT_CODE = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();

        if(isNetworkConnected()){
            getdata();
        }else{
          //  getroomdata();
            Toast.makeText(this, "Please check your connection", LENGTH_SHORT).show();
        }

        jsonfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    private void initialization() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        my_recycler_view = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        jsonfileupload=findViewById(R.id.jsonfile);
        my_recycler_view.setLayoutManager(layoutManager);
        my_recycler_view.setHasFixedSize(true);

        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
    }


    private void getdata() {
        swipeRefreshLayout.setRefreshing(true);
        articleViewModel.getArticleResponseLiveData().observe(this, articleResponse -> {
            if (articleResponse != null) {
                List<Maindata> articles = articleResponse.getBanner1();
                articleArrayList.addAll(articles);
                employeeAdapter = new EmployeeAdapter(MainActivity.this, articleResponse.getBanner1(),"Live");
                my_recycler_view.setAdapter(employeeAdapter);
                employeeAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public String getRealPathFromURI( MainActivity context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path=getRealPathFromURI(MainActivity.this,uri);
                    String filename=path.substring(path.lastIndexOf("/")+1);
                    ReadFile(path);
                    Toast.makeText(this, filename, LENGTH_SHORT).show();

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void ReadFile(String path) {
    //    mSwipeRefreshLayout.setRefreshing(true);
        Gson gson = new Gson();
        String text = "";
        try {
            File yourFile = new File(path);
            InputStream inputStream = new FileInputStream(yourFile);
            StringBuilder stringBuilder = new StringBuilder();

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                while ((receiveString = bufferedReader.readLine()) != null){
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                text = stringBuilder.toString();
                Parsetest(text);
                Log.d("TAG",text);
            }
        } catch (FileNotFoundException e) {
            Log.e("file",e.getMessage());

        } catch (IOException e) {
            Log.e("file",e.getMessage());
        }
    }
    private void Parsetest(String text) {
        EmployeeModel employeeModel = new Gson().fromJson(text,EmployeeModel.class);
        employeeAdapter = new EmployeeAdapter(this, employeeModel.getBanner1(),"Live");
        my_recycler_view.setAdapter(employeeAdapter);
        employeeAdapter.notifyDataSetChanged();
 //       mSwipeRefreshLayout.setRefreshing(false);

    }
}
