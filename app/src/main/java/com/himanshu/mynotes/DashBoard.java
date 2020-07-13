package com.himanshu.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.himanshu.mynotes.Adapter.NoteRecyclerViewAdapter;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity {

    private static final String TAG = "DashBoard";
    private static final int NUM_COLUMNS = 2;

    private ArrayList<String> nTitle = new ArrayList<>();
    private ArrayList<String> nDescription = new ArrayList<>();
    private ArrayList<String> nDate = new ArrayList<>();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        InitNoteBitmaps();

    }

    public void InitNoteBitmaps(){

        nTitle.add("Health");
        nDescription.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fermentume in libero sit amet dignissim");
        nDate.add("10 May 2020");

        nTitle.add("Shop List");
        nDescription.add("1. vegetables\n2. Chilly\n3. Masala\n4. Chicken");
        nDate.add("10 June 2020");

        nTitle.add("Food");
        nDescription.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fermentume in libero sit amet dignissim");
        nDate.add("25 May 2020");

        nTitle.add("List");
        nDescription.add("1. vegetables\n2. Chilly\n3. Masala\n4. Chicken");
        nDate.add("10 June 2020");

        nTitle.add("Health");
        nDescription.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fermentume in libero sit amet dignissim");
        nDate.add("10 May 2020");

        nTitle.add("Shop List");
        nDescription.add("1. vegetables\n2. Chilly\n3. Masala\n4. Chicken");
        nDate.add("10 June 2020");

        InitRecyclerView();
    }

    private void InitRecyclerView(){
        recyclerView = findViewById(R.id.dashboard_recyclerView);
        NoteRecyclerViewAdapter noteRecyclerViewAdapter = new NoteRecyclerViewAdapter(this, nTitle,nDescription, nDate);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(noteRecyclerViewAdapter);
    }

    public void LogOutAccount(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void fetchColors(){
        // TODO(Fetch colors from db)
    }

}
