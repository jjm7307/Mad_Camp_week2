package com.example.mad_camp_week2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_camp_week2.R;
import com.example.mad_camp_week2.Retrofit.IMyService;
import com.example.mad_camp_week2.Retrofit.RetrofitClient;
import com.example.mad_camp_week2.models.ModelContacts;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static android.view.View.VISIBLE;

public class RecyclerViewAdapterContact extends RecyclerView.Adapter<RecyclerViewAdapterContact.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<ModelContacts> mListContacts;
    private String myfacebook_id = "1263435600511937";

    //Connect to DB
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    /*@Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }*/

    public RecyclerViewAdapterContact(Context context, List<ModelContacts> listContacts) {
        mListContacts = listContacts;
        mContext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Connecting server Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.items_contacts, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView contact_name, contact_number;
        ImageView contact_profile;
        ImageView heart;

        contact_name = holder.contact_name;
        contact_number = holder.contact_number;
        contact_profile = holder.contact_profile;
        heart = holder.heart;

        contact_profile.setImageResource(R.drawable.icon_fb_profile);
        contact_name.setText(mListContacts.get(position).getName());
        contact_number.setText(mListContacts.get(position).getNumber());
        if(mListContacts.get(position).getLikeU()){
            heart.setVisibility(View.VISIBLE);
        }
        else{
            heart.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mListContacts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView contact_name, contact_number;
        ImageView contact_call, contact_profile;
        FrameLayout like_U;
        ImageView heart;


        public ViewHolder(View itemView) {
            super(itemView);

            contact_profile = itemView.findViewById(R.id.profile);
            contact_name = itemView.findViewById(R.id.contact_name);
            contact_number = itemView.findViewById(R.id.contact_number);
            contact_call = itemView.findViewById(R.id.calling);
            like_U = itemView.findViewById(R.id.like_U);
            heart = itemView.findViewById(R.id.heart);

            contact_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+mListContacts.get(position).getNumber()));
                        mContext.startActivity(intent);
                    }
                }
            });

            like_U.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        if(heart.getVisibility()== VISIBLE){
                            sublikeU(myfacebook_id,mListContacts.get(position).getFacebook_id());
                            mListContacts.get(position).setLikeU(false);
                            heart.setVisibility(View.INVISIBLE);
                        }
                        else{
                            addlikeU(myfacebook_id,mListContacts.get(position).getFacebook_id());
                            mListContacts.get(position).setLikeU(true);
                            heart.setVisibility(VISIBLE);
                        }
                    }
                }
            });

            contact_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {

                    }
                }
            });
        }
    }
    private void getlikeU(String id) {
        compositeDisposable.add(iMyService.getlikeU(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(mContext, ""+response, Toast.LENGTH_SHORT).show();

                    }
                }));
    }
    private void setlikeU(String id, String likeU) {
        compositeDisposable.add(iMyService.setlikeU(id,likeU)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(mContext, ""+response, Toast.LENGTH_SHORT).show();
                    }
                }));
    }
    private void addlikeU(String id, final String id_U) {
        compositeDisposable.add(iMyService.getlikeU(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(mContext, ""+response, Toast.LENGTH_SHORT).show();
                        setlikeU(myfacebook_id,response.replace("\"", "")+id_U+":");
                    }
                }));
    }
    private void sublikeU(String id, final String id_U) {
        compositeDisposable.add(iMyService.getlikeU(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(mContext, ""+response, Toast.LENGTH_SHORT).show();
                        setlikeU(myfacebook_id,response.replace("\"", "").replace(id_U+":", ""));
                    }
                }));
    }
}