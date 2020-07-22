package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.mynotes.animation.CustomItemAnimation;
import com.himanshu.mynotes.model.Notes;
import com.himanshu.mynotes.viewHolder.NoteViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DeleteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static final int NUM_COLUMNS = 2;
    private DatabaseReference reference;
    int currentColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        reference = FirebaseDatabase.getInstance().getReference().child("notes")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("deletedNotes");

        recyclerView = findViewById(R.id.delete_recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        checkWhenNoteDeleted();

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
                        holder.cardView.setCardBackgroundColor(Color.parseColor(model.getTileColor()));
                        if (model.getNoteTitle().equals("")) {
                            holder.Title.setVisibility(View.GONE);
                        } else if (!model.getNoteTitle().equals("")) {
                            holder.Title.setVisibility(View.VISIBLE);
                            holder.Title.setText(model.getNoteTitle());
                        }

                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                popUpDialogForNote(model.getNoteTitle(), model.getNoteDesc(), model.getTimeOfCreation(), model.getTileColor(), model.getNoteId());
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

    public void popUpDialogForNote(final String title, final String description, String date, String bgColor, final String nid) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete_note);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView TitleTxt = dialog.findViewById(R.id.dialog_delete_title);
        TextView DescriptionTxt = dialog.findViewById(R.id.dialog_delete_description);
        TextView DateTxt = dialog.findViewById(R.id.dialog_delete_date);
        Button DeleteBtn = dialog.findViewById(R.id.dialog_delete_delete_btn);
        Button RestoreBtn = dialog.findViewById(R.id.dialog_delete_restore_btn);
        Button CopyBtn = dialog.findViewById(R.id.dialog_delete_copy_btn);
        CardView cardView = dialog.findViewById(R.id.dialog_delete_cardView);

        TitleTxt.setText(title);
        DescriptionTxt.setText(description);
        DateTxt.setText(date);
        cardView.setCardBackgroundColor(Color.parseColor(bgColor));

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference deleteReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("deletedNotes").child(nid);

                deleteReference.removeValue();
                dialog.dismiss();
                Toast.makeText(DeleteActivity.this, "Note Permanently deleted", Toast.LENGTH_SHORT).show();
            }
        });

        CopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = title + "\n" + description;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Note", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(DeleteActivity.this, "Note Copied", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RestoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference fromReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("deletedNotes").child(nid);
                final DatabaseReference toReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("noteList").child(nid);

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        toReference.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    fromReference.removeValue();
                                    Toast.makeText(DeleteActivity.this, "Restored", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DeleteActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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


    public void checkWhenNoteDeleted() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dn : snapshot.getChildren()) {
                        String storeDate = dn.child("timeOfCreation").getValue().toString();

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
                        String CurrentDate = currentDate.format(calendar.getTime());
                        Date date = new Date(CurrentDate);
                        Date date2 = new Date(storeDate);
                        long diff = date.getTime() - date2.getTime();

                        if ((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))> 7) {
                            dn.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void DeleteBackButton(View view) {
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