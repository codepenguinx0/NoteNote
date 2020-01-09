package com.teampenguin.apps.notenote.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

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
//        SharedPreferences sp = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(sp!=null)
        {
            SharedPreferences.Editor editor = sp.edit();

            Set<String> savedCategories = sp.getStringSet(SP_NOTE_CATEGORY, new HashSet<>());
            Set<String> temp = new HashSet<>(savedCategories);

            Log.d(TAG, "addNewNoteCategory: savedCategories.size()" + savedCategories.size());

            if(savedCategories.contains(newCategoryName))
            {
//                Toast.makeText(context, "This category already exists", Toast.LENGTH_SHORT).show();
                return false;
            }

            temp.add(newCategoryName);
            Log.d(TAG, "addNewNoteCategory: NEW savedCategories.size()" + temp.size());
            editor.remove(SP_NOTE_CATEGORY);
            editor.putStringSet(SP_NOTE_CATEGORY, temp);
            editor.apply();
//            editor.commit();
            return true;
        }

//        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        return false;
    }

    public static boolean deleteNoteCategory(String category)
    {
//        SharedPreferences sp = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(sp!=null)
        {
            SharedPreferences.Editor editor = sp.edit();

            Set<String> savedCategories = sp.getStringSet(SP_NOTE_CATEGORY, new HashSet<>());
            Set<String> temp = new HashSet<>(savedCategories);

            if(temp.remove(category))
            {
                editor.remove(SP_NOTE_CATEGORY);
                editor.putStringSet(SP_NOTE_CATEGORY, temp);
                editor.apply();
//                editor.commit();
                return true;
//                return sp.edit().putStringSet(SP_NOTE_CATEGORY, savedCategories).commit();
            }
        }

        return false;
    }

    public static Set<String> getNoteCategories()
    {
        Set<String> categories = new HashSet<>();
//        SharedPreferences sp = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(sp!=null)
        {
            categories = sp.getStringSet(SP_NOTE_CATEGORY, new HashSet<>());
            Log.d(TAG, "getNoteCategories: categories.size() " + categories.size());
        }

        return categories;
    }

    //endregion

}
