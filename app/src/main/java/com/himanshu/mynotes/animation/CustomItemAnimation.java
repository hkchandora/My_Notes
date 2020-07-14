package com.himanshu.mynotes.animation;

import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.himanshu.mynotes.R;

public class CustomItemAnimation extends DefaultItemAnimator {

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        //This animation is for when exist note delete
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(
                holder.itemView.getContext(),
                R.anim.delete_note_anim
        ));
        return super.animateRemove(holder);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        //This animation is for when new note create
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(
                holder.itemView.getContext(),
                R.anim.add_note_anim
        ));
        return super.animateAdd(holder);
    }

    //customize the duration
    @Override
    public long getAddDuration() {
        return 500;
    }

    @Override
    public long getRemoveDuration() {
        return 500;
    }
}
