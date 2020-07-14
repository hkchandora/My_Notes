package com.himanshu.mynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.himanshu.mynotes.viewHolder.ColorListItemViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kalpesh on 14/07/20.
 */
class ColorListAdapter extends RecyclerView.Adapter<ColorListItemViewHolder> {

    private List<String> colorsList;

    public ColorListAdapter(List<String> colorsList) {
        this.colorsList = colorsList;
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
    }

    @Override
    public int getItemCount() {
        return colorsList.size();
    }

}
