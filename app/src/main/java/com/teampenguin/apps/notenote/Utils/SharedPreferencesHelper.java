package com.teampenguin.apps.notenote.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class SharedPreferencesHelper {

    private static final String TAG = "SharedPreferencesHelper";

    private static final String SHARE_PREFERENCE_NAME = "NoteNoteSP";
    private static final String SP_NOTE_CATEGORY = "noteCategories";

    private static SharedPreferences sp = null;

    public static void init(Context context)
    {
        if(sp == null)
        {
            sp = context.getSharedPreferences(SHARE_PREFERENCE_NAME,Context.MODE_PRIVATE);
        }
    }

    //region Edit Note Category
    public static boolean addNewNoteCategory(String newCategoryName)
    {
        if(sp!=null)
        {
            SharedPreferences.Editor editor = sp.edit();

            HashSet<String> savedCategories = (HashSet<String>) sp.getStringSet(SP_NOTE_CATEGORY, new HashSet<String>());
            Set<String> temp = new HashSet<>(savedCategories);

            Log.d(TAG, "addNewNoteCategory: savedCategories.size()" + savedCategories.size());

            if(savedCategories.contains(newCategoryName))
            {
                return false;
            }

            temp.add(newCategoryName);
            Log.d(TAG, "addNewNoteCategory: NEW savedCategories.size()" + temp.size());
            editor.remove(SP_NOTE_CATEGORY);
            editor.putStringSet(SP_NOTE_CATEGORY, temp);
            editor.apply();
            return true;
        }

        return false;
    }

    public static boolean deleteNoteCategory(String category)
    {
        if(sp!=null)
        {
            SharedPreferences.Editor editor = sp.edit();

            HashSet<String> savedCategories = (HashSet<String>) sp.getStringSet(SP_NOTE_CATEGORY, new HashSet<String>());
            Set<String> temp = new HashSet<>(savedCategories);

            if(temp.remove(category))
            {
                editor.remove(SP_NOTE_CATEGORY);
                editor.putStringSet(SP_NOTE_CATEGORY, temp);
                editor.apply();
                return true;
            }
        }

        return false;
    }

    public static Set<String> getNoteCategories()
    {
        Set<String> categories = new HashSet<>();

        if(sp!=null)
        {
            categories = sp.getStringSet(SP_NOTE_CATEGORY, new HashSet<String>());
            Log.d(TAG, "getNoteCategories: categories.size() " + categories.size());
        }

        return categories;
    }

    //endregion

}
