package com.himanshu.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.himanshu.mynotes.adapter.OnBoardingAdapter;
import com.himanshu.mynotes.model.OnBoardingItem;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends AppCompatActivity {

    private OnBoardingAdapter onBoardingAdapter;
    private LinearLayout layoutOnboaoardingIndigator;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        isThisFirstTime();

        layoutOnboaoardingIndigator = findViewById(R.id.layoutOnBoardingIndicators);
        nextBtn = findViewById(R.id.buttonOnBoardAction);

        setUpOnBoardingItem();

        ViewPager2 onBoardingViewPager = findViewById(R.id.onBoardingViewPager);
        onBoardingViewPager.setAdapter(onBoardingAdapter);

        setupOnBoardingIndicator();
        setCurrentOnBoardingIndicator(0);

        onBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnBoardingIndicator(position);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onBoardingViewPager.getCurrentItem() + 1 < onBoardingAdapter.getItemCount()){
                    onBoardingViewPager.setCurrentItem(onBoardingViewPager.getCurrentItem() + 1);
                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });

    }

    private void setUpOnBoardingItem() {
        List<OnBoardingItem> onBoardingItems = new ArrayList<>();

        OnBoardingItem itemPayOnline = new OnBoardingItem();
        itemPayOnline.setTitle("Pay Your Bill Online");
        itemPayOnline.setDescription("Electric bill payment is a feature of online, mobile and telegram banking.");
        itemPayOnline.setImage(R.drawable.add_note_photo);

        OnBoardingItem itemPayOnline2 = new OnBoardingItem();
        itemPayOnline2.setTitle("Pay Your Bill Online");
        itemPayOnline2.setDescription("Electric bill payment is a feature of online, mobile and telegram banking.");
        itemPayOnline2.setImage(R.drawable.add_note);

        OnBoardingItem itemPayOnline3 = new OnBoardingItem();
        itemPayOnline3.setTitle("Pay Your Bill Online");
        itemPayOnline3.setDescription("Electric bill payment is a feature of online, mobile and telegram banking.");
        itemPayOnline3.setImage(R.drawable.profilemale);

        onBoardingItems.add(itemPayOnline);
        onBoardingItems.add(itemPayOnline2);
        onBoardingItems.add(itemPayOnline3);

        onBoardingAdapter = new OnBoardingAdapter(onBoardingItems);
    }

    public void setupOnBoardingIndicator() {
        ImageView[] indicators = new ImageView[onBoardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboaoardingIndigator.addView(indicators[i]);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setCurrentOnBoardingIndicator(int index) {
        int childCount = layoutOnboaoardingIndigator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutOnboaoardingIndigator.getChildAt(i);
            if(i == index){
                imageView.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive)
                );
            }
        }

        if(index == onBoardingAdapter.getItemCount() - 1){
            nextBtn.setText("Start");
        } else {
            nextBtn.setText("Next");
        }
    }

    public void isThisFirstTime(){
        //Check if app is opened for the first time
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String FirstTime = preferences.getString("FirstTimeInstall", "");

        if(FirstTime.equals("Yes")){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstTimeInstall", "Yes");
            editor.apply();
        }
    }
}