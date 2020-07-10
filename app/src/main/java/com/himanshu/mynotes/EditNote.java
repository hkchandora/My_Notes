package com.himanshu.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditNote extends AppCompatActivity {

    private String nid = "";
    private String type = "";
    private TextView ActivityTitle;
    private EditText Title, Description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        ActivityTitle = findViewById(R.id.activity_title);
        Title = findViewById(R.id.edit_note_title);
        Description = findViewById(R.id.edit_note_description);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().getString("type");
        }

        if(type.equals("editNote")){
            ForEditNoteActivity();
        } else if(type.equals("addNote")){
            ForAddNoteActivity();
        }
    }

    public void ForEditNoteActivity(){
        ActivityTitle.setText("Edit Note");
        nid = getIntent().getExtras().getString("nid");
        Toast.makeText(this, nid, Toast.LENGTH_SHORT).show();
    }

    public void ForAddNoteActivity(){
        ActivityTitle.setText("Add Note");
    }

    public void BackPress(View view){
        finish();
    }
}

