package com.example.mad_camp_week2.fragments;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_camp_week2.R;
import com.example.mad_camp_week2.Retrofit.IMyService;
import com.example.mad_camp_week2.Retrofit.RetrofitClient;
import com.example.mad_camp_week2.SplashActivity;
import com.example.mad_camp_week2.adapters.ContactsRvAdapter;
import com.example.mad_camp_week2.adapters.ViewPagerAdapter;
import com.example.mad_camp_week2.models.ModelContacts;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentContacts extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private ContactsRvAdapter adapter;
    private List<ModelContacts> contact_list = new ArrayList<>();
    private String friends_list="", friend_id="";
    private Button btn_download, btn_upload;

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
                update_view();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ContactsRvAdapter(getContext(),contact_list);
        recyclerView.setAdapter(adapter);
        return v;
    }

    public void update_view(){
        List<ModelContacts> list = new ArrayList<>();
        contact_list.add(new ModelContacts("jaemin","012-345-6789"));
        adapter.notifyDataSetChanged();
    }

    public void downloadContacts() {
        contact_list.clear();
        //friends_list = readcontact("1263435600511937");
        //contact_list.add(new ModelContacts(friends_list,"012-345-6789"));
        readcontact("1263435600511937");
        contact_list.add(new ModelContacts(friends_list,"012-345-6789"));
        adapter.notifyDataSetChanged();
    }
    private List<ModelContacts> uploadContacts(){
        List<ModelContacts> list = new ArrayList<>();
        Cursor cursor = null;
        cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        cursor.moveToFirst();
        do {
            list.add(new ModelContacts(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            ));
        } while (cursor.moveToNext());

        return list;
    }

    private void readcontact(String id) {
        compositeDisposable.add(iMyService.readcontact(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        friends_list = response.replace("\"", "");
                        String[] friends = friends_list.split(",");
                        for (String friend : friends){
                            readcontactnum(friend);
                        }
                    }
                }));
    }
    private void readcontactnum(String id) {
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
                        contact_list.add(new ModelContacts(name,number));
                        adapter.notifyDataSetChanged();
                    }
                }));
    }
}