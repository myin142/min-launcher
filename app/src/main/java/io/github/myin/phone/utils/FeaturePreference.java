package io.github.myin.phone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import io.github.myin.phone.SharedConst;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class FeaturePreference {

    private SharedPreferences preferences;
    private Set<String> features;

    public void init(Context ctx) {
        preferences = getSharedPreferences(ctx);
        features = preferences.getStringSet(SharedConst.PREF_ENABLED_FEATURES, new HashSet<>());
    }

    private SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences(SharedConst.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public Set<String> getFeatures() {
        if (features == null) {
            throw new RuntimeException("Features not initialized.");
        }
        return features;
    }

    public boolean isFeatureEnabled(String feature) {
        return getFeatures().contains(feature);
    }

    public void toggleFeature(String feature, boolean enable) {
        if (enable) {
            features.add(feature);
        } else {
            features.remove(feature);
        }

        saveFeatureSettings(features);
    }

    private void saveFeatureSettings(Set<String> enabledFeatures) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(SharedConst.PREF_ENABLED_FEATURES, enabledFeatures);
        editor.apply();
    }

}
