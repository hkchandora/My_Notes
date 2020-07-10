package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.himanshu.mynotes.Model.Notes;
import com.himanshu.mynotes.ViewHolder.NoteViewHolder;

import java.util.ArrayList;
import java.util.Random;

public class DashBoard extends AppCompatActivity {

    private static final String TAG = "DashBoard";
    private static final int NUM_COLUMNS = 2;

    private ArrayList<String> nTitle = new ArrayList<>();
    private ArrayList<String> nDescription = new ArrayList<>();
    private ArrayList<String> nDate = new ArrayList<>();

    private DatabaseReference reference;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;

    private ProgressDialog loadBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

//        InitNoteBitmaps();


        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Notes")
                .child(auth.getCurrentUser().getUid());

        recyclerView = findViewById(R.id.dashboard_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadBar = new ProgressDialog(this);


        loadBar.show();
        loadBar.setContentView(R.layout.progress_dialog);
        loadBar.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadBar.setCanceledOnTouchOutside(false);
        loadBar.dismiss();

        FirebaseRecyclerOptions<Notes> options =
                new FirebaseRecyclerOptions.Builder<Notes>()
                        .setQuery(reference, Notes.class)
                        .build();

        FirebaseRecyclerAdapter<Notes, NoteViewHolder> adapter =
                new FirebaseRecyclerAdapter<Notes, NoteViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull NoteViewHolder holder, final int position, @NonNull final Notes model) {

                        Random random = new Random();
                        int currentColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                        holder.cardView.setCardBackgroundColor(currentColor);

                        holder.Title.setText(model.getTitle());
                        holder.Description.setText(model.getDescription());
                        holder.Date.setText(model.getDate());

                        loadBar.dismiss();

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getApplicationContext(), EditNote.class)
                                        .putExtra("type", "editText")
                                        .putExtra("nid", model.getNid()));
                            }
                        });

                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Toast.makeText(DashBoard.this, "Long Press", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_grid_item, parent, false);
                        return new NoteViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

//    public void InitNoteBitmaps() {
//
//        nTitle.add("Health");
//        nDescription.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fermentume in libero sit amet dignissim");
//        nDate.add("10 May 2020");
//
//        nTitle.add("Shop List");
//        nDescription.add("1. vegetables\n2. Chilly\n3. Masala\n4. Chicken");
//        nDate.add("10 June 2020");
//
//        nTitle.add("Food");
//        nDescription.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fermentume in libero sit amet dignissim");
//        nDate.add("25 May 2020");
//
//        nTitle.add("List");
//        nDescription.add("1. vegetables\n2. Chilly\n3. Masala\n4. Chicken");
//        nDate.add("10 June 2020");
//
//        nTitle.add("Health");
//        nDescription.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fermentume in libero sit amet dignissim");
//        nDate.add("10 May 2020");
//
//        nTitle.add("Shop List");
//        nDescription.add("1. vegetables\n2. Chilly\n3. Masala\n4. Chicken");
//        nDate.add("10 June 2020");
//
//        InitRecyclerView();
//    }
//
//    private void InitRecyclerView() {
//        recyclerView = findViewById(R.id.dashboard_recyclerView);
//        NoteRecyclerViewAdapter noteRecyclerViewAdapter = new NoteRecyclerViewAdapter(this, nTitle, nDescription, nDate);
//
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(staggeredGridLayoutManager);
//        recyclerView.setAdapter(noteRecyclerViewAdapter);
//    }

    public void AddNoteButton(View view) {
        startActivity(new Intent(getApplicationContext(), EditNote.class)
                .putExtra("type", "addNote"));
    }

    public void LogOutAccount(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}
