package io.github.myin.phone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import io.github.myin.phone.SharedConst;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class FeaturePreference {

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

    private static SharedPreferences preferences;
    private static Set<String> features;

    private static Map<String, Object> featureValues;

    private static final List<Runnable> onFeatureChangeObserver = new ArrayList<>();

    public static void init(Context ctx) {
        preferences = getSharedPreferences(ctx);
        features = preferences.getStringSet(SharedConst.PREF_ENABLED_FEATURES, new HashSet<>());

        featureValues = new HashMap<>();
        featureValues.put(SharedConst.PREF_LAYOUT_DIRECTION, preferences.getInt(SharedConst.PREF_LAYOUT_DIRECTION, LayoutDirection.LEFT.value));
    }

    public static void addObserver(Runnable runnable) {
        onFeatureChangeObserver.add(runnable);
        runnable.run();
    }

    public static void removeObserver(Runnable runnable) {
        onFeatureChangeObserver.remove(runnable);
    }

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences(SharedConst.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static Set<String> getFeatures() {
        if (features == null) {
            throw new RuntimeException("Features not initialized.");
        }
        return features;
    }

    public static boolean isFeatureEnabled(String feature) {
        return getFeatures().contains(feature);
    }

    public static void toggleFeature(String feature, boolean enable) {
        if (enable) {
            features.add(feature);
        } else {
            features.remove(feature);
        }

        saveFeatureSettings(features);
        for (Runnable runnable : onFeatureChangeObserver) {
            runnable.run();
        }
    }

    private static void saveFeatureSettings(Set<String> enabledFeatures) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(SharedConst.PREF_ENABLED_FEATURES, enabledFeatures);
        editor.apply();
    }

    public static LayoutDirection getLayoutDirection() {
        return LayoutDirection.fromValue((Integer) featureValues.get(SharedConst.PREF_LAYOUT_DIRECTION))
                .orElse(LayoutDirection.LEFT);
    }

    public static void setLayoutDirection(LayoutDirection dir) {
        featureValues.put(SharedConst.PREF_LAYOUT_DIRECTION, dir.value);
        setFeatureSetting(editor -> editor.putInt(SharedConst.PREF_LAYOUT_DIRECTION, dir.value));
    }

    private static void setFeatureSetting(Consumer<SharedPreferences.Editor> action) {
        SharedPreferences.Editor editor = preferences.edit();
        action.accept(editor);
        editor.apply();
    }

}
