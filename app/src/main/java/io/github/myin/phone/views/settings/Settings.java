package io.github.myin.phone.views.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.github.myin.phone.R;
import io.github.myin.phone.SharedConst;
import io.github.myin.phone.utils.FeaturePreference;
import io.github.myin.phone.views.settings.apps.ManageAppsActivity;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        overridePendingTransition(R.anim.anim_top_in, R.anim.anim_top_out);

        findViewById(R.id.edit_apps).setOnClickListener(v -> openEditApps());
        findViewById(R.id.about).setOnClickListener(v -> openAbout());

        initSwitch(R.id.enable_open_apps, SharedConst.PREF_OPEN_APP_FEATURE);
        initSwitch(R.id.show_clock, SharedConst.PREF_SHOW_CLOCK_FEATURE);
        initSwitch(R.id.show_date, SharedConst.PREF_SHOW_DATE_FEATURE);
        initSwitch(R.id.show_hidden_apps, SharedConst.PREF_SHOW_HIDDEN_APPS);
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

    public void openAbout() {
        Intent aboutIntent = new Intent(this, About.class);
        startActivity(aboutIntent);
    }
}
