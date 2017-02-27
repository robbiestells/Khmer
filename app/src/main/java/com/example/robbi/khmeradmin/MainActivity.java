package com.example.robbi.khmeradmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Adapters.ResponseAdapter;
import models.Response;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    MobileServiceClient mClient;
    MobileServiceTable<Response> mResponseTable;
    ResponseAdapter mAdapter;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        try {
            mClient = new MobileServiceClient(
                    "http://khmer.azurewebsites.net/", // Replace with the Site URL
                    this);


            mResponseTable = mClient.getTable(Response.class);
            // MobileServiceTable<ToDoItem> mToDoTable = mClient.getTable("ToDoItemBackup", ToDoItem.class);


            mAdapter = new ResponseAdapter(this, R.layout.response_row);

            ListView responseListView = (ListView) findViewById(R.id.responseList);
            responseListView.setAdapter(mAdapter);

            ResponseAsync async = new ResponseAsync();
            async.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }


    }

    private class ResponseAsync extends AsyncTask<Void, Void, Void> {
        List<Response> results = null;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
            results = mResponseTable.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (MobileServiceException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.clear();
            Collections.reverse(results);
            for (Response item : results) {
                mAdapter.add(item);
            }
            progressDialog.dismiss();
        }
    }

}