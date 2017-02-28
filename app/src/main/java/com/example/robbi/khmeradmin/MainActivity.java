package com.example.robbi.khmeradmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableJsonQueryCallback;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import Adapters.ResponseAdapter;
import models.Response;

import static android.view.View.GONE;
import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

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
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://khmer.azurewebsites.net",
                    this);

            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            mResponseTable = mClient.getTable(Response.class);

            mAdapter = new ResponseAdapter(this, R.layout.response_row);
            ListView responseListView = (ListView) findViewById(R.id.responseList);
            responseListView.setAdapter(mAdapter);

            refreshItemsFromTable();

            //  ResponseAsync async = new ResponseAsync();
           //   async.execute();
        } catch (MalformedURLException e) {
            Log.v("Getting Table", "There was an error creating the Mobile Service. Verify the URL");

        }catch (Exception e){
            Log.v("Getting Table", "Error");
        }

    }

    private void refreshItemsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<Response> results = refreshItemsFromMobileServiceTable();

                    //Offline Sync
                    //final List<ToDoItem> results = refreshItemsFromMobileServiceTableSyncTable();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.clear();

                            for (Response item : results) {
                                mAdapter.add(item);
                            }
                        }
                    });
                } catch (final Exception e){
                    Log.v("Async Task", "Error");
                }

                return null;
            }
        };

        runAsyncTask(task);
    }

    private List<Response> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException {
        return mResponseTable.where().execute().get();
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }
}