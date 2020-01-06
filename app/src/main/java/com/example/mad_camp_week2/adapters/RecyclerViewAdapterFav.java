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
import com.example.mad_camp_week2.models.ModelFavs;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static android.view.View.VISIBLE;

public class RecyclerViewAdapterFav extends RecyclerView.Adapter<RecyclerViewAdapterFav.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<ModelFavs> mListFavs;
    private String myfacebook_id = "1263435600511937";

    //Connect to DB
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    public RecyclerViewAdapterFav(Context context, List<ModelFavs> listFavs) {
        mListFavs = listFavs;
        mContext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Connecting server Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.items_favs, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView name,gender,birthday;

        name = holder.name;
        gender = holder.gender;
        birthday = holder.birthday;

        name.setText(mListFavs.get(position).getName());
        gender.setText(mListFavs.get(position).getGender());
        birthday.setText(mListFavs.get(position).getBirthday());

    }

    @Override
    public int getItemCount() {
        return mListFavs.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, gender, birthday;


        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            gender = itemView.findViewById(R.id.gender);
            birthday = itemView.findViewById(R.id.birthday);

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        //Coding action
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