package com.himanshu.mynotes.viewHolder;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.himanshu.mynotes.R;

/**
 * Created by Kalpesh on 14/07/20.
 */
public class ColorListItemViewHolder extends RecyclerView.ViewHolder {

    public ColorListItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(String color) {
        View v = itemView.findViewById(R.id.view_color_list_item);
        v.getBackground().setColorFilter(Color.parseColor(color),
                PorterDuff.Mode.SRC_IN);
    }

}
