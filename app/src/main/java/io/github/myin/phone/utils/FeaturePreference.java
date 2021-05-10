package io.github.myin.phone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import io.github.myin.phone.SharedConst;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class FeaturePreference {

    public enum LayoutDirection {
        LEFT(View.LAYOUT_DIRECTION_LTR),
        RIGHT(View.LAYOUT_DIRECTION_RTL),
        ;

        private final int value;

        LayoutDirection(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Optional<LayoutDirection> fromValue(Integer value) {
            return Stream.of(LayoutDirection.values())
                    .filter(dir -> dir.value == value)
                    .findFirst();
        }
    }

    private SharedPreferences preferences;
    private Set<String> features;

    private Map<String, Object> featureValues;

    public void init(Context ctx) {
        preferences = getSharedPreferences(ctx);
        features = preferences.getStringSet(SharedConst.PREF_ENABLED_FEATURES, new HashSet<>());

        featureValues = new HashMap<>();
        featureValues.put(SharedConst.PREF_LAYOUT_DIRECTION, preferences.getInt(SharedConst.PREF_LAYOUT_DIRECTION, LayoutDirection.LEFT.value));
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

    public LayoutDirection getLayoutDirection() {
        return LayoutDirection.fromValue((Integer) featureValues.get(SharedConst.PREF_LAYOUT_DIRECTION))
                .orElse(LayoutDirection.LEFT);
    }

    public void setLayoutDirection(LayoutDirection dir) {
        featureValues.put(SharedConst.PREF_LAYOUT_DIRECTION, dir.value);
        setFeatureSetting(editor -> editor.putInt(SharedConst.PREF_LAYOUT_DIRECTION, dir.value));
    }

    private void setFeatureSetting(Consumer<SharedPreferences.Editor> action) {
        SharedPreferences.Editor editor = preferences.edit();
        action.accept(editor);
        editor.apply();
    }

}
