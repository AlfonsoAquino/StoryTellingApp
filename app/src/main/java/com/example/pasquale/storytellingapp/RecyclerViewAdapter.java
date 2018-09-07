package com.example.pasquale.storytellingapp;

import android.content.Context;
import android.content.Intent;
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


import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Aws on 28/01/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Album> mData ;


    public RecyclerViewAdapter(Context mContext, List<Album> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_album,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String fileName = "41.jpg";
        String completePath = Environment.getExternalStorageDirectory()+"/"+Config.destAlbumPrev+"/"+fileName;

        File file = new File(completePath);
        Uri imageUri = Uri.fromFile(file);
        Log.d(TAG, "onBindViewHolder: ----------------------------------------- ci sono");
        if(file.exists()) {
            holder.album_title.setText(mData.get(position).getNome());
            Log.d(TAG, "onBindViewHolder: ----------------------------------------- entro"+mData.get(position).getNome());
            Glide.with(mContext).load(imageUri).into(holder.album_cover);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(mData.get(position).getTipo()==2) {
//                        Intent intent = new Intent(mContext, SequenceGameActivity.class);
//
//                        // passing data to the album activity
//                        intent.putExtra("tipo", mData.get(position).getTipo());
//                        Log.i("ajsoidaosdoaisjdoaisdjo",mData.get(position).getTipo()+"-----------<");
//                        intent.putParcelableArrayListExtra("arrayVignette", mData.get(position).getVignette());
//                        // start the activity
//                        mContext.startActivity(intent);
//
//                    }else {
                        Intent intent = new Intent(mContext, StoryGameActivity.class);

                        // passing data to the album activity
                        intent.putExtra("tipo", mData.get(position).getTipo());
                        Log.i("ajsoidaosdoaisjdoaisdjo", mData.get(position).getTipo() + "-----------<");
                        intent.putParcelableArrayListExtra("arrayVignette", mData.get(position).getVignette());
                        // start the activity
                        mContext.startActivity(intent);
//                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
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