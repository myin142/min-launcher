package io.github.myin.phone.views.settings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.github.myin.phone.R;
import io.github.myin.phone.views.settings.apps.ManageAppsActivity;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        findViewById(R.id.edit_apps).setOnClickListener(v -> openEditApps());
        findViewById(R.id.about).setOnClickListener(v -> openAbout());
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
