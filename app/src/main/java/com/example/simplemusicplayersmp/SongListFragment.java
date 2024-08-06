package com.example.simplemusicplayersmp;

import static com.example.simplemusicplayersmp.MainActivity.musicFiles_MOA;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SongListFragment extends Fragment {

    RecyclerView recyclerView_MOA;
    MusicAdapter musicAdapter_MOA;

    public SongListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        recyclerView_MOA = view.findViewById(R.id.recycler_view);
        recyclerView_MOA.setHasFixedSize(true);
        if(!(musicFiles_MOA.size() < 1)){
            musicAdapter_MOA = new MusicAdapter(getContext(), musicFiles_MOA);
            recyclerView_MOA.setAdapter(musicAdapter_MOA);
            recyclerView_MOA.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        return view;
    }
}