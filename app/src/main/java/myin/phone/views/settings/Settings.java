package myin.phone.views.settings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import myin.phone.R;
import myin.phone.views.settings.apps.ManageAppsActivity;
import myin.phone.views.settings.toolbar.ManageToolsActivity;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        findViewById(R.id.edit_apps).setOnClickListener(v -> openEditApps());
        findViewById(R.id.edit_tools).setOnClickListener(v -> openEditTools());
    }

    public void openEditApps() {
        Intent appsListIntent = new Intent(this, ManageAppsActivity.class);
        appsListIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(appsListIntent);
    }

    public void openEditTools() {
        Intent toolsIntent = new Intent(this, ManageToolsActivity.class);
        toolsIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(toolsIntent);
    }
}
