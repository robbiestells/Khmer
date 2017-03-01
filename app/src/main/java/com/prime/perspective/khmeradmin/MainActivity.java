package com.prime.perspective.khmeradmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Adapters.ResponseAdapter;
import models.Response;

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