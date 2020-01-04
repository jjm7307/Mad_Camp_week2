package com.example.mad_camp_week2.fragments;

import androidx.fragment.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_camp_week2.R;
import com.example.mad_camp_week2.adapters.ContactsRvAdapter;
import com.example.mad_camp_week2.models.ModelContacts;

import java.util.ArrayList;
import java.util.List;

public class FragmentContacts extends Fragment implements TextWatcher {

    private View v;
    private RecyclerView recyclerView;

    ContactsRvAdapter adapter;

    EditText editText_search;
    public FragmentContacts(){
    }

    ImageView imageView_cancle;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.frag_contacts, container, false);

        recyclerView = v.findViewById(R.id.rv_contacts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(layoutManager);

        editText_search = v.findViewById(R.id.eidtText_search);
        editText_search.addTextChangedListener(this);

        imageView_cancle = v.findViewById(R.id.edittext_cancle_button);
        imageView_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_search.setText("");
            }
        });

        adapter = new ContactsRvAdapter(getContext(),getContacts());
        recyclerView.setAdapter(adapter);

        return v;
    }


    private List<ModelContacts> getContacts() {

        List<ModelContacts> list = new ArrayList<>();
        Cursor cursor = null;
        cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        cursor.moveToFirst();


        do {
            list.add(new ModelContacts(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI))
            ));
        } while (cursor.moveToNext());
        return list;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        adapter.getFilter().filter(s);
        Log.v("filter_ start : ", " hi");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}