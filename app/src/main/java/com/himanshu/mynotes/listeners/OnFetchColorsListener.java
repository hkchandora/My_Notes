package com.himanshu.mynotes.listeners;

import java.util.List;

/**
 * Created by Kalpesh on 14/07/20.
 */
public interface OnFetchColorsListener {
    void onSuccess(List<String> colorsList);

    void onFailure(String errorMessage);
}
