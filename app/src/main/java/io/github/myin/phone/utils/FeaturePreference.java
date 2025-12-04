package io.github.myin.phone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import io.github.myin.phone.SharedConst;
import io.github.myin.phone.theme.ThemeId;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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

        public LayoutDirection flip() {
            return this == LEFT ? RIGHT : LEFT;
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
        featureValues.put(SharedConst.PREF_CALENDAR_ENABLED, preferences.getStringSet(SharedConst.PREF_CALENDAR_ENABLED, new HashSet<>()));
        featureValues.put(SharedConst.PREF_HOME_SHOW_TODO, preferences.getBoolean(SharedConst.PREF_HOME_SHOW_TODO, false));
        featureValues.put(SharedConst.PREF_THEME_ID, preferences.getString(SharedConst.PREF_THEME_ID, ThemeId.DEFAULT.getId()));
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

    public static Set<String> getCalendarEnabled() {
        return (Set<String>) featureValues.get(SharedConst.PREF_CALENDAR_ENABLED);
    }

    public static void setCalendarEnabled(String calendar, boolean enabled) {
        Set<String> enabledCalendars = new HashSet<>(getCalendarEnabled());
        if (enabled) {
            enabledCalendars.add(calendar);
        } else {
            enabledCalendars.remove(calendar);
        }

        featureValues.put(SharedConst.PREF_CALENDAR_ENABLED, enabledCalendars);
        setFeatureSetting(editor -> editor.putStringSet(SharedConst.PREF_CALENDAR_ENABLED, enabledCalendars));
    }

    public static boolean isHomeShowTodo() {
        return Boolean.TRUE.equals(featureValues.get(SharedConst.PREF_HOME_SHOW_TODO));
    }

    public static void setHomeShowTodo(boolean show) {
        featureValues.put(SharedConst.PREF_HOME_SHOW_TODO, show);
        setFeatureSetting(editor -> editor.putBoolean(SharedConst.PREF_HOME_SHOW_TODO, show));
    }

    public static ThemeId getThemeId() {
        Object themeId = featureValues.get(SharedConst.PREF_THEME_ID);
        return Optional.ofNullable(themeId)
                .map(id -> ThemeId.Companion.fromId((String) id))
                .orElse(ThemeId.DEFAULT);
    }

    public static int getThemeResourceId() {
        return getThemeId().getStyleResId();
    }

    public static void setThemeId(ThemeId themeId) {
        final var theme = (themeId != null) ? themeId : ThemeId.DEFAULT;
        featureValues.put(SharedConst.PREF_THEME_ID, theme.getId());
        setFeatureSetting(editor -> editor.putString(SharedConst.PREF_THEME_ID, theme.getId()));
    }

    private static void setFeatureSetting(Consumer<SharedPreferences.Editor> action) {
        SharedPreferences.Editor editor = preferences.edit();
        action.accept(editor);
        editor.apply();
    }

    public static DateTimeFormatter getDateTimeFormatter(Context context) {
        var locale = Configuration.getCurrentLocale(context);
        var dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale);
        if (Locale.JAPAN.getLanguage().equals(locale.getLanguage())) {
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale);
        }

        return dateFormat;
    }

    public static DateTimeFormatter getTimeFormatter(Context context) {
        var locale = Configuration.getCurrentLocale(context);
        if (isFeatureEnabled(SharedConst.PREF_TIME_FORMAT_24)) {
            return DateTimeFormatter.ofPattern("HH:mm").withLocale(locale);
        }

        return DateTimeFormatter.ofPattern("hh:mm a").withLocale(locale);
    }

}
