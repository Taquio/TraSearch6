package com.example.taquio.trasearch6;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 5000;
    private FirebaseAuth mAuth;

    private ViewPager viewPager;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private LinearLayout mDotLayout;

    private Button mNextBtn;
    private Button mBackBtn;

    private int mCurrentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        loadSlidingViewPager();

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent startActivityIntent = new Intent(MainActivity.this, AppStart.class);
                startActivity(startActivityIntent);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

        mAuth = FirebaseAuth.getInstance();


    }

    //for the dots in the sliding intro
//    private void addDotsIndicator(int position)
//    {
//        mDotLayout = (LinearLayout) findViewById(R.id.mDotsLayout);
//        mDots = new TextView[5];
//        mDotLayout.removeAllViews();
//        for(int i = 0; i < mDots.length; i++)
//        {
//            mDots[i] = new TextView(this);
//            mDots[i].setText(Html.fromHtml("&#8226;"));
//            mDots[i].setTextSize(35);
//            mDots[i].setTextColor(getResources().getColor(R.color.colorAccent));
//
//            mDotLayout.addView(mDots[i]);
//        }
//        if(mDots.length > 0)
//        {
//            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
//        }
//    }
//    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            addDotsIndicator(position);
//            mCurrentPage = position;
//
//            if(position == 0)
//            {
//                mNextBtn.setEnabled(true);
//                mBackBtn.setEnabled(false);
//                mBackBtn.setVisibility(View.INVISIBLE);
//
//                mNextBtn.setText("Next");
//                mBackBtn.setText("");
//            }else if(position == mDots.length -1){
//                mNextBtn.setEnabled(true);
//                mBackBtn.setEnabled(true);
//                mBackBtn.setVisibility(View.VISIBLE);
//
//                mNextBtn.setText("Finish");
//                mBackBtn.setText("Back");
//            }else{
//                mNextBtn.setEnabled(true);
//                mBackBtn.setEnabled(true);
//                mBackBtn.setVisibility(View.VISIBLE);
//
//                mNextBtn.setText("Next");
//                mBackBtn.setText("Back");
//            }
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    };
//    //for the sliding intro
//    private void loadSlidingViewPager() {
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        sliderAdapter = new SliderAdapter(this);
//        viewPager.setAdapter(sliderAdapter);
//
//        addDotsIndicator(0);
//        viewPager.addOnPageChangeListener(viewListener);
//
//        mNextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(mCurrentPage + 1);
//            }
//        });
//
//        mBackBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(mCurrentPage - 1);
//            }
//        });
//    }

    private void updateUI(FirebaseUser user){
        if(user!=null)
        {
            Toast.makeText(MainActivity.this,"Welcome back",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}
