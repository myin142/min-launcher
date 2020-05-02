package io.github.myin.phone.views.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.github.myin.phone.R;
import io.github.myin.phone.SharedConst;
import io.github.myin.phone.utils.PreferenceSettings;
import io.github.myin.phone.views.settings.apps.ManageAppsActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Settings extends AppCompatActivity {

    private Map<String, Switch> featureSwitchMap;
    private Set<String> features;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        findViewById(R.id.edit_apps).setOnClickListener(v -> openEditApps());
        findViewById(R.id.about).setOnClickListener(v -> openAbout());

        featureSwitchMap = new HashMap<>();
        featureSwitchMap.put(SharedConst.PREF_OPEN_APP_FEATURE, findViewById(R.id.enable_open_apps));

        features = PreferenceSettings.getFeatures(this);
        for (String feature : features) {
            Switch featureSwitch = featureSwitchMap.get(feature);
            if (featureSwitch != null) {
                featureSwitch.setChecked(true);
            }
        }

        for (String feature : featureSwitchMap.keySet()) {
            Switch featureSwitch = featureSwitchMap.get(feature);
            if (featureSwitch == null) continue;

            if (features.contains(feature)) {
                featureSwitch.setChecked(true);
            }

            featureSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                toggleFeature(feature, isChecked);
                PreferenceSettings.saveFeatureSettings(this, features);
            });
        }
    }

    private void toggleFeature(String feature, boolean isChecked) {
        if (isChecked) {
            features.add(feature);
        } else {
            features.remove(feature);
        }
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
