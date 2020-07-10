package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.mynotes.Model.Notes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditNote extends AppCompatActivity {

    private String nid = "";
    private String type = "";
    private TextView ActivityTitle;
    private EditText Title, Description;
    private String SaveCurrentDate, SaveCurrentTime, TitleTxt, DescriptionTxt;
    private String NoteId = "";

    private DatabaseReference reference;
    private String NoteTitle = "", NoteDescription= "";
    private Notes notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        ActivityTitle = findViewById(R.id.activity_title);
        Title = findViewById(R.id.edit_note_title);
        Description = findViewById(R.id.edit_note_description);

        notes = new Notes();

        reference = FirebaseDatabase.getInstance().getReference().child("Notes")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().getString("type");
        }

        if (type.equals("editNote")) {
            ForEditNoteActivity();
        } else if (type.equals("addNote")) {
            ForAddNoteActivity();
        }
    }

    public void ForEditNoteActivity() {
        ActivityTitle.setText("Edit Note");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            nid = getIntent().getExtras().getString("nid");
        }

        assert nid != null;
        reference.child(nid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    NoteTitle = snapshot.child("title").getValue().toString();
                    NoteDescription = snapshot.child("description").getValue().toString();

//                    Toast.makeText(EditNote.this, NoteTitle+ NoteDescription, Toast.LENGTH_SHORT).show();
                    Title.setText(NoteTitle);
                    Description.setText(NoteDescription);

                } else {
                    Toast.makeText(EditNote.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void ForAddNoteActivity() {
        ActivityTitle.setText("Add Note");
    }

    public void BackPress(View view) {
        if (type.equals("editNote")) {

            NoteTitle = Title.getText().toString();
            NoteDescription = Description.getText().toString();

            reference.child(nid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().child("title").setValue(NoteTitle);
                    snapshot.getRef().child("description").setValue(NoteDescription);
                    Toast.makeText(EditNote.this, "Change Saved", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            finish();
        } else if (type.equals("addNote")) {
            TitleTxt = Title.getText().toString();
            DescriptionTxt = Description.getText().toString();

            if (TextUtils.isEmpty(TitleTxt)) {
                Title.setError("Required");
            } else if (TextUtils.isEmpty(DescriptionTxt)) {
                Description.setError("Required");
            } else {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
                SaveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
                SaveCurrentTime = currentTime.format(calendar.getTime());
                NoteId = SaveCurrentDate + " " + SaveCurrentTime;

                notes.setNid(NoteId);
                notes.setTitle(TitleTxt);
                notes.setDescription(DescriptionTxt);
                notes.setDate(SaveCurrentDate);
                notes.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                reference.child(NoteId).setValue(notes);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

