package com.example.taquio.trasearch6;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
//                Intent startActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(startActivityIntent);
//                SplashActivity.this.finish();

                SharedPreferences settings=getSharedPreferences("prefs",0);
                boolean firstRun=settings.getBoolean("firstRun",false);
                if(!firstRun)//if running for first time
                //Splash will load for first time
                {
                    SharedPreferences.Editor editor=settings.edit();
                    editor.putBoolean("firstRun",true);
                    editor.commit();
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();

                }
                else
                {

                    startActivity(new Intent(SplashActivity.this,GuestSearch.class));
                    finish();
                }



            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    public void refId(){
        layouttop = findViewById(R.id.layouttop);
        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
    }
}
