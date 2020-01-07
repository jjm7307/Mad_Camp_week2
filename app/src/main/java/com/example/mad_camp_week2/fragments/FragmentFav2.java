package com.example.mad_camp_week2.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mad_camp_week2.R;
import com.example.mad_camp_week2.adapters.RecyclerViewAdapterFav;
import com.example.mad_camp_week2.models.ModelFavs;

import java.util.ArrayList;
import java.util.List;

// db에서 받아온 값이 5개라고 가정
public class FragmentFav2 extends Fragment {
    private RecyclerView myrecyclerview;
    private RecyclerViewAdapterFav recyclerAdapter;
    private List<ModelFavs> lstImageCard = new ArrayList<>();
    private View v;
    private Button backBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstImageCard.add(new ModelFavs ("조재민",22,"10/13/1998"));
        lstImageCard.add(new ModelFavs ("조혜빈",20,"02/13/1998"));
        lstImageCard.add(new ModelFavs ("신재문",23,"07/13/1998"));
        lstImageCard.add(new ModelFavs ("김옥경",21,"12/13/1998"));
        lstImageCard.add(new ModelFavs ("김라면",19,"01/13/1998"));
        Log.d("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~",""+Integer.parseInt("01")); // "01" Parsing format check
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_favs2, container, false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.card_recyclerview);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerAdapter = new RecyclerViewAdapterFav(getContext(), lstImageCard);
        myrecyclerview.setAdapter(recyclerAdapter);

        backBtn = v.findViewById(R.id.btn_fragmentB_back); // 뒤로가기 버튼 누르면 다시 프래그먼트A로
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back 버튼 누르면 AFragment로 replace
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentFav fragmentFav = new FragmentFav();
                fragmentTransaction.replace(R.id.fragmentB, fragmentFav);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });


        return v;
    }
}