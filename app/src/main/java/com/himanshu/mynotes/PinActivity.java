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

import java.util.Objects;

public class PinActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static final int NUM_COLUMNS = 2;
    private DatabaseReference reference;
    int currentColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);


        reference = FirebaseDatabase.getInstance().getReference().child("notes")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("noteList");

        recyclerView = findViewById(R.id.pin_recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query = reference.orderByChild("isPinned").equalTo(true);

        FirebaseRecyclerOptions<Notes> options =
                new FirebaseRecyclerOptions.Builder<Notes>()
                        .setQuery(query, Notes.class)
                        .build();

        final FirebaseRecyclerAdapter<Notes, NoteViewHolder> adapter =
                new FirebaseRecyclerAdapter<Notes, NoteViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position, @NonNull final Notes model) {

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
                                popUpDialogForNote(model.getNoteTitle(), model.getNoteDesc(), model.getTimeOfCreation(), model.getTileColor(),
                                        model.getNoteId());
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
        dialog.setContentView(R.layout.dialog_pin_note);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView TitleTxt = dialog.findViewById(R.id.dialog_pin_title);
        TextView DescriptionTxt = dialog.findViewById(R.id.dialog_pin_description);
        TextView DateTxt = dialog.findViewById(R.id.dialog_pin_date);
        Button UnpinBtn = dialog.findViewById(R.id.dialog_unpin_btn);
        CardView cardView = dialog.findViewById(R.id.dialog_pin_cardView);

        TitleTxt.setText(title);
        DescriptionTxt.setText(description);
        DateTxt.setText(date);
        cardView.setCardBackgroundColor(Color.parseColor(bgColor));

        UnpinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference pinReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("noteList").child(nid);

                pinReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            snapshot.getRef().child("isPinned").setValue(false);
                            Toast.makeText(PinActivity.this, "Unpinned", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        dialog.show();
    }


    public void PinBackButton(View view) {
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