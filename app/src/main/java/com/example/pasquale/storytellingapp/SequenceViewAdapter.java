package com.example.pasquale.storytellingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Aws on 28/01/2018.
 */

public class SequenceViewAdapter extends RecyclerView.Adapter<SequenceViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Vignetta> mData ;
    private int i;
    int last;
    private SQLiteHandler db;
    private String idUtente;


    public SequenceViewAdapter(Context mContext, List<Vignetta> mData, String idUtente) {
        this.mContext = mContext;
        this.mData = mData;
        i=1;
        last=mData.size();
        db=new SQLiteHandler(this.mContext);
        this.idUtente=idUtente;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_album,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String fileName = String.valueOf(mData.get(position).getIdAlbum())+""+mData.get(position).getOrdine()+".jpg";
        String completePath = Environment.getExternalStorageDirectory()+"/"+Config.destVignette+"/"+fileName;

        File file = new File(completePath);
        Uri imageUri = Uri.fromFile(file);
        if(file.exists()) {
            holder.album_title.setText("ordine: "+mData.get(position).getOrdine());
            Glide.with(mContext).load(imageUri).into(holder.album_cover);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.get(position).setOrdineProvvisorio(i++);
                    holder.itemView.setClickable(false);
                    Log.i("---asdadadasd---","ordine provvisorio "+mData.get(position).getOrdineProvvisorio());
                    if((i-1)==last){
                        boolean x=true;
                        for(int i=0;i<mData.size();i++){
                            int ordine=mData.get(position).getOrdine();
                            int ordineProv=mData.get(position).getOrdineProvvisorio();
                            if(ordine!=ordineProv)
                                x=false;
                            Log.i(ContentValues.TAG,"sdadjsodaisduaosduoaduoaduoiaduoiaudoiaudoaudo.............>"+ordine+","+ordineProv);
                        }
                        if (x){
                            db.addStatistica(idUtente,String.valueOf(mData.get(0).getIdAlbum()),"1","0");
                            Toast.makeText(mContext, "Corretto!!!!", Toast.LENGTH_LONG).show();
                        }else{
                            db.addStatistica(idUtente,String.valueOf(mData.get(0).getIdAlbum()),"0","1");
                            Toast.makeText(mContext, "Sbagliato!!!!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void remove(int position, View view){

    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView album_title;
        ImageView album_cover;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            album_title = (TextView) itemView.findViewById(R.id.album_title) ;
            album_cover = (ImageView) itemView.findViewById(R.id.album_cover);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);


        }
    }


}