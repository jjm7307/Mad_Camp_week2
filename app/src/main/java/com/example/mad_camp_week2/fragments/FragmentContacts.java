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
import com.example.mad_camp_week2.models.ModelContacts;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import org.json.JSONObject;

public class FragmentContacts extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterContact adapter;
    private List<ModelContacts> contact_list = new ArrayList<>();
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
    public FragmentContacts(){
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.frag_contacts, container, false);
        btn_download = (Button)v.findViewById(R.id.btn_download);
        btn_upload = (Button)v.findViewById(R.id.btn_upload);
        recyclerView = v.findViewById(R.id.rv_contacts);

        //Connecting server Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadContacts();
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadContacts();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapterContact(getContext(),contact_list);
        recyclerView.setAdapter(adapter);
        return v;
    }

    public void uploadContacts(){
        List<ModelContacts> list = new ArrayList<>();
        adapter.notifyDataSetChanged();
    }

    public void downloadContacts() {
        contact_list.clear();
        //friends_list = readcontact("1263435600511937");
        //contact_list.add(new ModelContacts(friends_list,"012-345-6789"));
        readcontact(myfacebook_id);
        adapter.notifyDataSetChanged();
    }

    private void readcontact(String id) {
        compositeDisposable.add(iMyService.readcontact(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        for (String friend : response.replace("\"", "").split(",")){
                            readcontactnum(friend);
                        }
                    }
                }));
    }
    private void readcontactnum(final String id) {
        compositeDisposable.add(iMyService.readcontactnum(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObj = new JSONObject(response);

                        String name = (String) jsonObj.get("name");
                        String number = (String) jsonObj.get("number");
                        IlikeU(myfacebook_id, id, name, number);
                        adapter.notifyDataSetChanged();
                    }
                }));
    }
    private void IlikeU(String id, final String id_U, final String name, final String number) {
        compositeDisposable.add(iMyService.getlikeU(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        contact_list.add(new ModelContacts(name,number,id_U,response.contains(id_U)));
                        adapter.notifyDataSetChanged();
                    }
                }));
    }

}