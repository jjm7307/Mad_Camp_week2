
package com.example.mad_camp_week2.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mad_camp_week2.R;
import com.example.mad_camp_week2.Retrofit.IMyService;
import com.example.mad_camp_week2.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class FragmentFav extends Fragment {
    private View v;
    private ImageView imageView;
    private Button hint, restart;
    private TextView count_number;
    private int count = 0; // 현재 눌러진 횟수를 저장하기 위한 전역변수
    private Boolean first = true;
    private int love_me_count = 0;
    private int loadedResource[] = {R.drawable.game_success, R.drawable.game_fail_1, R.drawable.game_fail_2, R.drawable.game_fail_3}; // success이 나와야지 hint버튼이 활성화
    // 만약 랜덤값이 0,1이 나오면 restart버튼만 활성화

    private String myfacebook_id = "1263435600511937";

    //Connect to DB
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_favs, container, false);

        //Connecting server Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        imageView = v.findViewById(R.id.egg_imageView);
        count_number = v.findViewById(R.id.count_number);
        hint = v.findViewById(R.id.hint_btn);
        restart = v.findViewById(R.id.restart_btn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                int delay = 150;
                ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "rotation",0f,10f);
                animation.setDuration(delay);
                animation.setStartDelay(delay*0);
                animation.start();

                ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView, "rotation",10f,-10f);
                animation1.setDuration(delay);
                animation1.setStartDelay(delay*1);
                animation1.start();
                checkCount();

                ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView, "rotation",-10f,7f);
                animation2.setDuration(delay);
                animation2.setStartDelay(delay*2);
                animation2.start();
                checkCount();

                ObjectAnimator animation3 = ObjectAnimator.ofFloat(imageView, "rotation",7f,0f);
                animation3.setDuration(delay);
                animation3.setStartDelay(delay*3);
                animation3.start();
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
        if(first){
            first=false;
            wholoveme(myfacebook_id);
        }
       count_number.setText(Integer.toString(love_me_count));
        return v;
    }

    public void checkCount() {
        // count가 0이면 두개의 버튼 invisible 5면 visible
        if (count == 0) { // 0이면 버튼 두개 숨기고, 초기화면 세팅
            hint.setVisibility(View.INVISIBLE);
            restart.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.tamago);
        } else if (count == 5) imageView.setImageResource(R.drawable.tamago1);
        else if (count == 10) imageView.setImageResource(R.drawable.tamago2);
        else if (count == 15) imageView.setImageResource(R.drawable.tamago3);
        else if (count == 20) {
            int index = getRandom();
            if (index != 0) { // 힌트를 볼수있는 아이템이 나오지 않은 경우
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
        } else {
            // do nothing
        }
    }
    private void wholoveme(final String id) {
        compositeDisposable.add(iMyService.readcontact(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        //Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        String friends_list = response.replace("\"", "");
                        String[] friends = friends_list.split(",");
                        for (String friend : response.replace("\"", "").split(",")){
                            loveme(id, friend);
                        }
                    }
                }));
    }
    private void loveme(final String id, final String friend) {
        compositeDisposable.add(iMyService.getlikeU(friend)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        if(response.contains(id)){
                            love_me_count++;
                            //Toast.makeText(getContext(), ""+Integer.toString(love_me_count), Toast.LENGTH_SHORT).show();
                            count_number.setText(Integer.toString(love_me_count));
                        }
                    }
                }));
    }

    // 미리 로딩된 이미지 중에서 랜덤하게 하나를 가져오기위한 index를 반환하는 함수
    public int getRandom() {
        return (int) (Math.random() * 4);
    }

}