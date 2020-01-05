package com.example.mad_camp_week2.fragments;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_camp_week2.R;
import com.example.mad_camp_week2.adapters.RecyclerViewAdapterGallery;
import com.example.mad_camp_week2.models.ImageCard;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FragmentGallery extends Fragment {
    View v;
    private Bundle bundle;
    private RecyclerView myrecyclerview;
    private RecyclerViewAdapterGallery recyclerAdapter;
    private List<ImageCard> lstImageCard = new ArrayList<>(); // 이미지카드를 저장할 배열
    private ArrayList<Uri> uris = new ArrayList<>();
    private Button push,pull;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();

    public FragmentGallery() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Data setting
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View setting
        v = inflater.inflate(R.layout.gallery_fragment, container, false); // 갤러리 뿌릴 프래그먼트를 인플레이트
        myrecyclerview = (RecyclerView) v.findViewById(R.id.gallery_recyclerview);
        push = (Button)v.findViewById(R.id.push_btn);
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택 액티비티 띄우고 서버에 업로드
                showSelect();
            }
        });
        pull = (Button)v.findViewById(R.id.pull_btn);
        pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 서버에서 마지막으로 가져온 부분부터 끝까지 가져옴 (싱크)
                setSync();
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
//            lstImg.add((ImageView)findViewById(R.id.img1));
//            lstImg.add((ImageView)findViewById(R.id.img2));
//            lstImg.add((ImageView)findViewById(R.id.img3));
//            lstImg.add((ImageView)findViewById(R.id.img4));
//            lstImg.add((ImageView)findViewById(R.id.img5));
//            lstImg.add((ImageView)findViewById(R.id.img6));
//            lstImg.add((ImageView)findViewById(R.id.img7));
//            lstImg.add((ImageView)findViewById(R.id.img8));
//            lstImg.add((ImageView)findViewById(R.id.img9));
//            lstImg.add((ImageView)findViewById(R.id.img10));
//            lstImg.add((ImageView)findViewById(R.id.img11));
//            lstImg.add((ImageView)findViewById(R.id.img12));
            ClipData clipData = data.getClipData();
            if (clipData != null) { // 이미지가 여러장 선택되었을 경우
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    uris.add(imageUri);
                    try {
                        InputStream is = getActivity().getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else { // 이미지가 한 장만 선택되었을 경우
                Uri imageUri = data.getData();
                uris.add(imageUri);
                try {
                    InputStream is = getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmaps.add(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            updateCardView(); // 카드뷰 객체 업데이트 --> uploadServer()로 바꾸기 // 포인터를 유지


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

    // 서버에서 마지막으로 가져온 부분부터 끝까지 가져와서 추가
    public void setSync(){

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
    // 서버에 선택된 파일들을 업로드
    public void uploadServer(){

    }
}