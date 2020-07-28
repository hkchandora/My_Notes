package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.himanshu.mynotes.fragment.DashBoardFragment;
import com.himanshu.mynotes.fragment.ProfileFragment;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MainDashBoardActivity extends AppCompatActivity {

    private SpaceNavigationView spaceNavigationView;
    private Fragment fragment = null, currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board);

        spaceNavigationView = findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.dashboard_icon));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.user_profile_icon));

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new DashBoardFragment()).commit();

        currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Intent i = new Intent(getApplicationContext(), EditNoteActivity.class);
                i.putExtra(EditNoteActivity.ACTION_TYPE, EditNoteActivity.ACTION_CREATE_NOTE);
                startActivity(i);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex == 0) {
                    fragment = new DashBoardFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();

                } else if (itemIndex == 1) {
                    fragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .addToBackStack(DashBoardFragment.class.getSimpleName())
                            .commit();
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        spaceNavigationView.onSaveInstanceState(outState);
    }
}