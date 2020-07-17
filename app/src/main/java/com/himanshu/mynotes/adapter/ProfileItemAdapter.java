package com.himanshu.mynotes.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(holder.ItemText, "title_transition");
                    pairs[1] = new Pair<View, String>(holder.ItemArrow, "arrow_transition");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
                    context.startActivity(i, options.toBundle());
                } else if (position == 1) {
                    Intent i2 = new Intent(context, ArchiveActivity2.class);
                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(holder.ItemText, "title_transition");
                    pairs[1] = new Pair<View, String>(holder.ItemArrow, "arrow_transition");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
                    context.startActivity(i2, options.toBundle());
                } else if (position == 2) {
                    Intent i3 = new Intent(context, DeleteActivity.class);
                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(holder.ItemText, "title_transition");
                    pairs[1] = new Pair<View, String>(holder.ItemArrow, "arrow_transition");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
                    context.startActivity(i3, options.toBundle());
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
        Button ItemArrow;
        public ProfileItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemImage = itemView.findViewById(R.id.item_profile_icon);
            ItemText = itemView.findViewById(R.id.item_profile_title);
            ItemArrow = itemView.findViewById(R.id.item_profile_arrow);
        }
    }
}