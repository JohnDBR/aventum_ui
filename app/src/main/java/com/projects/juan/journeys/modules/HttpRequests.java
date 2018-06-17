package com.projects.juan.journeys.modules;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.projects.juan.journeys.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by juan on 13/02/18.
 */

public class HttpRequests {

    public static void getRequest(final Context context, String token, String url, final String errConnection, final CallBack callBack){
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("token", "getRequest token -> " + token);
        client.addHeader("Authorization", token);
        client.get(context, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("response", new String(responseBody));
                callBack.sendResponse(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(responseBody != null){
                    Log.d("response", new String(responseBody));
                }
                Toast.makeText(context, errConnection, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void postRequest(final Context context, String token, String url, JSONObject params, final String errConnection, final CallBack callBack){
        AsyncHttpClient client = new AsyncHttpClient();
        if(token != null) client.addHeader("Authorization", token);
        StringEntity entity;
        try {
            entity = new StringEntity(params.toString());
            client.post(context, url, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    Log.d("response", new String(responseBody));
                    callBack.sendResponse(new String(responseBody));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if(responseBody != null){
                        Log.d("response", new String(responseBody));
                        Toast.makeText(context, errConnection, Toast.LENGTH_LONG).show();
                    }
                }

            });
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
    }

    public static void postMultipartRequest(final Context context, String token, String url, RequestParams params, final String errConnection, final CallBack callBack){
        AsyncHttpClient client = new AsyncHttpClient();
        if(token != null) client.addHeader("Authorization", token);
        client.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("response", new String(responseBody));
                callBack.sendResponse(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(responseBody != null){
                    Log.d("response", new String(responseBody));
                    Toast.makeText(context, errConnection, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public interface CallBack {

        void sendResponse(String response);
        void sendFailure(String response);
    }

}
