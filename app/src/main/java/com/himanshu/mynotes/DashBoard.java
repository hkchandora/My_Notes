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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.mynotes.Model.Notes;
import com.himanshu.mynotes.ViewHolder.NoteViewHolder;

import java.util.ArrayList;
import java.util.Random;

public class DashBoard extends AppCompatActivity {

    private static final int NUM_COLUMNS = 2;

    private DatabaseReference reference;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;
private TextView CurrentUserName;
    private ProgressDialog loadBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Notes")
                .child(auth.getCurrentUser().getUid());

        CurrentUserName = findViewById(R.id.dashboard_name);
        recyclerView = findViewById(R.id.dashboard_recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        loadBar = new ProgressDialog(this);
        loadBar.show();
        loadBar.setContentView(R.layout.progress_dialog);
        loadBar.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadBar.setCanceledOnTouchOutside(false);

        CheckAnyNoteIsAvailable();

        RetrieveCurrentUserInfo();

        RecyclerViewShow();
    }

    public void CheckAnyNoteIsAvailable(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loadBar.show();
                    loadBar.setContentView(R.layout.progress_dialog);
                    loadBar.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    loadBar.setCanceledOnTouchOutside(false);
                } else {
                    Toast.makeText(DashBoard.this, "No Notes Exists", Toast.LENGTH_SHORT).show();
                    loadBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void RetrieveCurrentUserInfo(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            CurrentUserName.setText("Hello "+snapshot.child("name").getValue().toString()+",\nGood morning");
                            loadBar.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void RecyclerViewShow(){

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
                                        .putExtra("type", "editNote")
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

    public void AddNoteButton(View view) {
        Intent i = new Intent(getApplicationContext(), EditNote.class);
        i.putExtra("type", "addNote");
        startActivity(i);
    }

    public void LogOutAccount(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}
