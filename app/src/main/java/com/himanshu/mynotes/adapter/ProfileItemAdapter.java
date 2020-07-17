package com.himanshu.mynotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.himanshu.mynotes.ArchiveActivity2;
import com.himanshu.mynotes.DeleteActivity;
import com.himanshu.mynotes.PinActivity;
import com.himanshu.mynotes.R;

public class ProfileItemAdapter extends RecyclerView.Adapter<ProfileItemAdapter.ProfileItemViewHolder> {

    private Context context;
    private String[] data;
    private int[] image;

    public ProfileItemAdapter(Context context, String[] data, int[] image) {
        this.context = context;
        this.data = data;
        this.image = image;
    }

    @NonNull
    @Override
    public ProfileItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_profile, parent, false);
        return new ProfileItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileItemViewHolder holder, final int position) {
        String Title = data[position];
        int TitleImage = image[position];
        holder.ItemText.setText(Title);
        holder.ItemImage.setImageResource(TitleImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    Intent i = new Intent(context, PinActivity.class);
                    ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                            holder.ItemText, "title_transition");
                    context.startActivity(i, option.toBundle());
                } else if (position == 1) {
                    Intent i2 = new Intent(context, ArchiveActivity2.class);
                    context.startActivity(i2);
                } else if (position == 2) {
                    Intent i3 = new Intent(context, DeleteActivity.class);
                    context.startActivity(i3);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ProfileItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ItemImage;
        TextView ItemText;

        public ProfileItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemImage = itemView.findViewById(R.id.item_profile_icon);
            ItemText = itemView.findViewById(R.id.item_profile_title);
        }
    }
}