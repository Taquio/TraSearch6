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

    //firebase authoration
    private FirebaseAuth mAuth;
    //for onboard slider
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

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

//Onboarding Slider with button
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);
        loadSlidingViewPager();

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
    }

    //for the Next and Back button
    private void loadSlidingViewPager() {

        mNextBtn = findViewById(R.id.nextBtn);
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        condition to load main activity
                if(mNextBtn.getText().toString() == "Finish") {
                    Intent startActivityIntent = new Intent(MainActivity.this, AppStart.class);
                    startActivity(startActivityIntent);
                    MainActivity.this.finish();

                }else
                    viewPager.setCurrentItem(mCurrentPage + 1);
            }
        });
        mBackBtn = findViewById(R.id.prevBtn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mCurrentPage - 1);
            }
        });
    }

    //for the dots in the sliding intro
    private void addDotsIndicator(int position)
    {
        mDotLayout = (LinearLayout) findViewById(R.id.mDotsLayout);
        mDots = new TextView[5];
        mDotLayout.removeAllViews();
        for(int i = 0; i < mDots.length; i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorAccent));

            mDotLayout.addView(mDots[i]);
        }
        if(mDots.length > 0)
        {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);
            mCurrentPage = i;

            if(i == 0)
            {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("Next");
                mBackBtn.setText("");
            }else if(i == mDots.length -1){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("Finish");
                mBackBtn.setText("Back");
            }else{
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("Next");
                mBackBtn.setText("Back");
            }        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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
