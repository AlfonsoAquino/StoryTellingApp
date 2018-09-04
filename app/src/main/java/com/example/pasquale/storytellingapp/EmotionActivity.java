package com.example.pasquale.storytellingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EmotionActivity extends AppCompatActivity {

    public List<Album> albums =new ArrayList<Album>();
    private SQLiteHandler db;
    public ArrayList<Album> emozioni;
    public ArrayList<Vignetta> vignette =new ArrayList<>();
    public ArrayList<Vignetta> vignetteAlbum =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);
        db=new SQLiteHandler(getApplicationContext());

        albums= new ArrayList<>();
        emozioni= new ArrayList<>();

        albums=db.getAlbumDetails();
        vignette=db.getVignettaDetails();
        Log.i("lakalkj------>","apsdadpoasdoapasdpapooipoipsodapd"+emozioni.size());
        for(Album a:albums){
            //seleziono solo gli album destinati allo storyTelling
            vignetteAlbum= new ArrayList<Vignetta>();
            if(a.getTipo()==1) {
                for(Vignetta b : vignette){
                    if (b.getIdAlbum()==a.getId())
                        vignetteAlbum.add(new Vignetta(b.getIdAlbum(), b.getPath(), b.getOrdine()));
                }
                Album album= new Album(a.getId(), a.getNome(), a.getPath(), a.getTipo());
                album.setVignette(vignetteAlbum);
                emozioni.add(album);
                Log.i("lakalkj------>","apsdadpoasdoapasdpasodapd"+emozioni.size());
            }
        }


        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,emozioni);
        myrv.setLayoutManager(new GridLayoutManager(this,3));
        myrv.setAdapter(myAdapter);
    }
}
