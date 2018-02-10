package com.example.taquio.trasearch6;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 5000;
    LinearLayout layouttop;
    Animation uptodown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        refId();
//        animation
        layouttop.setAnimation(uptodown);

        //splash screen
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent startActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(startActivityIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    public void refId(){
        layouttop = findViewById(R.id.layouttop);
        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
    }
}
