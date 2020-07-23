package com.himanshu.mynotes.viewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.himanshu.mynotes.R;

public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView Title, Description, Date, deletedDate;
    public CardView cardView;
    public ImageView Pin;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        Title = itemView.findViewById(R.id.note_item_title);
        Description = itemView.findViewById(R.id.note_item_description);
        Date = itemView.findViewById(R.id.note_item_date);
        cardView = itemView.findViewById(R.id.note_item_cardView);
        Pin = itemView.findViewById(R.id.note_item_pin);
        deletedDate = itemView.findViewById(R.id.note_item_deleted_date);
    }

    @Override
    public void onClick(View view) {

    }
}
