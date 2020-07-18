package com.himanshu.mynotes;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Snapshot;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.mynotes.listeners.OnFetchColorsListener;
import com.himanshu.mynotes.model.NoteColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kalpesh on 14/07/20.
 */
public class FirebaseRepository {

    private static FirebaseRepository sInstance;

    private FirebaseRepository() {
    }

    public static FirebaseRepository getInstance() {
        if (sInstance == null) {
            sInstance = new FirebaseRepository();
        }
        return sInstance;
    }

    public void fetchColors(final OnFetchColorsListener listener) {
        FirebaseDatabase.getInstance().getReference("colorsList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        List<NoteColor> list = new ArrayList<>();
                        for (DataSnapshot s : snapshot.getChildren()) {
                            list.add(s.getValue(NoteColor.class));
                        }
                        listener.onSuccess(list);
                    } catch (Exception e) {
                        listener.onFailure(e.getMessage());
                    }
                } else {
                    listener.onFailure("Failed to get data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure(error.getMessage());
            }
        });
    }

}
