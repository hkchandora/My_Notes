package com.himanshu.mynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.himanshu.mynotes.listeners.OnNoteColorClickListener;
import com.himanshu.mynotes.model.NoteColor;
import com.himanshu.mynotes.viewHolder.ColorListItemViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kalpesh on 14/07/20.
 */
class ColorListAdapter extends RecyclerView.Adapter<ColorListItemViewHolder> {

    private List<NoteColor> colorsList;
    private OnNoteColorClickListener listener;
    public int selectedColorPosition = 0;

    public ColorListAdapter(List<NoteColor> colorsList, OnNoteColorClickListener listener) {
        this.colorsList = colorsList;
        this.listener = listener;
        Collections.sort(colorsList, (s1, s2) -> s1.getPosition() - s2.getPosition());
    }

    @NonNull
    @Override
    public ColorListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_colors_list_item, parent, false);
        return new ColorListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorListItemViewHolder holder, int position) {
        holder.bind(colorsList.get(position));
        holder.itemView.setOnClickListener(view -> {
            notifyItemChanged(selectedColorPosition);
            colorsList.get(selectedColorPosition).setSelected(false);
            listener.onColorClick(colorsList.get(holder.getAdapterPosition()).getColor());
            colorsList.get(holder.getAdapterPosition()).setSelected(true);
            notifyItemChanged(position);
            selectedColorPosition = position;
        });
    }

    @Override
    public int getItemCount() {
        return colorsList.size();
    }

}
