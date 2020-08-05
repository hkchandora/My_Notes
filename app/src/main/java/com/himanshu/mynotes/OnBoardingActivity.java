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
                    SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("FirstTimeInstall", "No");
                    editor.apply();
                }
            }
        });

    }

    private void setUpOnBoardingItem() {
        List<OnBoardingItem> onBoardingItems = new ArrayList<>();

        OnBoardingItem itemPayOnline = new OnBoardingItem();
        itemPayOnline.setTitle("Make Your Own Notes");
        itemPayOnline.setDescription("Make your own notes and save it to your personal phone.");
        itemPayOnline.setImage(R.drawable.onboarding_one);

        OnBoardingItem itemPayOnline2 = new OnBoardingItem();
        itemPayOnline2.setTitle("Notes Security");
        itemPayOnline2.setDescription("Your saved notes are secure and safe, you can save your personal info.");
        itemPayOnline2.setImage(R.drawable.onboarding_three);

        OnBoardingItem itemPayOnline3 = new OnBoardingItem();
        itemPayOnline3.setTitle("Save Your Personal Notes");
        itemPayOnline3.setDescription("You can save your personal info, these notes are safe.");
        itemPayOnline3.setImage(R.drawable.onboarding_two);

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

        if(FirstTime.equals("No")){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
//        else {
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("FirstTimeInstall", "No");
//            editor.apply();
//        }
    }
}