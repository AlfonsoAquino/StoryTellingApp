package com.example.pasquale.storytellingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class StoryActivity extends AppCompatActivity {

    public List<Album> albums =new ArrayList<Album>();
    private SQLiteHandler db;
    public ArrayList<Album> storie;
    public ArrayList<Vignetta> vignette =new ArrayList<>();
    public ArrayList<Vignetta> vignetteAlbum =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        db=new SQLiteHandler(getApplicationContext());

        albums= new ArrayList<>();
        storie= new ArrayList<>();

        albums=db.getAlbumDetails();
        vignette=db.getVignettaDetails();

        for(Album a:albums){
            //seleziono solo gli album destinati allo storyTelling
            vignetteAlbum= new ArrayList<Vignetta>();
            if(a.getTipo()==0) {
                for(Vignetta b : vignette){
                    if (b.getIdAlbum()==a.getId())
                        vignetteAlbum.add(new Vignetta(b.getIdAlbum(), b.getPath(), b.getOrdine()));
                }
                Album album= new Album(a.getId(), a.getNome(), a.getPath(), a.getTipo());
                album.setVignette(vignetteAlbum);
                storie.add(album);
            }
        }


        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,storie);
        myrv.setLayoutManager(new GridLayoutManager(this,3));
        myrv.setAdapter(myAdapter);

    }


}
