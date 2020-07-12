package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.mynotes.animation.CustomItemAnimation;
import com.himanshu.mynotes.model.Notes;
import com.himanshu.mynotes.viewHolder.NoteViewHolder;

import java.util.Calendar;
import java.util.Random;

public class DashBoardActivity extends AppCompatActivity {

    private static final int NUM_COLUMNS = 2;

    private DatabaseReference reference;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;
    private TextView CurrentUserName;
    private ProgressDialog loadBar;
    private String currentTime = "";

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
        staggeredGridLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
//
//        loadBar = new ProgressDialog(this);
//        loadBar.show();
//        loadBar.setContentView(R.layout.progress_dialog);
//        loadBar.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        loadBar.setCanceledOnTouchOutside(false);

        checkAnyNoteIsAvailable();

        retrieveCurrentUserInfo();

        recyclerViewShow();
    }

    public void checkAnyNoteIsAvailable() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
//                    loadBar.show();
//                    loadBar.setContentView(R.layout.progress_dialog);
//                    loadBar.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                    loadBar.setCanceledOnTouchOutside(false);
                    Toast.makeText(DashBoardActivity.this, "No Notes Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void retrieveCurrentUserInfo() {

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            currentTime = "Good morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            currentTime = "Good afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            currentTime = "Good evening";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            currentTime = "Good night";
        }

        final String finalTime = currentTime;
        FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            CurrentUserName.setText("Hello " + snapshot.child("name").getValue().toString() + ",\n" + currentTime);
//                            loadBar.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void recyclerViewShow() {

        FirebaseRecyclerOptions<Notes> options =
                new FirebaseRecyclerOptions.Builder<Notes>()
                        .setQuery(reference.child("Note"), Notes.class)
                        .build();

        final FirebaseRecyclerAdapter<Notes, NoteViewHolder> adapter =
                new FirebaseRecyclerAdapter<Notes, NoteViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position, @NonNull final Notes model) {

                        Random random = new Random();
                        final int currentColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                        holder.cardView.setCardBackgroundColor(currentColor);

                        holder.Title.setText(model.getTitle());
                        holder.Description.setText(model.getDescription());
                        holder.Date.setText(model.getDate());

//                        loadBar.dismiss();

                        ViewCompat.setTransitionName(holder.Title, model.getTitle());


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                                intent.putExtra("type", "editNote");
                                intent.putExtra("nid", model.getNid());
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                popUpDialogForNote(model.getTitle(), model.getDescription(), model.getDate(), currentColor, model.getNid());
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
    
    public void popUpDialogForNote(final String title, final String description, String date, int bgColor, final String nid){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_long_press_note);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView TitleTxt = dialog.findViewById(R.id.dialog_long_press_title);
        TextView DescriptionTxt = dialog.findViewById(R.id.dialog_long_press_description);
        TextView DateTxt = dialog.findViewById(R.id.dialog_long_press_date);
        Button DeleteBtn = dialog.findViewById(R.id.dialog_long_press_delete_btn);
        Button ArchiveBtn = dialog.findViewById(R.id.dialog_long_press_archive_btn);
        Button CopyBtn = dialog.findViewById(R.id.dialog_long_press_copy_btn);
        CardView cardView = dialog.findViewById(R.id.dialog_long_press_cardView);

        TitleTxt.setText(title);
        DescriptionTxt.setText(description);
        DateTxt.setText(date);
        cardView.setCardBackgroundColor(bgColor);

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference deleteNoteReference = FirebaseDatabase.getInstance().getReference().child("Notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Note").child(nid);

                deleteNoteReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        Toast.makeText(DashBoardActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ArchiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference fromReference = FirebaseDatabase.getInstance().getReference().child("Notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Note").child(nid);
                final DatabaseReference toReference = FirebaseDatabase.getInstance().getReference().child("Notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Archive").child(nid);

                ValueEventListener valueEventListener = new ValueEventListener()  {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        toReference.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    fromReference.removeValue();
                                    Toast.makeText(DashBoardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(DashBoardActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };

                fromReference.addListenerForSingleValueEvent(valueEventListener);
            }
        });

        CopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = title+"\n"+description;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Note", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(DashBoardActivity.this, "Note Copied", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void addNoteButton(View view) {
        Intent i = new Intent(getApplicationContext(), EditNoteActivity.class);
        i.putExtra("type", "addNote");
        startActivity(i);
    }

    public void logOutAccount(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}
