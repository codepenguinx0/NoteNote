package com.teampenguin.apps.notenote.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class SharedPreferencesHelper {

    public static final String SHARE_PREFERENCE_NAME = "NoteNoteSP";
    public static final String SP_NOTE_CATEGORY = "noteCategories";

    public static boolean addNewNoteCategory(Context context, String newCategoryName)
    {
        SharedPreferences sp = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(sp!=null)
        {
            Set<String> savedCategories = sp.getStringSet(SP_NOTE_CATEGORY, new HashSet<>());

            if(savedCategories.contains(newCategoryName))
            {
                Toast.makeText(context, "This category already exists", Toast.LENGTH_SHORT).show();
                return false;
            }

            savedCategories.add(newCategoryName);
            return sp.edit().putStringSet(SP_NOTE_CATEGORY, savedCategories).commit();
        }

        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        return false;
    }

    public static Set<String> getNoteCategories(Context context)
    {
        Set<String> categories = new HashSet<>();
        SharedPreferences sp = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(sp!=null)
        {
            categories = sp.getStringSet(SP_NOTE_CATEGORY, new HashSet<>());
        }

        return categories;
    }
}
