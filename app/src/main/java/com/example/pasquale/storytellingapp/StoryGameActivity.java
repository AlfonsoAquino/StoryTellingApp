package com.example.pasquale.storytellingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class StoryGameActivity extends AppCompatActivity {

    private ArrayList<Vignetta> vignette;
    public ImageView imgV;
    public ImageView imgNext;
    public ImageView imgBack;
    int temp=0;
    int size;
    int tipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_game);

        // get array Vignette
        vignette=getIntent().getParcelableArrayListExtra("arrayVignette");
        tipo=Integer.parseInt(getIntent().getStringExtra("tipo"));
        imgV = (ImageView)findViewById(R.id.imgV);
        imgNext = (ImageView)findViewById(R.id.imgNext);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        //###### get first picture path ######
        Uri imageUri= checkUri(0);

        //###### Load picture with Glide ######
        Glide.with(this).load(imageUri).into(imgV);

        checkImgView();

        //###### The last two are the options ######
        size=vignette.size() - 2;

        //###### next image button ######
        imgNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                temp++;
                if(temp==size){
                    Intent intent = new Intent(getApplicationContext(),SelectionActivity.class);
                    ArrayList<Vignetta> scelte= new ArrayList<>();
                    scelte.add(vignette.get(size));
                    scelte.add(vignette.get(size+1));
                    intent.putParcelableArrayListExtra("scelte", scelte);
                    intent.putExtra("tipo",tipo);
                    startActivityForResult(intent,0);
                }
                else if(temp<size) {


                    Glide.with(getApplicationContext())
                            .load(checkUri(temp))
                            .into(imgV);
                    checkImgView();
                }
            }
        });

        //###### back image button ######
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temp>0) {
                    temp--;
                    Glide.with(getApplicationContext()).load(checkUri(temp)).into(imgV);
                    checkImgView();
                }
            }
        });

    }

    public Uri checkUri(int temp){

        String fileName = vignette.get(temp).getIdAlbum()+""+vignette.get(temp).getOrdine()+".jpg";
        String completePath = Environment.getExternalStorageDirectory()+"/"+Config.destVignette+"/"+fileName;

        File file = new File(completePath);
        Uri imageUri = Uri.fromFile(file);
        if(!file.exists()){
            Toast.makeText(getApplicationContext(),"immagine non trovata contatta l'assistenza",Toast.LENGTH_LONG).show();
            finish();
        }
        return imageUri;
    }

    public void checkImgView(){
        if(temp==0) {
            imgBack.setVisibility(View.INVISIBLE);
        }
        else if(temp==1){
            imgBack.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            temp=size-1;
        }
    }
}
