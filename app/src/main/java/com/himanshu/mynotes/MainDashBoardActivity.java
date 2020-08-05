package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.mynotes.fragment.DashBoardFragment;
import com.himanshu.mynotes.fragment.ProfileFragment;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MainDashBoardActivity extends AppCompatActivity {

    private SpaceNavigationView spaceNavigationView;
    private Fragment profileFragment;
    private Fragment dashboardFragment;
    private Fragment currentFragment;
    private int version;
    private Boolean isMandatory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board);

        spaceNavigationView = findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.dashboard_icon));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.user_profile_icon));

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new DashBoardFragment()).commit();

        dashboardFragment = new DashBoardFragment();
        profileFragment = new ProfileFragment();

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Intent i = new Intent(getApplicationContext(), EditNoteActivity.class);
                i.putExtra(EditNoteActivity.ACTION_TYPE, EditNoteActivity.ACTION_CREATE_NOTE);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.zoom_in, R.anim.static_animation).toBundle();
                startActivity(i, bundle);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex == 0) {
                    currentFragment = dashboardFragment;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, dashboardFragment)
//                            .addToBackStack(DashBoardFragment.class.getSimpleName())
                            .commit();
                } else if (itemIndex == 1) {
                    currentFragment = profileFragment;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, profileFragment)
//                            .addToBackStack(DashBoardFragment.class.getSimpleName())
                            .commit();
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
            }
        });


        checkUpdateAvailable();
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof DashBoardFragment) {
            finish();
            return;
        }
        if (currentFragment instanceof ProfileFragment) {
            currentFragment = dashboardFragment;
            spaceNavigationView.changeCurrentItem(0);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, dashboardFragment)
//                            .addToBackStack(DashBoardFragment.class.getSimpleName())
                    .commit();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        spaceNavigationView.onSaveInstanceState(outState);
    }

    public void checkUpdateAvailable() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("appInfo");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    version = Integer.parseInt(snapshot.child("version").getValue() + "");
                    isMandatory = Boolean.parseBoolean(snapshot.child("isMandatory").getValue() + "");
                    if (version > 1) {
                        updateDialogShow(isMandatory);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateDialogShow(Boolean isMandatory) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_app);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        TextView dialogMsg = dialog.findViewById(R.id.dialog_update_app_txt);
        Button laterBtn = dialog.findViewById(R.id.dialog_update_app_later_btn);
        Button exitBtn = dialog.findViewById(R.id.dialog_update_app_exit_btn);
        Button updateBtn = dialog.findViewById(R.id.dialog_update_app_update_btn);

        if (isMandatory) {
            exitBtn.setVisibility(View.VISIBLE);
            laterBtn.setVisibility(View.GONE);
        }

        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMandatory){
                    dialog.dismiss();
                }
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + MainDashBoardActivity.this.getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + MainDashBoardActivity.this.getPackageName())));
                }
            }
        });
        dialog.show();
    }
}