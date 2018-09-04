package com.example.pasquale.storytellingapp;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendStatistics extends AsyncTask<String, Integer,Void> {

    private final String TAG="StatisticsThread";
    private String idAlbum,idPaziente,numCorr,numErr;
    private Context context;

    public SendStatistics(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        idAlbum="0";
        idPaziente="0";
        numCorr="0";
        numErr="0";
    }

    @Override
    protected Void doInBackground(String... strings) {

        if (strings == null || strings.length < 4) {
            throw new IllegalArgumentException("You should offer 2 params, the first for the image source url, and the other for the destination file save path");
        } else {

            idPaziente=strings[0];
            idAlbum=strings[1];
            numCorr=strings[2];
            numErr=strings[3];


            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Config.STATISTICA, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Register Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        if (!error) {

                            Log.i(TAG, "------------>ok");
                        } else {

                            Log.i(TAG, "------------>erroreCaricamentoStatistiche");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting params to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("idPaziente", idPaziente);
                    params.put("idAlbum", idAlbum);
                    params.put("numCorrette", numCorr);
                    params.put("numSbagliate", numErr);

                    return params;
                }
            };
            // Adding request to request queue
            AppSingleton.getInstance(context).addToRequestQueue(strReq, "statistics");

            return null;
        }
    }
}