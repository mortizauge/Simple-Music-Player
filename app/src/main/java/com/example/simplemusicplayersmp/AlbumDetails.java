package com.example.simplemusicplayersmp;

import static com.example.simplemusicplayersmp.MainActivity.musicFiles_MOA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumDetails extends AppCompatActivity {
    RecyclerView recyclerView_MOA;
    ImageView albumPhoto_MOA;
    TextView album_MOA, artist_MOA;
    String albumName_MOA, artistName_MOA;
    ArrayList<MusicFiles> albumSongs_MOA = new ArrayList<>();
    AlbumDetailsAdapter albumDetailsAdapter_MOA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        recyclerView_MOA = findViewById(R.id.recycler_view);
        albumPhoto_MOA = findViewById(R.id.albumPhoto);
        artistName_MOA = getIntent().getStringExtra("artistName");
        albumName_MOA = getIntent().getStringExtra("albumName");
        album_MOA = findViewById(R.id.albumName);
        artist_MOA = findViewById(R.id.artistName);
        if (artistName_MOA != null && albumName_MOA != null) {
            album_MOA.setText(albumName_MOA);
            artist_MOA.setText(artistName_MOA);
        }
        else
        {
            album_MOA.setText("Unknown Album");
            artist_MOA.setText("Unknown Artist");
        }

        int j = 0;
        for (int i = 0; i < musicFiles_MOA.size(); i++) {
            if (albumName_MOA.equals(musicFiles_MOA.get(i).getAlbum_MOA())) {
                albumSongs_MOA.add(j, musicFiles_MOA.get(i));
                j++;
            }
        }
        byte[] image;
        try {
            image = getAlbumArt(albumSongs_MOA.get(0).getPath_MOA());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (image != null) {
            Glide.with(this).asBitmap()
                    .load(image)
                    .into(albumPhoto_MOA);
        }
        else
        {
            Glide.with(this)
                    .load(R.drawable.music)
                    .into(albumPhoto_MOA);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumSongs_MOA.size() < 1)) {
            albumDetailsAdapter_MOA = new AlbumDetailsAdapter(this, albumSongs_MOA);
            recyclerView_MOA.setAdapter(albumDetailsAdapter_MOA);
            recyclerView_MOA.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
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