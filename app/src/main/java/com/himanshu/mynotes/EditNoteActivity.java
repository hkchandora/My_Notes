package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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
import com.himanshu.mynotes.model.Notes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditNoteActivity extends AppCompatActivity {

    private String nid = "";
    private String type = "";
    private EditText Title, Description;
    private DatabaseReference reference;
    private String NoteTitle = "", NoteDescription = "";
    private Notes notes;
    private TextView ToolBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Title = findViewById(R.id.edit_note_title);
        Description = findViewById(R.id.edit_note_description);
        ToolBarTitle = findViewById(R.id.tool_bar_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        notes = new Notes();

        reference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("noteList");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().getString("type");
        }

        assert type != null;
        if (type.equals("editNote")) {
            forEditNoteActivity();
        } else if (type.equals("addNote")) {
            forAddNoteActivity();
        }
    }

    public void forEditNoteActivity() {
        ToolBarTitle.setText("EditText");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            nid = getIntent().getExtras().getString("nid");
        }

        reference.child(nid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    NoteTitle = snapshot.child("noteTitle").getValue().toString();
                    NoteDescription = snapshot.child("noteDesc").getValue().toString();

                    Title.setText(NoteTitle);
                    Description.setText(NoteDescription);
                } else {
                    Toast.makeText(EditNoteActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void forAddNoteActivity() {
        ToolBarTitle.setText("Add Note");
    }

    public void backPress(View view) {
        onBackPressed();
    }

    public void saveNoteInfo() {
        if (type.equals("editNote")) {

            NoteTitle = Title.getText().toString();
            NoteDescription = Description.getText().toString();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
            String saveCurrentDate = currentDate.format(calendar.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
            String saveCurrentTime = currentTime.format(calendar.getTime());
            final String editTime = saveCurrentDate + " " + saveCurrentTime;

            reference.child(nid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().child("noteTitle").setValue(NoteTitle);
                    snapshot.getRef().child("noteDesc").setValue(NoteDescription);
                    snapshot.getRef().child("lastEditTime").setValue(editTime);
                    Toast.makeText(EditNoteActivity.this, "Change Saved", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            finish();
        } else if (type.equals("addNote")) {
            String titleTxt = Title.getText().toString().trim();
            String descriptionTxt = Description.getText().toString().trim();

            if (TextUtils.isEmpty(titleTxt) && TextUtils.isEmpty(descriptionTxt)) {
                finish();
            } else if (TextUtils.isEmpty(descriptionTxt) && !TextUtils.isEmpty(titleTxt)) {
                Description.setError("Required");
            }
//            else if (TextUtils.isEmpty(titleTxt) && !TextUtils.isEmpty(descriptionTxt)) {
//                Title.setError("Required");
//            }
            else {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
                String saveCurrentTime = currentTime.format(calendar.getTime());
                String noteId = saveCurrentDate + " " + saveCurrentTime;

                notes.setNid(noteId);
                if (titleTxt.equals(null)) {
                    notes.setNoteTitle("");
                } else {
                    notes.setNoteTitle(titleTxt);
                }
                notes.setNoteDesc(descriptionTxt);
                notes.setTimeOfCreation(saveCurrentDate);
                notes.setLastEditTime("");
                notes.setIsPinned("false");
                notes.setTileColor("");
                reference.child(noteId).setValue(notes);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNoteInfo();
    }

}

