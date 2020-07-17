package com.himanshu.mynotes.fragment;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.mynotes.DashBoardActivity;
import com.himanshu.mynotes.EditNoteActivity;
import com.himanshu.mynotes.MainActivity;
import com.himanshu.mynotes.R;
import com.himanshu.mynotes.animation.CustomItemAnimation;
import com.himanshu.mynotes.model.Notes;
import com.himanshu.mynotes.viewHolder.NoteViewHolder;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends Fragment {


    private static final int NUM_COLUMNS = 2;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private ImageView ProfileImage;
    private RecyclerView recyclerView;
    private TextView CurrentUserName;
    private String currentTime = "";
    int currentColor = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference()
                .child(auth.getCurrentUser().getUid()).child("noteList");


        checkAnyNoteIsAvailable();

        retrieveCurrentUserInfo();

//        recyclerViewShow();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        CurrentUserName = (TextView) view.findViewById(R.id.dashboard_name);
        ProfileImage = (ImageView) view.findViewById(R.id.dashboard_profile_image);
        recyclerView = (RecyclerView) view.findViewById(R.id.dashboard_recyclerView);

        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewShow();
        return view;
    }

    public void checkAnyNoteIsAvailable() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(getActivity(), "No Notes Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void retrieveCurrentUserInfo() {

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            currentTime = "Good morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            currentTime = "Good afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            currentTime = "Good evening";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            currentTime = "Good night";
        }

        final String finalTime = currentTime;
        FirebaseDatabase.getInstance().getReference().child(auth.getCurrentUser().getUid()).child("userDetails")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            CurrentUserName.setText("Hello " + snapshot.child("name").getValue().toString() + ",\n" + currentTime);
                            Picasso.with(getActivity()).load(snapshot.child("photoUrl").getValue().toString())
                                    .placeholder(R.drawable.profilemale).into(ProfileImage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void recyclerViewShow() {

        Query query = reference;

        FirebaseRecyclerOptions<Notes> options =
                new FirebaseRecyclerOptions.Builder<Notes>()
                        .setQuery(query, Notes.class)
                        .build();

        final FirebaseRecyclerAdapter<Notes, NoteViewHolder> adapter =
                new FirebaseRecyclerAdapter<Notes, NoteViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position, @NonNull final Notes model) {

                        String getColor = model.getTileColor();

                        switch (getColor) {
                            case "1":
                                currentColor = R.color.color_one;
                                break;
                            case "2":
                                currentColor = R.color.color_two;
                                break;
                            case "3":
                                currentColor = R.color.color_three;
                                break;
                            case "4":
                                currentColor = R.color.color_four;
                                break;
                            case "5":
                                currentColor = R.color.color_five;
                                break;
                            case "6":
                                currentColor = R.color.color_six;
                                break;
                            case "7":
                                currentColor = R.color.color_seven;
                                break;
                            case "8":
                                currentColor = R.color.color_eight;
                                break;
                        }

                        holder.Description.setText(model.getNoteDesc());
                        holder.Date.setText(model.getTimeOfCreation());
                        holder.cardView.setCardBackgroundColor(getResources().getColor(currentColor));
                        if (model.getNoteTitle().equals("")) {
                            holder.Title.setVisibility(View.INVISIBLE);
                        } else if (!model.getNoteTitle().equals("")) {
                            holder.Title.setVisibility(View.VISIBLE);
                            holder.Title.setText(model.getNoteTitle());
                        }

                        if (model.getIsPinned().equals("true")) {
                            holder.Pin.setVisibility(View.VISIBLE);
                        } else if (model.getIsPinned().equals("false")) {
                            holder.Pin.setVisibility(View.INVISIBLE);
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), EditNoteActivity.class);
                                intent.putExtra("type", "editNote");
                                intent.putExtra("nid", model.getNid());
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

//                                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                                vibrator.vibrate(100);
                                popUpDialogForNote(model.getNoteTitle(), model.getNoteDesc(), model.getTimeOfCreation(), currentColor, model.getNid());

                                return false;
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_grid, parent, false);
                        return new NoteViewHolder(view);
                    }
                };

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new CustomItemAnimation());
        adapter.startListening();
        adapter.notifyItemInserted(1);
        adapter.notifyItemRemoved(1);
    }

    public void popUpDialogForNote(final String title, final String description, String date, int bgColor, final String nid) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_long_press_note);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView TitleTxt = dialog.findViewById(R.id.dialog_long_press_title);
        TextView DescriptionTxt = dialog.findViewById(R.id.dialog_long_press_description);
        TextView DateTxt = dialog.findViewById(R.id.dialog_long_press_date);
        Button DeleteBtn = dialog.findViewById(R.id.dialog_long_press_delete_btn);
        Button ArchiveBtn = dialog.findViewById(R.id.dialog_long_press_archive_btn);
        Button CopyBtn = dialog.findViewById(R.id.dialog_long_press_copy_btn);
        Button PinBtn = dialog.findViewById(R.id.dialog_long_press_pin_btn);
        CardView cardView = dialog.findViewById(R.id.dialog_long_press_cardView);

        TitleTxt.setText(title);
        DescriptionTxt.setText(description);
        DateTxt.setText(date);
        cardView.setCardBackgroundColor(getResources().getColor(bgColor));

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference fromReference = FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("noteList").child(nid);
                final DatabaseReference toReference = FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("deletedNotes").child(nid);

                fromReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (Objects.requireNonNull(snapshot.child("isPinned").getValue()).equals("false")) {
                            ValueEventListener valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    toReference.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()) {
                                                fromReference.removeValue();
                                                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            };
                            fromReference.addListenerForSingleValueEvent(valueEventListener);
                        } else if (snapshot.child("isPinned").getValue().equals("true")) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "For delete this note ypu have to unpin first", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        ArchiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference fromReference = FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("noteList").child(nid);
                final DatabaseReference toReference = FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("archivedNotes").child(nid);

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        toReference.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    fromReference.removeValue();
                                    Toast.makeText(getActivity(), "Archived", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                fromReference.addListenerForSingleValueEvent(valueEventListener);
            }
        });

        CopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = title + "\n" + description;
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("Note", text);
//                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Note Copied", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        PinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference pinReference = FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("noteList").child(nid);

                pinReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String checkPin = "";
                            checkPin = snapshot.child("isPinned").getValue().toString();
                            if (checkPin.equals("true")) {
                                snapshot.getRef().child("isPinned").setValue("false");
                            } else if (checkPin.equals("false")) {
                                snapshot.getRef().child("isPinned").setValue("true");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void logOutAccountBtn(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
}