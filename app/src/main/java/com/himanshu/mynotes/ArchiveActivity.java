package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.mynotes.animation.CustomItemAnimation;
import com.himanshu.mynotes.model.Notes;
import com.himanshu.mynotes.util.CryptoUtil;
import com.himanshu.mynotes.viewHolder.NoteViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ArchiveActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static final int NUM_COLUMNS = 2;
    private DatabaseReference reference;
    private RelativeLayout noNoteLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive2);

        noNoteLayout = findViewById(R.id.relative_layout3);
        noNoteLayout.setVisibility(View.GONE);

        reference = FirebaseDatabase.getInstance().getReference().child("notes")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("archivedNotes");

        recyclerView = findViewById(R.id.archive_recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    recyclerViewShow();
                } else {
                    noNoteLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void recyclerViewShow(){
        Query query = reference;

        FirebaseRecyclerOptions<Notes> options =
                new FirebaseRecyclerOptions.Builder<Notes>()
                        .setQuery(query, Notes.class)
                        .build();

        final FirebaseRecyclerAdapter<Notes, NoteViewHolder> adapter =
                new FirebaseRecyclerAdapter<Notes, NoteViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position, @NonNull final Notes model) {

//                        if (model.getNoteTitle() != null && !model.getNoteTitle().isEmpty()) {
//                            try {
//                                String decryptedText = new CryptoUtil().decrypt(model.getNoteId(), model.getNoteTitle());
//                                model.setNoteTitle(decryptedText);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        if (model.getNoteDesc() != null && !model.getNoteDesc().isEmpty()) {
//                            try {
//                                String decryptedText = new CryptoUtil().decrypt(model.getNoteId(), model.getNoteDesc());
//                                model.setNoteDesc(decryptedText);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }

                        holder.Description.setText(model.getNoteDesc());
                        holder.Date.setText(model.getTimeOfCreation());
                        holder.cardView.setCardBackgroundColor(Color.parseColor(model.getTileColor()));
                        if (model.getNoteTitle().equals("")) {
                            holder.Title.setVisibility(View.GONE);
                        } else if (!model.getNoteTitle().equals("")) {
                            holder.Title.setVisibility(View.VISIBLE);
                            holder.Title.setText(model.getNoteTitle());
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ArchiveActivity.this, EditNoteActivity.class);
                                Pair[] pairs = new Pair[2];
                                pairs[0] = new Pair<View, String>(holder.Title, "note_title");
                                pairs[1] = new Pair<View, String>(holder.Description, "note_description");
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ArchiveActivity.this, pairs);
                                intent.putExtra(EditNoteActivity.ACTION_TYPE, EditNoteActivity.ACTION_EDIT_NOTE);
                                intent.putExtra(EditNoteActivity.FROM_ACTIVITY, EditNoteActivity.ARCHIVE);
                                intent.putExtra(EditNoteActivity.NOTE_DATA, model);
                                startActivity(intent, options.toBundle());
                            }
                        });

                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                vb.vibrate(35);
                                popUpDialogForNote(model);
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

    public void popUpDialogForNote(Notes model) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_archive_note);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView TitleTxt = dialog.findViewById(R.id.dialog_archive_title);
        TextView DescriptionTxt = dialog.findViewById(R.id.dialog_archive_description);
        TextView DateTxt = dialog.findViewById(R.id.dialog_archive_date);
        Button DeleteBtn = dialog.findViewById(R.id.dialog_archive_delete_btn);
        Button UnarchiveBtn = dialog.findViewById(R.id.dialog_archive_archive_btn);
        CardView cardView = dialog.findViewById(R.id.dialog_archive_cardView);

        TitleTxt.setText(model.getNoteTitle());
        DescriptionTxt.setText(model.getNoteDesc());
        DateTxt.setText(model.getTimeOfCreation());
        cardView.setCardBackgroundColor(Color.parseColor(model.getTileColor()));

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
                String deletedDate = currentDate.format(calendar.getTime());

                final DatabaseReference fromReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("archivedNotes").child(model.getNoteId());
                final DatabaseReference toReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("deletedNotes").child(model.getNoteId());

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        toReference.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    fromReference.removeValue();
                                    Toast.makeText(ArchiveActivity.this, "Moved to bin", Toast.LENGTH_SHORT).show();

                                    toReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                snapshot.child("deletedDate").getRef().setValue(deletedDate);
                                                snapshot.child("lastEditTime").getRef().removeValue();
                                                snapshot.child("deletedFrom").getRef().setValue("archive");
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(ArchiveActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                fromReference.addListenerForSingleValueEvent(valueEventListener);
            }
        });

        UnarchiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final DatabaseReference fromReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("archivedNotes").child(model.getNoteId());
                final DatabaseReference toReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("noteList").child(model.getNoteId());

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        toReference.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    fromReference.removeValue();
                                    Toast.makeText(ArchiveActivity.this, "Unarchived", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ArchiveActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                fromReference.addListenerForSingleValueEvent(valueEventListener);
            }
        });
        dialog.show();
    }


    public void ArchiveBackButton(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}