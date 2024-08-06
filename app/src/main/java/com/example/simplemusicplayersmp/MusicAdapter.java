package com.example.simplemusicplayersmp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>{

    private Context mContext_MOA;
    private ArrayList<MusicFiles> mFiles_MOA;

    MusicAdapter(Context mContext_MOA, ArrayList<MusicFiles> mFiles_MOA){
        this.mFiles_MOA = mFiles_MOA;
        this.mContext_MOA = mContext_MOA;
    }

    MusicAdapter(){

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext_MOA).inflate( R.layout.music_items, parent, false);
        return new MyViewHolder( view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.file_name.setText( mFiles_MOA.get( position ).getTitle_MOA());
        holder.artist_name.setText( mFiles_MOA.get( position ).getArtist_MOA());
        byte[] image = new byte[0];
        try {
            image = getAlbumArt( mFiles_MOA.get( position ).getPath_MOA());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(image != null){
            Glide.with(mContext_MOA).asBitmap()
                    .load(image)
                    .into(holder.album_art);
        }
        else
        {
            Glide.with(mContext_MOA)
                    .load(R.drawable.music)
                    .into(holder.album_art);
        }
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext_MOA, PlayerActivity.class);
                intent .putExtra("position", position);
                mContext_MOA.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mFiles_MOA.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView file_name, artist_name;
        ImageView album_art;
        public MyViewHolder(View view) {
            super(view);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            artist_name = itemView.findViewById(R.id.music_file_artist);
        }
    }

    private byte[] getAlbumArt(String uri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        byte[] art = null;
        try {
            retriever.setDataSource(uri);
            art = retriever.getEmbeddedPicture();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return art;
    }

}
