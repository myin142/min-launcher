package io.github.myin.phone.views.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.github.myin.phone.R;
import io.github.myin.phone.SharedConst;
import io.github.myin.phone.utils.FeaturePreference;
import io.github.myin.phone.views.settings.apps.ManageAppsActivity;
import io.github.myin.phone.views.settings.toolbar.ManageToolsActivity;

public class Settings extends AppCompatActivity {

    private Spinner layoutDirection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        overridePendingTransition(R.anim.anim_top_in, R.anim.anim_top_out);
        updateLayout();

        findViewById(R.id.edit_apps).setOnClickListener(v -> openEditApps());
        findViewById(R.id.edit_tools).setOnClickListener(v -> openEditTools());
        findViewById(R.id.about).setOnClickListener(v -> openAbout());

        initSwitch(R.id.show_clock, SharedConst.PREF_SHOW_CLOCK_FEATURE);
        initSwitch(R.id.show_date, SharedConst.PREF_SHOW_DATE_FEATURE);
        initSwitch(R.id.show_hidden_apps, SharedConst.PREF_SHOW_HIDDEN_APPS);

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
    }

    @SuppressLint("WrongConstant")
    private void updateLayout() {
        findViewById(R.id.root).setLayoutDirection(FeaturePreference.getLayoutDirection().getValue());
    }

    @Override
    protected void onResume() {
        super.onResume();
        layoutDirection.setSelection(FeaturePreference.getLayoutDirection().ordinal());
    }

    private void initSwitch(int id, String feature) {
        Switch switchWidget = findViewById(id);
        switchWidget.setChecked(FeaturePreference.isFeatureEnabled(feature));
        switchWidget.setOnCheckedChangeListener((buttonView, isChecked) -> FeaturePreference.toggleFeature(feature, isChecked));
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
}
