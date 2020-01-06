package com.example.mad_camp_week2.fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_camp_week2.MainActivity;
import com.example.mad_camp_week2.R;
import com.example.mad_camp_week2.RetrofitInterface;
import com.example.mad_camp_week2.adapters.RecyclerViewAdapterGallery;
import com.example.mad_camp_week2.models.ImageCard;
import com.example.mad_camp_week2.models.ImageResult;
import com.facebook.login.LoginResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentGallery extends Fragment {
    View v;
    private RecyclerView myrecyclerview;
    private RecyclerViewAdapterGallery recyclerAdapter;
    private List<ImageCard> lstImageCard = new ArrayList<>(); // 이미지카드를 저장할 배열
    private ArrayList<Uri> uris = new ArrayList<>(); // url들을 저장
    private ArrayList<Uri> tmpUris = new ArrayList<>(); // url들을 저장
    private FloatingActionButton push,pull;
    private ArrayList<String> urisString = new ArrayList<>(); // url을 String으로 변환
    private Retrofit retrofit; //
    private RetrofitInterface retrofitInterface; //
    private String BASE_URL="http://192.249.19.251:0180"; //
    private Gson gson = new Gson();

    public FragmentGallery() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Data setting
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View setting
        v = inflater.inflate(R.layout.gallery_fragment, container, false); // 갤러리 뿌릴 프래그먼트를 인플레이트
        myrecyclerview = (RecyclerView) v.findViewById(R.id.gallery_recyclerview);
        push = (FloatingActionButton) v.findViewById(R.id.push_btn);
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelect();  // 선택 액티비티 띄우고 서버에 업로드
            }
        });

        pull = (FloatingActionButton) v.findViewById(R.id.pull_btn);
        pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animation = ObjectAnimator.ofFloat(pull, "rotation",0f,360f);
                animation.setDuration(2000);
                animation.start();
                setSync();  // 서버에 있는 이미지 all 가져옴 (싱크)
            }
        });

        myrecyclerview.setLayoutManager((new GridLayoutManager(getContext(), 3)));
        recyclerAdapter = new RecyclerViewAdapterGallery(getContext(), lstImageCard);
        myrecyclerview.setAdapter(recyclerAdapter);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            tmpUris.clear(); // 선택될때마다 비움
            ClipData clipData = data.getClipData();
            if (clipData != null) { // 이미지가 여러장 선택되었을 경우
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    tmpUris.add(imageUri); // 현재 프래그먼트에 표현되고 있는 것의 끝에 선택된 것을 이어붙임
                }
            } else { // 이미지가 한 장만 선택되었을 경우
                Uri imageUri = data.getData();
                tmpUris.add(imageUri); // 현재 서버에 있는 uri들을 유지
            }
            uploadServer(); //선택된 결과를 서버에 업로드
        }
    }

    // 선택 액티비티를 띄우고 서버에 업로드
    public void showSelect(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, 1); //
    }

    // 서버에서 모든 이미지를 가져와서 프래그먼트에 띄움
    public void setSync(){
        // 선택 액티비티 띄워서 결과 uris에 저장한 후
        // 선택된 uri의 횟수만큼 반복 // count변수로 세어서 상태코드와 함께 검사
        HashMap<String,String> map = new HashMap<>();
        map.put("tag","appData"); // query의 내용이 될 것임

        Call<ArrayList<ImageResult>> call = retrofitInterface.executePull(map); // 해당 이메일과 패스워드를 가진 하나의 객체를 전송

        call.enqueue(new Callback<ArrayList<ImageResult>>() {
            @Override
            public void onResponse(Call<ArrayList<ImageResult>> call, Response<ArrayList<ImageResult>> response) {
                if (response.code() == 200) {
                    uris.clear(); // 원래 저장되어 있던 배열다비우고, 다시 모두 받음
                    ArrayList<ImageResult> tmpList = response.body();
                    for(int i=0;i<tmpList.size();i++){
                        uris.add(Uri.parse(tmpList.get(i).getUri()));
                    }
                        // 데이터를 받았으면 다시 프래그먼트에 갤러리들을 셋팅
                        updateCardView();
                    }
                else if( response.code() == 404){
                    Toast.makeText(getContext(),"Download Failed",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ImageResult>> call, Throwable t) {
                //Toast.makeText(MainActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void updateCardView(){
        ImageCard imageCard;
        ArrayList<ImageCard> imageCards = new ArrayList<>();
        lstImageCard.clear();
        for (int i = 0; i < uris.size(); i++) {
            imageCard = new ImageCard();
            imageCard.setUri(uris.get(i));
            imageCard.setTitle((i + 1) + "th Image");
            imageCard.setDescription("");
            imageCards.add(imageCard);
        }
        lstImageCard.addAll(imageCards);
        recyclerAdapter.notifyDataSetChanged();
    }

    int count ; // 업로드 한 갯수를 셀 변수
    // 서버에 선택된 파일들을 업로드
    public void uploadServer(){
        count = 0;
        // 선택된 것의 갯수만큼 업로드
        for(int i=0;i<tmpUris.size();i++) {
            HashMap<String, String> map = new HashMap<>();
            // 요청 객체 ( 입력값을 ) 보냄
            map.put("tag", "appData");
            map.put("uri", tmpUris.get(i).toString());
            Call<Void> call = retrofitInterface.executePush(map);

        // 해당 요청을 서버 프로그램의 req출력 큐에 넣음
         call.enqueue(new Callback<Void>() { // 처리되어 결과가 오면 콜백함수가 실행 // 등록은 서버로부터 돌려받을 값이 없기 때문에 Void임
            @Override
            // 서버로부터 처리는 잘된경우 // 클라이언트는 상태코드로 처리결과를 확인
            public void onResponse(Call<Void> call, Response<Void> response) { // 요청과 받은 값
                if(response.code() == 200){
                   // Toast.makeText(MainActivity.this,"Signed up successfully",Toast.LENGTH_LONG).show();
                }else if(response.code() == 400){
                  //  Toast.makeText(MainActivity.this,"Already registered",Toast.LENGTH_LONG).show();
                }
            }
            // 예기치 못한 오류
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        );

    }
        if(count == tmpUris.size())
            Toast.makeText(getContext(),"Uploaded successfully",Toast.LENGTH_LONG).show();
    }
}