package com.himanshu.mynotes.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.himanshu.mynotes.AboutActivity;
import com.himanshu.mynotes.ArchiveActivity;
import com.himanshu.mynotes.DeleteActivity;
import com.himanshu.mynotes.MainActivity;
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
                    Intent i2 = new Intent(context, ArchiveActivity.class);
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
                } else if (position == 3) {
                    Intent i3 = new Intent(context, AboutActivity.class);
                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(holder.ItemText, "title_transition");
                    pairs[1] = new Pair<View, String>(holder.ItemArrow, "arrow_transition");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
                    context.startActivity(i3, options.toBundle());
                } else if (position == 4) {

                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_logout);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button logOutBtn = dialog.findViewById(R.id.dialog_log_out_btn);
                    Button cancelBtn = dialog.findViewById(R.id.dialog_cancel_btn);
                    dialog.show();
                    logOutBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            AuthUI.getInstance().signOut(context)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Intent i4 = new Intent(context, MainActivity.class);
                                                i4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                i4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(i4);
                                                ((AppCompatActivity) context).finish();
                                            } else{
                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
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