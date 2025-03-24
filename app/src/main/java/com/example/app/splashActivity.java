package com.example.app;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        VideoView videoView = findViewById(R.id.videoView);


        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
        videoView.setVideoURI(videoUri);


        videoView.start();


        Intent home = new Intent(splashActivity.this,MainActivity.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(home);
                finish(); // stack madhali activity pop karnyasathi  back karu tevha splash screen disali nahi pahije
            }
        },500);

    }
}
