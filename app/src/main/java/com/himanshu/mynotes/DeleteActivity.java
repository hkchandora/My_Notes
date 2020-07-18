package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.himanshu.mynotes.animation.CustomItemAnimation;
import com.himanshu.mynotes.model.Notes;
import com.himanshu.mynotes.viewHolder.NoteViewHolder;

public class DeleteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static final int NUM_COLUMNS = 2;
    private DatabaseReference reference;
    int currentColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        reference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("deletedNotes");

        recyclerView = findViewById(R.id.delete_recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query = reference;

        FirebaseRecyclerOptions<Notes> options =
                new FirebaseRecyclerOptions.Builder<Notes>()
                        .setQuery(query, Notes.class)
                        .build();

        final FirebaseRecyclerAdapter<Notes, NoteViewHolder> adapter =
                new FirebaseRecyclerAdapter<Notes, NoteViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position, @NonNull final Notes model) {

                        String getColor = model.getTileColor();

                        switch (getColor) {
                            case "1":
                                currentColor = R.color.color_one;
                                break;
                            case "2":
                                currentColor = R.color.color_two;
                                break;
                            case "3":
                                currentColor = R.color.color_three;
                                break;
                            case "4":
                                currentColor = R.color.color_four;
                                break;
                            case "5":
                                currentColor = R.color.color_five;
                                break;
                            case "6":
                                currentColor = R.color.color_six;
                                break;
                            case "7":
                                currentColor = R.color.color_seven;
                                break;
                            case "8":
                                currentColor = R.color.color_eight;
                                break;
                        }

                        holder.Description.setText(model.getNoteDesc());
                        holder.Date.setText(model.getTimeOfCreation());
//                        holder.cardView.setCardBackgroundColor(getResources().getColor(currentColor));
                        if (model.getNoteTitle().equals("")) {
                            holder.Title.setVisibility(View.GONE);
                        } else if (!model.getNoteTitle().equals("")) {
                            holder.Title.setVisibility(View.VISIBLE);
                            holder.Title.setText(model.getNoteTitle());
                        }

                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Toast.makeText(DeleteActivity.this, "Will implement later", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_grid, parent, false);
                        return new NoteViewHolder(view);
                    }
                };

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new CustomItemAnimation());
        adapter.startListening();
        adapter.notifyItemInserted(1);
        adapter.notifyItemRemoved(1);
    }

    public void DeleteBackButton(View view) {
        finish();
    }
}