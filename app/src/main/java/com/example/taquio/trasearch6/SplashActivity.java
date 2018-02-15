package com.example.taquio.trasearch6;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        refId();
//        animation
        Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        splashImage.startAnimation(mAnimation);

        final Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        //splash screen
        Thread timer = new Thread()
        {
          public void run()
          {
              try{
                  sleep(5000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }finally {
                  startActivity(intent);
                  finish();
              }
          }
        };
        timer.start();
    }

    public void refId(){
        splashImage = (ImageView) findViewById(R.id.splash_image);
    }
}
