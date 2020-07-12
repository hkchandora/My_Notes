package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Title = findViewById(R.id.edit_note_title);
        Description = findViewById(R.id.edit_note_description);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackPress();
            }
        });

        notes = new Notes();

        reference = FirebaseDatabase.getInstance().getReference().child("Notes")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().getString("type");
        }

        assert type != null;
        if (type.equals("editNote")) {
            ForEditNoteActivity();
        } else if (type.equals("addNote")) {
            ForAddNoteActivity();
        }
    }

    public void ForEditNoteActivity() {
        getSupportActionBar().setTitle("Edit Note");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            nid = getIntent().getExtras().getString("nid");
        }

        reference.child("Note").child(nid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    NoteTitle = snapshot.child("title").getValue().toString();
                    NoteDescription = snapshot.child("description").getValue().toString();

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

    public void ForAddNoteActivity() {
        getSupportActionBar().setTitle("Add Note");
    }

    public void BackPress() {
        if (type.equals("editNote")) {

            NoteTitle = Title.getText().toString();
            NoteDescription = Description.getText().toString();

            reference.child("Note").child(nid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().child("title").setValue(NoteTitle);
                    snapshot.getRef().child("description").setValue(NoteDescription);
                    Toast.makeText(EditNoteActivity.this, "Change Saved", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            finish();
        }
        else if (type.equals("addNote")) {
            String titleTxt = Title.getText().toString().trim();
            String descriptionTxt = Description.getText().toString().trim();

            if (TextUtils.isEmpty(titleTxt) && TextUtils.isEmpty(descriptionTxt)) {
                finish();
            } else if (TextUtils.isEmpty(descriptionTxt) && !TextUtils.isEmpty(titleTxt)) {
                Description.setError("Required");
            } else if (TextUtils.isEmpty(titleTxt) && !TextUtils.isEmpty(descriptionTxt)) {
                Title.setError("Required");
            } else {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
                String saveCurrentTime = currentTime.format(calendar.getTime());
                String noteId = saveCurrentDate + " " + saveCurrentTime;

                notes.setNid(noteId);
                notes.setTitle(titleTxt);
                notes.setDescription(descriptionTxt);
                notes.setDate(saveCurrentDate);
                notes.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                reference.child("Note").child(noteId).setValue(notes);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

