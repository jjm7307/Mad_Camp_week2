package com.example.mad_camp_week2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_camp_week2.ImageActivity;
import com.example.mad_camp_week2.R;
import com.example.mad_camp_week2.models.ImageCard;

import java.util.List;

public class RecyclerViewAdapterGallery extends RecyclerView.Adapter<RecyclerViewAdapterGallery.ImgViewHolder> {
    private Context mContext;
    private List<ImageCard> mData;

    public RecyclerViewAdapterGallery(Context mContext, List<ImageCard> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterGallery.ImgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.cardview_item_image, parent, false);
        ImgViewHolder vHolder = new ImgViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImgViewHolder holder, final int position) {

        holder.img_title.setText(mData.get(position).getTitle());
        holder.img_thumbnail.setImageURI(mData.get(position).getUri());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageActivity.class);

                // passing data to the image activity
                intent.putExtra("Title", mData.get(position).getTitle());
                intent.putExtra("Description", mData.get(position).getDescription());
                intent.putExtra("Thumbnail", mData.get(position).getUri().toString());

                //start at activity
                mContext.startActivity(intent);
            }
        });
        // Set click Listener

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ImgViewHolder extends RecyclerView.ViewHolder {
        private TextView img_title;
        private ImageView img_thumbnail;
        CardView cardView;

        public ImgViewHolder(@NonNull View itemView) {
            super(itemView);

            img_title = (TextView) itemView.findViewById(R.id.card_title_id);
            img_thumbnail = (ImageView) itemView.findViewById((R.id.card_img_id));
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }
}
