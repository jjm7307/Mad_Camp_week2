
package com.example.mad_camp_week2.fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_camp_week2.R;
import com.example.mad_camp_week2.Retrofit.IMyService;
import com.example.mad_camp_week2.Retrofit.RetrofitClient;
import com.example.mad_camp_week2.adapters.RecyclerViewAdapterContact;
import com.example.mad_camp_week2.adapters.RecyclerViewAdapterFav;
import com.example.mad_camp_week2.models.ModelContacts;
import com.example.mad_camp_week2.models.ModelFavs;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import org.json.JSONObject;


public class FragmentFav extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterFav adapter;
    private List<ModelFavs> fav_list = new ArrayList<>();
    private String friends_list="", friend_id="";
    private Button btn_download, btn_upload;

    private String myfacebook_id = "1263435600511937";

    //Connect to DB
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    public FragmentFav(){
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.frag_favs, container, false);
        btn_download = (Button)v.findViewById(R.id.btn_download);
        btn_upload = (Button)v.findViewById(R.id.btn_upload);
        recyclerView = v.findViewById(R.id.rv_favs);

        //Connecting server Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFavs();
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapterFav(getContext(),fav_list);
        recyclerView.setAdapter(adapter);
        return v;
    }

    public void downloadFavs() {
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
                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        if(response.contains(id)){
                            savethem(friend);
                        }
                    }
                }));
    }
    private void savethem(final String id) {
        compositeDisposable.add(iMyService.readcontactnum(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObj = new JSONObject(response);
                        fav_list.add(new ModelFavs(
                                (String) jsonObj.get("name"),
                                (String) jsonObj.get("gender"),
                                (String) jsonObj.get("birthday")));
                        adapter.notifyDataSetChanged();
                    }
                }));
    }
}