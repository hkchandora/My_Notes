package com.himanshu.mynotes.viewHolder;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.himanshu.mynotes.R;
import com.himanshu.mynotes.model.NoteColor;

/**
 * Created by Kalpesh on 14/07/20.
 */
public class ColorListItemViewHolder extends RecyclerView.ViewHolder {

    public ColorListItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(NoteColor color) {
        View v = itemView.findViewById(R.id.view_color_list_item);
        View tick = itemView.findViewById(R.id.color_check);
        View stroke = itemView.findViewById(R.id.bg_stroke);
        v.getBackground().setColorFilter(Color.parseColor(color.getColor()),
                PorterDuff.Mode.SRC_IN);
        if (color.isSelected()) {
            tick.setVisibility(View.VISIBLE);
            stroke.setVisibility(View.VISIBLE);
        } else {
            tick.setVisibility(View.GONE);
            stroke.setVisibility(View.GONE);
        }
    }

}
