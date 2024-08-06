package com.example.simplemusicplayersmp;

import static com.example.simplemusicplayersmp.MainActivity.albums_MOA;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AlbumListFragment extends Fragment {

    RecyclerView recyclerView_MOA;
    AlbumAdapter albumAdapter_MOA;

    public AlbumListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_list, container, false);
        recyclerView_MOA = view.findViewById(R.id.recycler_view);
        recyclerView_MOA.setHasFixedSize(true);
        if(!(albums_MOA.size() < 1)){
            albumAdapter_MOA = new AlbumAdapter(getContext(), albums_MOA);
            recyclerView_MOA.setAdapter(albumAdapter_MOA);
            recyclerView_MOA.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        return view;
    }
}