package com.himanshu.mynotes.listeners;

import com.himanshu.mynotes.model.NoteColor;

import java.util.List;

/**
 * Created by Kalpesh on 14/07/20.
 */
public interface OnFetchColorsListener {
    void onSuccess(List<NoteColor> colorsList);

    void onFailure(String errorMessage);
}
