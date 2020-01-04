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
    FloatingActionButton img_share;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;

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

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
                Uri uri = Uri.fromFile(tmp_file);
                Log.v("sharing_intent_name: ", uri.toString());
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT, uri.toString());
                share.setType("image/*");
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(share, "Share image using"));

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                Toast.makeText(getApplicationContext(), "Button2", Toast.LENGTH_SHORT).show();
            }
        });



    }


    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }


    }
}
