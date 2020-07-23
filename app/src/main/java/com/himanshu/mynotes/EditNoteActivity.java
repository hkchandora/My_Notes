package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private String NoteTitle = "", NoteDescription = "", NoteTileColor = "";
    private Notes notes;
    private TextView ToolBarTitle;
    private String selectedCardBgColor;
    private RadioGroup ColorPanelGroup;
    private RadioButton One, Two, Three, Four, Five, Six, Seven, Eight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);


        ColorPanelGroup = findViewById(R.id.color_panel_group);
        One = findViewById(R.id.color_one_btn);
        Two = findViewById(R.id.color_two_btn);
        Three = findViewById(R.id.color_three_btn);
        Four = findViewById(R.id.color_four_btn);
        Five = findViewById(R.id.color_five_btn);
        Six = findViewById(R.id.color_six_btn);
        Seven = findViewById(R.id.color_seven_btn);
        Eight = findViewById(R.id.color_eight_btn);


        Title = findViewById(R.id.edit_note_title);
        Description = findViewById(R.id.edit_note_description);
        ToolBarTitle = findViewById(R.id.tool_bar_title);
        Toolbar toolbar =  findViewById(R.id.tool_bar);
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
                    NoteTileColor = snapshot.child("tileColor").getValue().toString();
                    Title.setText(NoteTitle);
                    Description.setText(NoteDescription);

                    switch (NoteTileColor) {
                        case "1":
                            One.setChecked(true);
                            break;
                        case "2":
                            Two.setChecked(true);
                            break;
                        case "3":
                            Three.setChecked(true);
                            break;
                        case "4":
                            Four.setChecked(true);
                            break;
                        case "5":
                            Five.setChecked(true);
                            break;
                        case "6":
                            Six.setChecked(true);
                            break;
                        case "7":
                            Seven.setChecked(true);
                            break;
                        case "8":
                            Eight.setChecked(true);
                            break;
                    }
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
        One.setChecked(true);
    }

    public void backPress(View view) {
        onBackPressed();
    }

    public void saveNoteInfo() {
        if (type.equals("editNote")) {

            NoteTitle = Title.getText().toString();
            NoteDescription = Description.getText().toString();

            RadioButton selectedRadioButton = null;
            selectedRadioButton = findViewById(ColorPanelGroup.getCheckedRadioButtonId());
            if (selectedRadioButton == One) {
                NoteTileColor = "1";
            } else if (selectedRadioButton == Two) {
                NoteTileColor = "2";
            } else if (selectedRadioButton == Three) {
                NoteTileColor = "3";
            } else if (selectedRadioButton == Four) {
                NoteTileColor = "4";
            } else if (selectedRadioButton == Five) {
                NoteTileColor = "5";
            } else if (selectedRadioButton == Six) {
                NoteTileColor = "6";
            } else if (selectedRadioButton == Seven) {
                NoteTileColor = "7";
            } else if (selectedRadioButton == Eight) {
                NoteTileColor = "8";
            }


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
                    snapshot.getRef().child("tileColor").setValue(NoteTileColor);
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
            } else {

                RadioButton selectedRadioButton = null;
                selectedRadioButton = findViewById(ColorPanelGroup.getCheckedRadioButtonId());
                if (selectedRadioButton == One) {
                    selectedCardBgColor = "1";
                } else if (selectedRadioButton == Two) {
                    selectedCardBgColor = "2";
                } else if (selectedRadioButton == Three) {
                    selectedCardBgColor = "3";
                } else if (selectedRadioButton == Four) {
                    selectedCardBgColor = "4";
                } else if (selectedRadioButton == Five) {
                    selectedCardBgColor = "5";
                } else if (selectedRadioButton == Six) {
                    selectedCardBgColor = "6";
                } else if (selectedRadioButton == Seven) {
                    selectedCardBgColor = "7";
                } else if (selectedRadioButton == Eight) {
                    selectedCardBgColor = "8";
                }

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
                String saveCurrentTime = currentTime.format(calendar.getTime());
                String noteId = saveCurrentDate + " " + saveCurrentTime;


                if (titleTxt.equals(null)) {
                    notes.setNoteTitle("");
                } else {
                    notes.setNoteTitle(titleTxt);
                }
                notes.setNid(noteId);
                notes.setNoteDesc(descriptionTxt);
                notes.setTimeOfCreation(saveCurrentDate);
                notes.setLastEditTime("");
                notes.setIsPinned("false");
                notes.setTileColor(selectedCardBgColor);
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

