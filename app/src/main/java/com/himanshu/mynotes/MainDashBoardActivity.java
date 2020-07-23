package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.himanshu.mynotes.fragment.DashBoardFragment;
import com.himanshu.mynotes.fragment.ProfileFragment;

public class MainDashBoardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new DashBoardFragment()).commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment fragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.dashboard:
                            fragment = new DashBoardFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                            break;
                        case R.id.add_note:
                            Intent i = new Intent(MainDashBoardActivity.this, EditNoteActivity.class);
                            i.putExtra("type", "addNote");
                            startActivity(i);
                            break;
                        case R.id.profile:
                            fragment = new ProfileFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                            break;
                    }

                    return true;
                }
            };
}
