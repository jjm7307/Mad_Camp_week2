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
import android.widget.Toast;

import com.example.mad_camp_week2.R;
import com.example.mad_camp_week2.Retrofit.IMyService;
import com.example.mad_camp_week2.Retrofit.RetrofitClient;
import com.example.mad_camp_week2.adapters.RecyclerViewAdapterFav;
import com.example.mad_camp_week2.models.ModelFavs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

// db에서 받아온 값이 5개라고 가정
public class FragmentFav2 extends Fragment {
    private RecyclerView myrecyclerview;
    private RecyclerViewAdapterFav adapter;
    private View v;
    private List<ModelFavs> fav_list = new ArrayList<>();
    private FloatingActionButton backBtn;

    private String myfacebook_id = "1263435600511937";

    //Connect to DB
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    public FragmentFav2(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_favs2, container, false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.card_recyclerview);
        backBtn = (FloatingActionButton)v.findViewById(R.id.btn_fragmentB_back); // 뒤로가기 버튼 누르면 다시 프래그먼트A로

        //Connecting server Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        myrecyclerview.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapterFav(getContext(),fav_list);
        myrecyclerview.setAdapter(adapter);
        if(fav_list.size() == 0){
            downloadFavs();
        }
        return v;
    }
    private void downloadFavs() {
        fav_list.clear();
        wholoveme(myfacebook_id);
        adapter.notifyDataSetChanged();
    }
    private void wholoveme(final String id) {
        compositeDisposable.add(iMyService.readcontact(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        //Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        String friends_list = response.replace("\"", "");
                        String[] friends = friends_list.split(",");
                        for (String friend : response.replace("\"", "").split(",")){
                            loveme(id, friend);
                        }
                    }
                }));
    }
    private void loveme(final String id, final String friend) {
        compositeDisposable.add(iMyService.getlikeU(friend)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        //Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        if(response.contains(id)){
                            getAge(friend);
                        }
                    }
                }));
    }
    private void getAge(final String id){
        compositeDisposable.add(iMyService.getAge(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        String Arr_birth[] = response.replace("\"", "").split("/");
                        savethem(id, Integer.parseInt(Arr_birth[2].trim()));
                    }
                }));
    }
    private void savethem(final String id, final Integer age) {
        compositeDisposable.add(iMyService.readcontactnum(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        //Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObj = new JSONObject(response);
                        fav_list.add(new ModelFavs(
                                (String) jsonObj.get("name"),
                                (String) jsonObj.get("gender"),
                                (String) jsonObj.get("birthday"),
                                age));
                        adapter.notifyDataSetChanged();
                    }
                }));
    }
}