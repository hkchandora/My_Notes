package com.himanshu.mynotes;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.mynotes.listeners.OnFetchColorsListener;

import java.util.Collections;
import java.util.List;

/**
 * Created by Kalpesh on 14/07/20.
 */
class FirebaseRepository {

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
        FirebaseDatabase.getInstance().getReference("colorList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        listener.onSuccess((List<String>) snapshot.getValue());
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
