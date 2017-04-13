package com.example.robbi.khmeradmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.microsoft.windowsazure.mobileservices.table.query.Query;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
                    "http://khmer.azurewebsites.net/",
                    this);

            final OkHttpClientFactory client = new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                   long timeout = 3000;
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    OkHttpClient okHttpClient = new OkHttpClient();
                    okHttpClient.setConnectTimeout(timeout, unit);
                    okHttpClient.setReadTimeout(timeout, unit);
                    okHttpClient.setWriteTimeout(timeout, unit);
                    return okHttpClient;
                }
            };

            mClient.setAndroidHttpClientFactory(client);

            mResponseTable = mClient.getTable(Response.class);

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
                results = mResponseTable.orderBy("Date",QueryOrder.Descending).top(20).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (results != null) {
                mAdapter.clear();
                for (Response item : results) {
                    mAdapter.add(item);
                }
            }
            progressDialog.dismiss();
        }
    }
}