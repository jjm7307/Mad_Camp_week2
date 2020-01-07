
package com.example.mad_camp_week2.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mad_camp_week2.R;

public class FragmentFav extends Fragment {
    private View v;
    private ImageView imageView;
    private Button hint,restart;
    private int count = 0; // 현재 눌러진 횟수를 저장하기 위한 전역변수
    private int loadedResource[] = {R.drawable.img1,R.drawable.img2,R.drawable.img3}; // img3이 나와야지 hint버튼이 활성화
    // 만약 랜덤값이 0,1이 나오면 restart버튼만 활성화

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_favs, container, false);

        imageView = v.findViewById(R.id.egg_imageView);
        hint = v.findViewById(R.id.hint_btn);
        restart = v.findViewById(R.id.restart_btn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                checkCount();
            }
        });

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hint 버튼 누르면 BFragment로 replace
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentFav2 fragmentFav2 = new FragmentFav2();
                fragmentTransaction.replace(R.id.fragmentA, fragmentFav2);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // restart버튼 누르면 count초기화하고 checkCount호출
                count = 0;
                checkCount();
            }
        });
        return v;
    }

    public void checkCount(){
        // count가 0이면 두개의 버튼 invisible 5면 visible
        if(count == 0){ // 0이면 버튼 두개 숨기고, 초기화면 세팅
            hint.setVisibility(View.INVISIBLE);
            restart.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.tamago);
        }
        else if(count == 5) imageView.setImageResource(R.drawable.tamago1);
        else if(count == 10) imageView.setImageResource(R.drawable.tamago2);
        else if(count == 15) imageView.setImageResource(R.drawable.tamago3);
        else if(count == 20) {
            int index = getRandom();
            if (index != 2) { // 힌트를 볼수있는 아이템이 나오지 않은 경우
                restart.setVisibility(View.VISIBLE);
                imageView.setImageResource(loadedResource[index]);
                // 랜덤하게 넣어놓고, 힌트 출력
                // 힌트를 출력할 수 있는 아이템이 나와야지(img3) 힌트버튼 출력
            } else { // 힌트를 얻을 수 있는 아이템이 나온경우 버튼 사용자가 액션 취할수 있게 버튼 두개 출력하기
                hint.setVisibility(View.VISIBLE);
                restart.setVisibility(View.VISIBLE);
                imageView.setImageResource(loadedResource[index]);
                // + 아이템 획득 ! 뾰롱 ~소리
            }
        }
        else{
            // do nothing
        }
    }

    // 미리 로딩된 이미지 중에서 랜덤하게 하나를 가져오기위한 index를 반환하는 함수
    public int getRandom(){
        return (int)(Math.random()*3);
    }

}

//
//public class FragmentFav extends Fragment {
//    private View v;
//    private RecyclerView recyclerView;
//    private RecyclerViewAdapterFav adapter;
//    private List<ModelFavs> fav_list = new ArrayList<>();
//    private String friends_list="", friend_id="";
//    private Button btn_download, btn_upload;
//
//    // Add Hyebin
//    private ImageView imageView;
//    private Button hint,restart;
//    private int count = 0; // 현재 눌러진 횟수를 저장하기 위한 전역변수
//    private int loadedResource[] = {R.drawable.img1,R.drawable.img2,R.drawable.img3}; // img3이 나와야지 hint버튼이 활성화
//    // 만약 랜덤값이 0,1이 나오면 restart버튼만 활성화
//    //
//
//    private String myfacebook_id = "1263435600511937";
//
//    //Connect to DB
//    CompositeDisposable compositeDisposable = new CompositeDisposable();
//    IMyService iMyService;
//
//    @Override
//    public void onStop() {
//        compositeDisposable.clear();
//        super.onStop();
//    }
//    public FragmentFav(){
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        v = inflater.inflate(R.layout.frag_favs, container, false);
//        btn_download = (Button)v.findViewById(R.id.btn_download);
//        btn_upload = (Button)v.findViewById(R.id.btn_upload);
//        recyclerView = v.findViewById(R.id.rv_favs);
//
//        //Connecting server Init Service
//        Retrofit retrofitClient = RetrofitClient.getInstance();
//        iMyService = retrofitClient.create(IMyService.class);
//
//        btn_download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                downloadFavs();
//            }
//        });
//        btn_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
//        recyclerView.setLayoutManager(layoutManager);
//
//        adapter = new RecyclerViewAdapterFav(getContext(),fav_list);
//        recyclerView.setAdapter(adapter);
//        return v;
//    }
//
//    public void downloadFavs() {
//        fav_list.clear();
//        wholoveme(myfacebook_id);
//        adapter.notifyDataSetChanged();
//    }
//
//    private void wholoveme(final String id) {
//        compositeDisposable.add(iMyService.readcontact(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
//                        String friends_list = response.replace("\"", "");
//                        String[] friends = friends_list.split(",");
//                        for (String friend : response.replace("\"", "").split(",")){
//                            loveme(id, friend);
//                        }
//                    }
//                }));
//    }
//    private void loveme(final String id, final String friend) {
//        compositeDisposable.add(iMyService.getlikeU(friend)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
//                        if(response.contains(id)){
//                            savethem(friend);
//                        }
//                    }
//                }));
//    }
//    private void savethem(final String id) {
//        compositeDisposable.add(iMyService.readcontactnum(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
//                        JSONObject jsonObj = new JSONObject(response);
//                        fav_list.add(new ModelFavs(
//                                (String) jsonObj.get("name"),
//                                (String) jsonObj.get("gender"),
//                                (String) jsonObj.get("birthday")));
//                        adapter.notifyDataSetChanged();
//                    }
//                }));
//    }
//}