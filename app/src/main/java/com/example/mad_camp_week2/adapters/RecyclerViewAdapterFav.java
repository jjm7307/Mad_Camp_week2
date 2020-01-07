package com.example.mad_camp_week2.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_camp_week2.R;
import com.example.mad_camp_week2.Retrofit.IMyService;
import com.example.mad_camp_week2.Retrofit.RetrofitClient;
import com.example.mad_camp_week2.models.ModelFavs;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static android.view.View.VISIBLE;

public class RecyclerViewAdapterFav extends RecyclerView.Adapter<RecyclerViewAdapterFav.ViewHolder> {
    private static Context mContext;
    private List<ModelFavs> mListFavs;
    private static Boolean can_open = true;

    public RecyclerViewAdapterFav(Context mContext, List<ModelFavs> listFavs) {
        can_open = true;
        this.mContext = mContext;
        mListFavs = listFavs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.items_favs, parent, false);
        ViewHolder vHolder = new ViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        TextView card_name, card_age, card_birth;
        ImageView card_profile;

        card_profile = holder.card_profile;
        card_name = holder.card_name;
        card_age = holder.card_age;
        card_birth = holder.card_birth;

        card_name.setText(mListFavs.get(position).getMaskName());
        card_age.setText(mListFavs.get(position).getAge());
        card_birth.setText(mListFavs.get(position).getSeason() + "에 태어남");
    }

    @Override
    public int getItemCount() {
        return mListFavs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView card_name, card_age, card_birth;
        ImageView card_profile;
        CardView cardView;
        LinearLayout cardViewFront;
        FrameLayout cardViewBack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card_name = (TextView) itemView.findViewById(R.id.card_name);
            card_age = (TextView) itemView.findViewById(R.id.card_age);
            card_birth = (TextView) itemView.findViewById(R.id.card_birth);
            card_profile = (ImageView) itemView.findViewById((R.id.card_img_id));
            cardView = (CardView) itemView.findViewById(R.id.cardview_id); // 클릭이벤트를 받을 가장 바깥의 cardview
            cardViewFront = (LinearLayout) itemView.findViewById(R.id.cardview_front); // 정보를 가지고 내용을 리셋시켜야 하므로 어댑터가 필요
            cardViewBack = (FrameLayout) itemView.findViewById(R.id.cardview_back); // 어댑터 필요없음
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if(can_open) {
                            can_open = false;
                            ObjectAnimator flipon = ObjectAnimator.ofFloat(cardView, "scaleX", 1.0f);
                            ObjectAnimator flipoff = ObjectAnimator.ofFloat(cardView, "scaleX", 0.0f);
                            ObjectAnimator flipoff2 = ObjectAnimator.ofFloat(cardViewBack, "scaleX", 0.0f);

                            flipon.setDuration(500);
                            flipon.setStartDelay(500);
                            flipoff.setDuration(500);
                            flipoff2.setDuration(1);
                            flipoff2.setStartDelay(500);

                            flipon.start();
                            flipoff2.start();
                            flipoff.start();
                        }
                        else{
                            String alert[] ={
                                    "에헤이 욕심부리지 맙시다",
                                    "궁금하면 게임 한판 더?",
                                    "한 명에 집중하는건 어때요?",
                                    "과욕은 금물",
                                    "우와 카드가 여러 장이에요? 인기 많다"};
                            Toast.makeText(mContext, ""+alert[(int)(Math.random()*5)], Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
    }
}