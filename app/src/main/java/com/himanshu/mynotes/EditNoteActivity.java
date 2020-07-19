package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.mynotes.listeners.OnNoteColorClickListener;
import com.himanshu.mynotes.model.NoteColor;
import com.himanshu.mynotes.model.Notes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class EditNoteActivity extends AppCompatActivity {

    public static final String ACTION_TYPE = "action_type";
    public static final String ACTION_EDIT_NOTE = "edit_note";
    public static final String ACTION_CREATE_NOTE = "create_note";
    public static final String NOTE_DATA = "note_data";
    private String type = "";
    private EditText Title, Description;
    private DatabaseReference reference;
    private String NoteTitle = "", NoteDescription = "", NoteTileColor = "";
    private Notes noteModel;
    private TextView ToolBarTitle;
    private String storeNewTileColor="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);


        Title = findViewById(R.id.edit_note_title);
        Description = findViewById(R.id.edit_note_description);
        ToolBarTitle = findViewById(R.id.tool_bar_title);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        reference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("noteList");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().getString(ACTION_TYPE);
        }

        assert type != null;
        if (type.equals(ACTION_EDIT_NOTE)) {
            forEditNoteActivity();
        } else if (type.equals(ACTION_CREATE_NOTE)) {
            noteModel = new Notes();
            noteModel.setNoteId(FirebaseDatabase.getInstance().getReference().push().getKey());
            forAddNoteActivity();
        }

        RecyclerView recyclerView = findViewById(R.id.colors_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);

        List<NoteColor> colors = AppsPrefs.getInstance(this).getColorsList();
        if (colors == null || colors.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            ColorListAdapter adapter = new ColorListAdapter(colors, listener);
            if (type.equals(ACTION_CREATE_NOTE)) {
                noteModel.setTileColor(colors.get(0).getColor());
                colors.get(0).setSelected(true);
            } else {
                for (int i = 0; i < colors.size(); i++) {
                    NoteColor color = colors.get(i);
                    if (color.getColor().equals(noteModel.getTileColor())) {
                        color.setSelected(true);
                        adapter.selectedColorPosition = i;
                        break;
                    }
                }
            }
            recyclerView.setAdapter(adapter);
        }

        if (noteModel.getTileColor() != null && !noteModel.getTileColor().isEmpty()) {
            changeColor(noteModel.getTileColor());
        }

    }

    public void forEditNoteActivity() {
        ToolBarTitle.setText("EditText");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            noteModel = getIntent().getParcelableExtra(NOTE_DATA);
            if (noteModel == null) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        Title.setText(noteModel.getNoteTitle());
        Description.setText(noteModel.getNoteDesc());
    }

    public void forAddNoteActivity() {
        ToolBarTitle.setText("Add Note");
    }

    public void backPress(View view) {
        onBackPressed();
    }

    public void saveNoteInfo() {
        if (type.equals(ACTION_EDIT_NOTE)) {

            NoteTitle = Title.getText().toString();
            NoteDescription = Description.getText().toString();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
            String saveCurrentDate = currentDate.format(calendar.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
            String saveCurrentTime = currentTime.format(calendar.getTime());
            final String editTime = saveCurrentDate + " " + saveCurrentTime;


//            List<NoteColor> colors = AppsPrefs.getInstance(this).getColorsList();
//            ColorListAdapter adapter = new ColorListAdapter(colors, listener);
//            for (int i = 0; i < colors.size(); i++) {
//                NoteColor color = colors.get(i);
//                if (color.isSelected()) {
//
//                    int storeNewTileColor = color.getPosition();
//
//                    Toast.makeText(this, "position"+storeNewTileColor, Toast.LENGTH_SHORT).show();
//
//                    adapter.getItemId(i);
////                    color.setSelected(true);
////                    adapter.selectedColorPosition = i;
//                    break;
//                }
//            }

            reference.child(noteModel.getNoteId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().child("noteTitle").setValue(NoteTitle);
                    snapshot.getRef().child("noteDesc").setValue(NoteDescription);
                    snapshot.getRef().child("lastEditTime").setValue(editTime);
//                    snapshot.getRef().child("tileColor").setValue(storeNewTileColor);
                    snapshot.getRef().child("lastEditedTimeStamp").setValue(System.currentTimeMillis());
                    Toast.makeText(EditNoteActivity.this, "Change Saved", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else if (type.equals(ACTION_CREATE_NOTE)) {
            String titleTxt = Title.getText().toString().trim();
            String descriptionTxt = Description.getText().toString().trim();

            if (TextUtils.isEmpty(titleTxt) && TextUtils.isEmpty(descriptionTxt)) {
                finish();
            } else if (TextUtils.isEmpty(descriptionTxt) && !TextUtils.isEmpty(titleTxt)) {
                Description.setError("Required");
            } else {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
                String saveCurrentTime = currentTime.format(calendar.getTime());
                String noteId = saveCurrentDate + " " + saveCurrentTime;

                noteModel.setNoteTitle(titleTxt);
                noteModel.setNoteId(noteId);
                noteModel.setNoteDesc(descriptionTxt);
                noteModel.setTimeOfCreation(saveCurrentDate);
                noteModel.setLastEditTime(saveCurrentDate);
                noteModel.setIsPinned(false);
                long timeStamp = System.currentTimeMillis();
                noteModel.setCreatedTimeStamp(timeStamp);
                noteModel.setLastEditedTimeStamp(timeStamp);
//                notes.setTileColor(selectedCardBgColor);TODO
                reference.child(noteId).setValue(noteModel);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        saveNoteInfo();
        super.onBackPressed();
    }

    private OnNoteColorClickListener listener = color -> {
        noteModel.setTileColor(color);
        changeColor(color);
    };

    //change background color of title and description
    private void changeColor(String color) {
        Title.setBackgroundColor(Color.parseColor(color));
        Description.setBackgroundColor(Color.parseColor(color));
    }

}

