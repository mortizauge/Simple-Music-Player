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

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyHolder>{
    private Context mContext_MOA;
    static ArrayList<MusicFiles> albumFiles_MOA;
    View view;

    public AlbumDetailsAdapter(Context mContext_MOA, ArrayList<MusicFiles> albumFiles_MOA) {
        this.mContext_MOA = mContext_MOA;
        this.albumFiles_MOA = albumFiles_MOA;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext_MOA).inflate( R.layout.music_items, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.album_name.setText( albumFiles_MOA.get( position ).getTitle_MOA());
        holder.artist_name.setText( albumFiles_MOA.get( position ).getArtist_MOA());
        byte[] image = new byte[0];
        try {
            image = getAlbumArt(albumFiles_MOA.get( position ).getPath_MOA());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            image = getAlbumArt( albumFiles_MOA.get( position ).getPath_MOA());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(image != null){
            Glide.with(mContext_MOA).asBitmap()
                    .load(image)
                    .into(holder.album_image);
        }
        else
        {
            Glide.with(mContext_MOA)
                    .load(R.drawable.music)
                    .into(holder.album_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext_MOA, PlayerActivity.class);
                intent.putExtra("sender", "albumDetails");
                intent.putExtra("position", position);
                mContext_MOA.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumFiles_MOA.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView album_image;
        TextView album_name, artist_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.music_img);
            album_name = itemView.findViewById(R.id.music_file_name);
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
