package com.himanshu.mynotes.adapter;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.himanshu.mynotes.EditNoteActivity;
import com.himanshu.mynotes.R;
import com.himanshu.mynotes.model.Notes;

import java.util.List;

public class NoteItemAdapter extends RecyclerView.Adapter<NoteItemAdapter.ViewHolder> {

    private List<Notes> noteData;
    private int TestCount = 0;
    private Context context;

    public NoteItemAdapter(List<Notes> noteData, Context context) {
        this.noteData = noteData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_grid, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        TestCount += 1;
        Notes ld = noteData.get(i);

        if (ld.getNoteTitle().equals("")) {
            holder.Title.setVisibility(View.GONE);
        } else if (!ld.getNoteTitle().equals("")) {
            holder.Title.setVisibility(View.VISIBLE);
            holder.Title.setText(ld.getNoteTitle());
        }
        if (ld.getNoteDesc().equals("")) {
            holder.Description.setVisibility(View.GONE);
        } else if (!ld.getNoteDesc().equals("")) {
            holder.Description.setVisibility(View.VISIBLE);
            holder.Description.setText(ld.getNoteDesc());
        }
        if (ld.getIsPinned()) {
            holder.Pin.setVisibility(View.VISIBLE);
        } else if (!ld.getIsPinned()) {
            holder.Pin.setVisibility(View.INVISIBLE);
        }
        holder.Date.setText(ld.getTimeOfCreation());
        holder.cardView.setCardBackgroundColor(Color.parseColor(ld.getTileColor()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(holder.Title, "note_title");
                pairs[1] = new Pair<View, String>(holder.Description, "note_description");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) v.getContext(), pairs);
                intent.putExtra(EditNoteActivity.ACTION_TYPE, EditNoteActivity.ACTION_EDIT_NOTE);
                intent.putExtra(EditNoteActivity.FROM_ACTIVITY, EditNoteActivity.DASHBOARD);
                intent.putExtra(EditNoteActivity.NOTE_DATA, ld);
                v.getContext().startActivity(intent, options.toBundle());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Vibrator vb = (Vibrator) Context.getSystemService(Context.VIBRATOR_SERVICE);
//                vb.vibrate(35);
//                popUpDialogForNote(ld);

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return noteData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Title, Description, Date, deletedDate;
        public CardView cardView;
        public ImageView Pin;

        public ViewHolder(View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.note_item_title);
            Description = itemView.findViewById(R.id.note_item_description);
            Date = itemView.findViewById(R.id.note_item_date);
            cardView = itemView.findViewById(R.id.note_item_cardView);
            Pin = itemView.findViewById(R.id.note_item_pin);
            deletedDate = itemView.findViewById(R.id.note_item_deleted_date);

        }
    }
}
