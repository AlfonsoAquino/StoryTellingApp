package com.example.pasquale.storytellingapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CardView cardStory;
    private CardView cardEmotion;
    private CardView cardSequence;
    private ArrayList<Album> albums;
    private ArrayList<Vignetta> vignette;
    private ArrayList<Statistica> statistiche;
    private SQLiteHandler db;

    final String PREFS_NAME = "MyPrefsFile";
    private final int PERMISSION_REQUEST_CODE=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

//        if(checkPermission()) {
            db = new SQLiteHandler(getApplicationContext());
            vignette = new ArrayList<Vignetta>();
            albums = new ArrayList<Album>();
            statistiche = new ArrayList<Statistica>();
            cardStory = (CardView) findViewById(R.id.cardStory);
            cardSequence = (CardView) findViewById(R.id.cardSequence);
            cardEmotion = (CardView) findViewById(R.id.cardEmotion);


            //controllo per il primo accesso
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            //se non loggato allora loginActivity
            boolean log = settings.contains("idUtente");
            Log.i(".......><","logged?"+log);
            if (!log) {

                //the app is being launched for first time, do something
                Log.d("Comments", "First time");

                //first time task
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                //record the fact that the app has been started at least once
                settings.edit().putBoolean("my_first_time", false).commit();
//            } else {

                albums = db.getAlbumDetails();
                vignette = db.getVignettaDetails();
                statistiche = db.getStatisticsDetails();
                Log.i("asdadadadad....", "-------------->" + statistiche.size());
                if (isOnline() && statistiche.size() != 0) {
                    for (Statistica a : statistiche) {
                        Log.i("asdadadadad....", "-------------->" + a.toString());
                        new SendStatistics(getApplicationContext()).execute("" + a.getIdPaziente(), "" + a.getIdAlbum(), "" + a.getNumCorrette(), "" + a.getNumSbagliate());

                    }
                }
            }
//        creare bottone per scaricare nuovamente tutti gli album(quindi collegarsi direttamente alla pagina LoadingActivity)


            cardStory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkAlbum(0)) {
                        Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
                        intent.putParcelableArrayListExtra("albums", albums);
                        intent.putParcelableArrayListExtra("vignette", vignette);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "non ci sono album", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            cardEmotion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkAlbum(1)) {
                        Intent intent = new Intent(getApplicationContext(), EmotionActivity.class);
                        intent.putParcelableArrayListExtra("albums", albums);
                        intent.putParcelableArrayListExtra("vignette", vignette);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "non ci sono album", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            cardSequence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkAlbum(2)) {
                        Intent intent = new Intent(getApplicationContext(), SequenceActivity.class);
                        intent.putParcelableArrayListExtra("albums", albums);
                        intent.putParcelableArrayListExtra("vignette", vignette);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "non ci sono album", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//        }else{
//            requestPermission();
//            finish();
//
//        }
    }
    //this methods are semplified with editor if error check this
    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
    public Boolean checkAlbum(int count){
        int i=0;
        for(Album a:albums){
            if(a.getTipo()==count)
                i++;
        }
        return i != 0;
    }


    //for checking of permission
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    else {

                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }


}