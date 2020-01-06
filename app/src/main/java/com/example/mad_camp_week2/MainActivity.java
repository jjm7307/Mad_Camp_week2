package com.example.mad_camp_week2;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.mad_camp_week2.adapters.ViewPagerAdapter;
import com.example.mad_camp_week2.fragments.FragmentContacts;
import com.example.mad_camp_week2.fragments.FragmentGallery;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public FragmentGallery fragmentGallery = new FragmentGallery();

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentContacts(), "Contacts");
        adapter.addFragment(fragmentGallery, "Images");
        adapter.addFragment(new FragmentContacts(), "gift box");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);

    }

    public class BackPressCloseHandler{

        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context){
            this.activity = context;
        }

        public void onBackPressed(){
            if(System.currentTimeMillis() > backKeyPressedTime + 2000){
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
                activity.finish();
                toast.cancel();
            }
        }

        public void showGuide(){
            toast = Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르시면 종료됩니다. ", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}