package com.example.pasquale.storytellingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class SequenceGameActivity extends AppCompatActivity {

    private ArrayList<Vignetta> vignette;
    public static int NOVIGNETTE=9;
    int tipo;
    private String idUtente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        vignette=getIntent().getParcelableArrayListExtra("arrayVignette");
        tipo=getIntent().getIntExtra("tipo",NOVIGNETTE);
        if(tipo==NOVIGNETTE)
            finish();
        SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
        idUtente=settings.getString("idUtente",null);
        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview);
        SequenceViewAdapter myAdapter = new SequenceViewAdapter(this,vignette,idUtente);
        myrv.setLayoutManager(new GridLayoutManager(this,3));
        myrv.setAdapter(myAdapter);
    }
}
