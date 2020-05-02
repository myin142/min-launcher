package io.github.myin.phone.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import io.github.myin.phone.SharedConst;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class PreferenceSettings {

    public Set<String> getFeatures(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences(SharedConst.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getStringSet(SharedConst.PREF_ENABLED_FEATURES, new HashSet<>());
    }

    public void saveFeatureSettings(Activity activity, Set<String> enabledFeatures) {
        SharedPreferences pref = activity.getSharedPreferences(SharedConst.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(SharedConst.PREF_ENABLED_FEATURES, enabledFeatures);
        editor.apply();
    }

}
