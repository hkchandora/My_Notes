package com.himanshu.mynotes.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.himanshu.mynotes.R;

import java.util.ArrayList;
import java.util.Random;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "NoteRecyclerViewAdapter";
    private ArrayList<String> nTitle = new ArrayList<>();
    private ArrayList<String> nDescription = new ArrayList<>();
    private ArrayList<String> nDate = new ArrayList<>();
    private Context nContext;

    public NoteRecyclerViewAdapter(Context context, ArrayList<String> nTitle, ArrayList<String> nDescription, ArrayList<String> nDate) {
        this.nTitle = nTitle;
        this.nDescription = nDescription;
        this.nDate = nDate;
        this.nContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_grid_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: Called.");

        Random random = new Random();
        int currentColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        holder.cardView.setCardBackgroundColor(currentColor);

        holder.Title.setText(nTitle.get(position));
        holder.Description.setText(nDescription.get(position));
        holder.Date.setText(nDate.get(position));

    }

    @Override
    public int getItemCount() {
        return nTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView Title, Description, Date;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.Title = itemView.findViewById(R.id.note_item_title);
            this.Description = itemView.findViewById(R.id.note_item_description);
            this.Date = itemView.findViewById(R.id.note_item_date);
            this.cardView = itemView.findViewById(R.id.note_item_cardView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(nContext, "Single Click", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(nContext, "Long Pressed", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}