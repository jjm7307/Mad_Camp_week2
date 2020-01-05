package com.example.mad_camp_week2.models;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_camp_week2.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.File;



public class image_inlarge extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_image_inlarge);

        Intent mIntent = getIntent();
        final String filePath = mIntent.getStringExtra("filename");
        Log.v("FileName_after_intent: ", filePath);
        final File tmp_file = new File(filePath);

        if (tmp_file.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(tmp_file.getAbsolutePath());
            PhotoView photoView = findViewById(R.id.image_large);
            photoView.setImageBitmap(myBitmap);
        }

    }
}
