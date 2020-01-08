package com.teampenguin.apps.notenote.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharePerferenceHelper {

    public static final String SHARE_PREFERENCE_NAME = "NoteNoteSP";
    public static final String SP_NOTE_CATEGORY = "noteCategories";

    public static boolean addNewNoteCategory(Context context, String newCategoryName)
    {
        SharedPreferences sp = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(sp!=null)
        {
            Set<String> savedCategories = sp.getStringSet(SP_NOTE_CATEGORY, new HashSet<>());
            savedCategories.add(newCategoryName);
            return sp.edit().putStringSet(SP_NOTE_CATEGORY, savedCategories).commit();
        }

        return false;
    }
}
