package com.example.pasquale.storytellingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoadingActivity extends AppCompatActivity {

    AnimationDrawable animationDrawable;
    private String idUtente;
    private int idAlbum;
    private String path;
    private ArrayList<Album> albums=new ArrayList<Album>();
    private ArrayList<Vignetta> vignette=new ArrayList<Vignetta>();
    //    private SQLiteHandler db;
    private final String TAG="Loading activity";
    final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        ImageView img = (ImageView) findViewById(R.id.img_loading);
        img.setBackgroundResource(R.drawable.animation_loading);

        animationDrawable = (AnimationDrawable) img.getBackground();
        animationDrawable.start();


        settings = getSharedPreferences(PREFS_NAME, 0);
        idUtente=settings.getString("idUtente",null);

        createDirIfNotExists(Config.destAlbumPrev);
        createDirIfNotExists(Config.destVignette);

        godSaveTheLoading();

    }

    private void godSaveTheLoading(){

        albumSetArray();


    }

    private void albumSetArray(){
        String cancel_req_tag = "ImageDownload";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.ALBUMS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONArray jObj = new JSONArray(response);
                    String error=jObj.getJSONObject(0).getString("error");
                    if (error.equals("false")) {

                        for(int i=0; i<jObj.length();i++){
                            JSONObject album= jObj.getJSONObject(i);

                            albums.add(new Album(Integer.parseInt(album.getString("id")),
                                    album.getString("nome"),
                                    album.getString("path"),
                                    Integer.parseInt(album.getString("tipo"))));
                        }
                        Log.i(TAG,"------------->numero di album: "+albums.size());

                        for(int i=0; i<albums.size();i++) {
                            path = albums.get(i).getPath();
                            String nomeAlbum=String.valueOf(albums.get(i).getId());
                            new ImageSaveTask(getApplicationContext()).execute(path,Config.destAlbumPrev,nomeAlbum);                        }
                        vignetteSetArray();
                    } else {

                        String errorMsg = jObj.getJSONObject(0).getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        //tornare alla main o da qualche altra parte
                        finalTask();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "download Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String,String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idUtente", idUtente);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

    private void vignetteSetArray(){
        String cancel_req_tag = "VignetteDownload";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.VIGNETTE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONArray jObj = new JSONArray(response);
                    String error = jObj.getJSONObject(0).getString("error");

                    if (error.equals("false")) {

                        for (int i = 0; i < jObj.length(); i++) {
                            JSONObject vignetta = jObj.getJSONObject(i);

                            vignette.add(new Vignetta(Integer.parseInt(vignetta.getString("idAlbum")),
                                    vignetta.getString("pathVignetta"),
                                    Integer.parseInt(vignetta.getString("numero"))));
                        }
                        Log.i(TAG, "------------->numero di vignette: " + vignette.size());

                        for(int i=0; i<vignette.size();i++) {
                            path = vignette.get(i).getPath();
                            String nomeAlbum=String.valueOf(vignette.get(i).getIdAlbum())+String.valueOf(vignette.get(i).getOrdine());
                            new ImageSaveTask(getApplicationContext()).execute(path,Config.destVignette,nomeAlbum);
                        }

                        finalTask();
                    } else {

                        String errorMsg = jObj.getJSONObject(0).getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "download Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String,String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idUtente", idUtente);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);

    }

    public class ImageSaveTask extends android.os.AsyncTask<String, Void, Void> {
        private Context context;

        public ImageSaveTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            if (params == null || params.length < 3) {
                throw new IllegalArgumentException("You should offer 2 params, the first for the image source url, and the other for the destination file save path");
            }

            String src = params[0];
            String dst =Environment.getExternalStorageDirectory()+"/"+params[1]+"/"+params[2]+".jpg";
            Log.i(TAG,"------------------------>"+dst);

            try {
                File file = Glide.with(context)
                        .load(src)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                //gestire il caso in cui il path dell'immagine non porta risultati
                File dstFile = new File(dst);
                if (!dstFile.exists()) {
                    boolean success = dstFile.createNewFile();
                    if (!success) {
                        return null;
                    }
                }

                InputStream in = null;
                OutputStream out = null;

                try {
                    in = new BufferedInputStream(new FileInputStream(file));
                    out = new BufferedOutputStream(new FileOutputStream(dst));

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.flush();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("ImageDownload","finish");
        }
    }
    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            file.mkdirs();
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }

    private void finalTask(){

        SQLiteHandler sql=new SQLiteHandler(getApplicationContext());
        for(Album a:albums){
            sql.addAlbum(a.getId(),a.getNome(),a.getPath(),a.getTipo());
        }

        for(Vignetta v: vignette){
            sql.addVignetta(v.getIdAlbum(),v.getPath(),v.getOrdine());
        }



        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}