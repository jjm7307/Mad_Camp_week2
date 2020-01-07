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

public class RecyclerViewAdapterFav extends RecyclerView.Adapter<RecyclerViewAdapterFav.ImgViewHolder> {
    View v;
    private Context mContext;
    private List<ModelFavs> mData; // 어댑터가 hold할 사람에 대한 정보를 저장할 객체
    private List<CardView> lstCardView = new ArrayList<>(); // 이벤트 처리를 위해 전역으로 저장해 놓음
    private List<CardView> lstCardViewFront= new ArrayList<>();
    private List<CardView> lstCardViewBack= new ArrayList<>();

    public RecyclerViewAdapterFav(Context mContext, List<ModelFavs> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterFav.ImgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(mContext).inflate(R.layout.items_favs, parent, false);
        ImgViewHolder vHolder = new ImgViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ImgViewHolder holder, final int position) {
        holder.img_description.setText(mData.get(position).getRandomDescription()); // 해당 Person객체가 가지고 있는 정보를 가지고 해당 필드중 랜덤하게 하나 출력해주기 위한 문자열을 반환
        // 원래는 인터넷 상의 url로 모자이크 처리해서 셋팅해줘야 함
        //Uri tmpUri = mData.get(position).getUri(); // url으로 변경해야 함
        holder.img_thumbnail.setImageURI(mData.get(position).getUri()); // 이미지뷰를 세팅하기 위한 uri를 받아옴
        // -------> 원래는 피카소 써서 인터넷상의 url을 가지고 이미지에 처리를 해서 보여줘야함
        lstCardView.add(holder.cardView);
        lstCardViewFront.add(holder.cardViewFront);
        lstCardViewBack.add(holder.cardViewBack);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            // 카드가 클릭되면 카드 앞뒤의 visible체크
            @Override
            public void onClick(View v) {
                filpCard(position);
//                Intent intent = new Intent(mContext, ImageActivity.class);
//
//                // passing data to the image activity
//                intent.putExtra("Title", mData.get(position).getTitle());
//                intent.putExtra("Description", mData.get(position).getDescription());
//                intent.putExtra("Thumbnail", mData.get(position).getUri().toString());
//
//                //start at activity
//                mContext.startActivity(intent);

            }
        });
        holder.cardView.getFocusable();
        // Set click Listener

    }
    // 현재 카드 뒤집음
    public void filpCard(int position){

        // 카드뷰가 클릭되었을 때 만약 뒷면이 보이고 있는 상태이면 --> 뒷면 안보이게 하고 앞면 보이게
        if(lstCardView.get(position).getVisibility() == View.VISIBLE){
            lstCardViewBack.get(position).setVisibility(View.INVISIBLE);
            lstCardViewFront.get(position).setVisibility(View.VISIBLE);
        }
        // 카드뷰가 클릭되었을 때 만약 앞면이 보이고 있는 상태이면 --> 이미 힌트확인했으므로 토스트 띄움
        else if(lstCardViewFront.get(position).getVisibility() == View.VISIBLE){
            Toast.makeText(v.getContext(),"욕심 부리지마",Toast.LENGTH_LONG).show();
        }
        else{
            //do nothing
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ImgViewHolder extends RecyclerView.ViewHolder {
        private TextView img_description;
        private ImageView img_thumbnail;
        public static CardView cardView,cardViewFront,cardViewBack;

        public ImgViewHolder(@NonNull View itemView) {
            super(itemView);

            img_description = (TextView) itemView.findViewById(R.id.card_description_id);
            img_thumbnail = (ImageView) itemView.findViewById((R.id.card_img_id));
            cardView = (CardView) itemView.findViewById(R.id.cardview_id); // 클릭이벤트를 받을 가장 바깥의 cardview
            cardViewFront = (CardView) itemView.findViewById(R.id.cardview_front); // 정보를 가지고 내용을 리셋시켜야 하므로 어댑터가 필요
            cardViewBack = (CardView) itemView.findViewById(R.id.cardview_back); // 어댑터 필요없음
        }
    }
}
//어댑터에 데이터 셋팅해서 확인하기
//public class RecyclerViewAdapterFav extends RecyclerView.Adapter<RecyclerViewAdapterFav.ViewHolder> {
//
//    private Context mContext;
//    private LayoutInflater inflater;
//    private List<ModelFavs> mListFavs;
//    private String myfacebook_id = "1263435600511937";
//
//    //Connect to DB
//    CompositeDisposable compositeDisposable = new CompositeDisposable();
//    IMyService iMyService;
//
//    public RecyclerViewAdapterFav(Context context, List<ModelFavs> listFavs) {
//        mListFavs = listFavs;
//        mContext = context;
//    }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        //Connecting server Init Service
//        Retrofit retrofitClient = RetrofitClient.getInstance();
//        iMyService = retrofitClient.create(IMyService.class);
//
//        inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(R.layout.items_favs, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//        TextView name,gender,birthday;
//
//        name = holder.name;
//        gender = holder.gender;
//        birthday = holder.birthday;
//
//        name.setText(mListFavs.get(position).getName());
//        gender.setText(mListFavs.get(position).getGender());
//        birthday.setText(mListFavs.get(position).getBirthday());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mListFavs.size();
//    }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        TextView name, gender, birthday;
//
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            name = itemView.findViewById(R.id.name);
//            gender = itemView.findViewById(R.id.gender);
//            birthday = itemView.findViewById(R.id.birthday);
//
//            name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if(position != RecyclerView.NO_POSITION) {
//                        //Coding action
//                    }
//                }
//            });
//        }
//    }
//    private void getlikeU(String id) {
//        compositeDisposable.add(iMyService.getlikeU(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        Toast.makeText(mContext, ""+response, Toast.LENGTH_SHORT).show();
//
//                    }
//                }));
//    }
//    private void setlikeU(String id, String likeU) {
//        compositeDisposable.add(iMyService.setlikeU(id,likeU)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        Toast.makeText(mContext, ""+response, Toast.LENGTH_SHORT).show();
//                    }
//                }));
//    }
//    private void addlikeU(String id, final String id_U) {
//        compositeDisposable.add(iMyService.getlikeU(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        Toast.makeText(mContext, ""+response, Toast.LENGTH_SHORT).show();
//                        setlikeU(myfacebook_id,response.replace("\"", "")+id_U+":");
//                    }
//                }));
//    }
//    private void sublikeU(String id, final String id_U) {
//        compositeDisposable.add(iMyService.getlikeU(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        Toast.makeText(mContext, ""+response, Toast.LENGTH_SHORT).show();
//                        setlikeU(myfacebook_id,response.replace("\"", "").replace(id_U+":", ""));
//                    }
//                }));
//    }
//}

