package com.example.pasquale.storytellingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class SequenceGameActivity extends AppCompatActivity {

    private ArrayList<Vignetta> vignette;
    private ArrayList<Vignetta> stirred;
    public static int NOVIGNETTE=9;
    int tipo;
    private String idUtente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        vignette=getIntent().getParcelableArrayListExtra("arrayVignette");
        stirred=new ArrayList<>();
        tipo=getIntent().getIntExtra("tipo",NOVIGNETTE);

        if(tipo==NOVIGNETTE)
            finish();

        SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
        idUtente=settings.getString("idUtente",null);

        //processo casualità
        ArrayList<Integer> list = getRandomNonRepeatingIntegers(vignette.size(), 0, vignette.size());
        for(int i=0;i<vignette.size();i++){
            stirred.add(vignette.get(list.get(i)));
        }

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview);
        SequenceViewAdapter myAdapter = new SequenceViewAdapter(this,stirred,idUtente);
        myrv.setLayoutManager(new GridLayoutManager(this,2));
        myrv.setAdapter(myAdapter);
    }

    public static int getRandomInt(int min, int max) {
        Random random = new Random();

        return random.nextInt((max - min) ) + min;
    }

    public static ArrayList<Integer> getRandomNonRepeatingIntegers(int size, int min,
                                                                   int max) {
        ArrayList<Integer> numbers = new ArrayList<Integer>();

        while (numbers.size() < size) {
            int random = getRandomInt(min, max);

            if (!numbers.contains(random)) {
                numbers.add(random);
            }
        }

        return numbers;
    }
}
