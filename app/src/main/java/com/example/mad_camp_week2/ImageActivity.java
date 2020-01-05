package com.example.mad_camp_week2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_camp_week2.R;
import com.github.chrisbanes.photoview.PhotoView;


public class ImageActivity extends AppCompatActivity {
    private TextView imgTitle;
    //private ImageView img;
    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imgTitle = (TextView)findViewById(R.id.img_title);
       // img = (ImageView)findViewById(R.id.img_thumbnail);
        photoView = findViewById(R.id.photoView);

        // Receive Data
        Intent intent = getIntent();
        String title = intent.getExtras().getString("Title");
        String image = intent.getExtras().getString("Thumbnail");

        // Setting values
        imgTitle.setText(title);
       // img.setImageURI(Uri.parse(image));
        photoView.setImageURI(Uri.parse(image));
    }
}
