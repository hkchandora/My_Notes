package com.himanshu.mynotes.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Vibrator;
import android.util.Log;
import android.util.Pair;
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
import com.himanshu.mynotes.AppsPrefs;
import com.himanshu.mynotes.EditNoteActivity;
import com.himanshu.mynotes.EditProfileActivity;
import com.himanshu.mynotes.FirebaseRepository;
import com.himanshu.mynotes.MainActivity;
import com.himanshu.mynotes.R;
import com.himanshu.mynotes.animation.CustomItemAnimation;
import com.himanshu.mynotes.listeners.OnFetchColorsListener;
import com.himanshu.mynotes.model.NoteColor;
import com.himanshu.mynotes.model.Notes;
import com.himanshu.mynotes.util.CryptoUtil;
import com.himanshu.mynotes.viewHolder.NoteViewHolder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class DashBoardFragment extends Fragment {

    private static final String TAG = "DashBoardFragment";

    private static final int NUM_COLUMNS = 2;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private ImageView ProfileImage;
    private RecyclerView recyclerView;
    private TextView CurrentUserName;
    private String currentTime = "";
    private CardView addNoteCard;
    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("notes")
                .child(auth.getCurrentUser().getUid());
    }

    @Override
    public void onStart() {
        super.onStart();

        checkAnyNoteIsAvailable();
        retrieveCurrentUserInfo();
        fetchColors();
    }

    private void fetchColors() {
        FirebaseRepository.getInstance().fetchColors(new OnFetchColorsListener() {
            @Override
            public void onSuccess(List<NoteColor> colorsList) {
                AppsPrefs.getInstance(requireActivity()).saveColorsList(colorsList);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d(TAG, "onFailure: " + errorMessage);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);

        addNoteCard = view.findViewById(R.id.add_new_note_card);

        CurrentUserName = view.findViewById(R.id.dashboard_name);
        ProfileImage = (ImageView) view.findViewById(R.id.dashboard_profile_image);

        recyclerView = (RecyclerView) view.findViewById(R.id.dashboard_recyclerView);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewShow("");

        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.toString() != null) {
//                    try {
//                        Notes notes = new Notes();
//                        newText = new CryptoUtil().encrypt(notes.getNoteId(), newText);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    recyclerViewShow(newText.toString());
                } else {
                    recyclerViewShow("");
                }
                return false;
            }
        });
        return view;
    }

    public void checkAnyNoteIsAvailable() {
        addNoteCard.setVisibility(View.GONE);
        reference.child("noteList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {

                    addNoteCard.setVisibility(View.VISIBLE);
                    addNoteCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), EditNoteActivity.class);
                            i.putExtra(EditNoteActivity.ACTION_TYPE, EditNoteActivity.ACTION_CREATE_NOTE);
                            startActivity(i);
                        }
                    });
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

        if (timeOfDay >= 4 && timeOfDay < 12) {
            currentTime = "Good morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            currentTime = "Good afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            currentTime = "Good evening";
        } else if ((timeOfDay >= 0 && timeOfDay < 4) || (timeOfDay >= 21 && timeOfDay < 24)) {
            currentTime = "Good night";
        }

        FirebaseDatabase.getInstance().getReference().child("userDetails").child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String name = snapshot.child("name").getValue().toString();
                            CurrentUserName.setText("Hello " + name.substring(0, name.indexOf(' ')).trim() + ",\n" + currentTime);
                            if (!snapshot.child("photoUrl").getValue().toString().equals("")) {
                                Picasso.with(ProfileImage.getContext()).load(snapshot.child("photoUrl").getValue().toString())
                                        .placeholder(R.drawable.profilemale).into(ProfileImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void recyclerViewShow(String data) {

//        String dataEncrypt = "";
//        Notes notes = new Notes();
//
//        try {
//            data = new CryptoUtil().encrypt(notes.getNoteId(), data);

        Query query = reference.child("noteList").orderByChild("noteDesc").startAt(data).endAt(data + "\uf8ff");

        FirebaseRecyclerOptions<Notes> options =
                new FirebaseRecyclerOptions.Builder<Notes>()
                        .setQuery(query, Notes.class)
                        .build();

        final FirebaseRecyclerAdapter<Notes, NoteViewHolder> adapter =
                new FirebaseRecyclerAdapter<Notes, NoteViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position, @NonNull final Notes model) {

                        if (model.getNoteTitle() != null && !model.getNoteTitle().isEmpty()) {
                            try {
                                String decryptedText = new CryptoUtil().decrypt(model.getNoteId(), model.getNoteTitle());
                                model.setNoteTitle(decryptedText);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (model.getNoteDesc() != null && !model.getNoteDesc().isEmpty()) {
                            try {
                                String decryptedText = new CryptoUtil().decrypt(model.getNoteId(), model.getNoteDesc());
                                model.setNoteDesc(decryptedText);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        holder.Description.setText(model.getNoteDesc());
                        holder.Date.setText(model.getTimeOfCreation());
                        holder.cardView.setCardBackgroundColor(Color.parseColor(model.getTileColor()));
                        if (model.getNoteTitle().equals("")) {
                            holder.Title.setVisibility(View.GONE);
                        } else if (!model.getNoteTitle().equals("")) {
                            holder.Title.setVisibility(View.VISIBLE);
                            holder.Title.setText(model.getNoteTitle());
                        }

                        if (model.getIsPinned()) {
                            holder.Pin.setVisibility(View.VISIBLE);
                        } else if (!model.getIsPinned()) {
                            holder.Pin.setVisibility(View.INVISIBLE);
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(getActivity(), EditNoteActivity.class);
                                Pair[] pairs = new Pair[2];
                                pairs[0] = new Pair<View, String>(holder.Title, "note_title");
                                pairs[1] = new Pair<View, String>(holder.Description, "note_description");
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                                intent.putExtra(EditNoteActivity.ACTION_TYPE, EditNoteActivity.ACTION_EDIT_NOTE);
                                intent.putExtra(EditNoteActivity.FROM_ACTIVITY, EditNoteActivity.DASHBOARD);
                                intent.putExtra(EditNoteActivity.NOTE_DATA, model);
                                startActivity(intent, options.toBundle());
                            }
                        });

                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Vibrator vb = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                vb.vibrate(35);
                                popUpDialogForNote(model);

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
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void popUpDialogForNote(final Notes note) {

        final Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.dialog_long_press_note);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView TitleTxt = dialog.findViewById(R.id.dialog_long_press_title);
        TextView DescriptionTxt = dialog.findViewById(R.id.dialog_long_press_description);
        TextView DateTxt = dialog.findViewById(R.id.dialog_long_press_date);
        ImageView DeleteBtn = dialog.findViewById(R.id.dialog_long_press_delete_btn);
        ImageView ArchiveBtn = dialog.findViewById(R.id.dialog_long_press_archive_btn);
        ImageView CopyBtn = dialog.findViewById(R.id.dialog_long_press_copy_btn);
        ImageView PinBtn = dialog.findViewById(R.id.dialog_long_press_pin_btn);
        CardView cardView = dialog.findViewById(R.id.dialog_long_press_cardView);

        TitleTxt.setText(note.getNoteTitle());
        DescriptionTxt.setText(note.getNoteDesc());
        DateTxt.setText(note.getTimeOfCreation());
        cardView.setCardBackgroundColor(Color.parseColor(note.getTileColor()));

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setIsPinned(false);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
                final String deletedDate = currentDate.format(calendar.getTime());

                final DatabaseReference fromReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("noteList").child(note.getNoteId());
                final DatabaseReference toReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("deletedNotes").child(note.getNoteId());

                fromReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                toReference.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isComplete()) {
                                            fromReference.removeValue();
                                            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();

                                            toReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        snapshot.child("deletedDate").getRef().setValue(deletedDate);
                                                        snapshot.child("lastEditTime").getRef().removeValue();
                                                        snapshot.child("deletedFrom").getRef().setValue("dashboard");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        } else {
                                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };
                        fromReference.addListenerForSingleValueEvent(valueEventListener);
                        dialog.dismiss();
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
                final DatabaseReference fromReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("noteList").child(note.getNoteId());
                final DatabaseReference toReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("archivedNotes").child(note.getNoteId());

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
                String text = note.getNoteTitle() + "\n" + note.getNoteDesc();
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Note", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Note Copied", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        PinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference pinReference = FirebaseDatabase.getInstance().getReference().child("notes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("noteList").child(note.getNoteId());

                pinReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            boolean checkPin;
                            checkPin = (boolean) snapshot.child("isPinned").getValue();
                            if (checkPin) {
                                snapshot.getRef().child("isPinned").setValue(false);
                            } else {
                                snapshot.getRef().child("isPinned").setValue(true);
                            }
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        dialog.show();
    }

}