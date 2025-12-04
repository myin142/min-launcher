package io.github.myin.phone.views.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.function.Consumer;

import io.github.myin.phone.R;
import io.github.myin.phone.SharedConst;
import io.github.myin.phone.data.calendar.Calendar;
import io.github.myin.phone.data.calendar.CalendarService;
import io.github.myin.phone.theme.ThemeColorExtractor;
import io.github.myin.phone.theme.ThemeColors;
import io.github.myin.phone.theme.ThemeId;
import io.github.myin.phone.utils.FeaturePreference;
import io.github.myin.phone.utils.Permission;
import io.github.myin.phone.views.settings.apps.ManageAppsActivity;
import io.github.myin.phone.views.settings.toolbar.ManageToolsActivity;
import io.github.myin.phone.views.todo.TodoActivity;

import android.view.LayoutInflater;

public class Settings extends AppCompatActivity {

    private Spinner layoutDirection;
    private LinearLayout calendarLayout;
    private Button calendarPermission;
    private LinearLayout themeChipsContainer;

    CalendarService calendarService = new CalendarService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Apply theme BEFORE calling super.onCreate() and setContentView()
        setTheme(FeaturePreference.getThemeResourceId());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.anim_top_in, R.anim.anim_top_out);
        updateLayout();

        findViewById(R.id.edit_apps).setOnClickListener(v -> openEditApps());
        findViewById(R.id.edit_tools).setOnClickListener(v -> openEditTools());
        findViewById(R.id.about).setOnClickListener(v -> openAbout());
        findViewById(R.id.edit_todos).setOnClickListener(v -> openTodoList());

        calendarLayout = findViewById(R.id.calendar_list);
        calendarPermission = findViewById(R.id.request_calendar_permission);
        calendarPermission.setOnClickListener(view -> Permission.Companion.requestPermission(this, Permission.READ_CALENDAR));
        checkIfCalendarPermissionGranted();

        initSwitch(R.id.show_clock, SharedConst.PREF_SHOW_CLOCK_FEATURE, isChecked -> findViewById(R.id.time_format_24).setVisibility(isChecked ? View.VISIBLE : View.GONE));
        initSwitch(R.id.time_format_24, SharedConst.PREF_TIME_FORMAT_24);

        initSwitch(R.id.show_date, SharedConst.PREF_SHOW_DATE_FEATURE);
        initSwitch(R.id.show_hidden_apps, SharedConst.PREF_SHOW_HIDDEN_APPS);
        initSwitch(R.id.show_todo_button, SharedConst.PREF_SHOW_TODO_BUTTON);

        layoutDirection = findViewById(R.id.layout_direction);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.settings_layout_direction_array,
                R.layout.dropdown_item);
        layoutDirection.setAdapter(adapter);
        layoutDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FeaturePreference.LayoutDirection[] dirs = FeaturePreference.LayoutDirection.values();
                if (position >= 0 && position < dirs.length) {
                    FeaturePreference.setLayoutDirection(dirs[position]);
//                    updateLayout(); // Switch Widget does not work with dynamic layout change
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Initialize theme chips
        themeChipsContainer = findViewById(R.id.theme_chips_container);
        setupThemeChips();
    }

    private void setupThemeChips() {
        themeChipsContainer.removeAllViews();

        final ThemeId currentTheme = FeaturePreference.getThemeId();
        final LayoutInflater inflater = LayoutInflater.from(this);

        for (ThemeId themeId : ThemeId.getEntries()) {
            View chipView = inflater.inflate(R.layout.theme_chip, themeChipsContainer, false);

            final ThemeColors colors = ThemeColorExtractor.INSTANCE.getThemeColors(this, themeId.getStyleResId());

            View backgroundContainer = chipView.findViewById(R.id.color_background);
            View textIndicator = chipView.findViewById(R.id.color_text);
            TextView themeName = chipView.findViewById(R.id.theme_name);

            backgroundContainer.setBackgroundColor(colors.getPrimaryDark());
            textIndicator.setBackgroundColor(colors.getPrimary());
            chipView.setBackgroundColor(colors.getAccent());
            themeName.setText(themeId.getDisplayName());

            if (themeId == currentTheme) {
                chipView.setAlpha(1.0f);
            } else {
                chipView.setAlpha(0.2f);
            }

            // Set click listener to change theme
            chipView.setOnClickListener(v -> {
                FeaturePreference.setThemeId(themeId);
                recreate();
            });

            themeChipsContainer.addView(chipView);
        }
    }

    private void checkIfCalendarPermissionGranted() {
        if (Permission.Companion.hasPermission(this, Permission.READ_CALENDAR)) {
            calendarPermission.setVisibility(View.GONE);
            loadCalendar();
        } else {
            calendarPermission.setVisibility(View.VISIBLE);
        }
    }

    private void loadCalendar() {
        calendarLayout.removeAllViews();
        final LayoutInflater inflater = LayoutInflater.from(this);

        final var calendarIds = FeaturePreference.getCalendarEnabled();
        for (Calendar calendar : calendarService.readCalendar(this)) {
            final var calendarBtn = (SwitchCompat) inflater.inflate(R.layout.settings_switch, calendarLayout, false);
            calendarBtn.setText(calendar.getName());

            final var layout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            calendarBtn.setLayoutParams(layout);
            calendarBtn.setChecked(calendarIds.contains(String.valueOf(calendar.getId())));
            calendarBtn.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            calendarBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                FeaturePreference.setCalendarEnabled(String.valueOf(calendar.getId()), isChecked);
            });

            calendarLayout.addView(calendarBtn);
        }
    }

    @SuppressLint("WrongConstant")
    private void updateLayout() {
        findViewById(R.id.root).setLayoutDirection(FeaturePreference.getLayoutDirection().getValue());
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfCalendarPermissionGranted();
        layoutDirection.setSelection(FeaturePreference.getLayoutDirection().ordinal());
    }

    private void initSwitch(int id, String feature) {
        initSwitch(id, feature, x -> {
        });
    }

    private void initSwitch(int id, String feature, Consumer<Boolean> onToggle) {
        SwitchCompat switchWidget = findViewById(id);
        switchWidget.setChecked(FeaturePreference.isFeatureEnabled(feature));
        switchWidget.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FeaturePreference.toggleFeature(feature, isChecked);
            onToggle.accept(isChecked);
        });

        onToggle.accept(switchWidget.isChecked());
    }

    public void openEditApps() {
        Intent appsListIntent = new Intent(this, ManageAppsActivity.class);
        appsListIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(appsListIntent);
    }

    public void openEditTools() {
        Intent toolListIntent = new Intent(this, ManageToolsActivity.class);
        toolListIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(toolListIntent);
    }

    public void openAbout() {
        Intent aboutIntent = new Intent(this, About.class);
        startActivity(aboutIntent);
    }

    public void openTodoList() {
        Intent todoIntent = new Intent(this, TodoActivity.class);
        startActivity(todoIntent);
    }
}
