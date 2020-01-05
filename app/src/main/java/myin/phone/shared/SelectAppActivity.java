package myin.phone.shared;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import myin.phone.apps.AppsList;
import myin.phone.data.app.HomeApp;

public class SelectAppActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == RESULT_OK) {
            String packageName = data.getStringExtra(AppsList.SELECTED_PACKAGE_APP);
            String className = data.getStringExtra(AppsList.SELECTED_CLASS_APP);
            String label = data.getStringExtra(AppsList.SELECTED_LABEL_APP);

            HomeApp appItem = new HomeApp(packageName, className, label);
            onAppSelected(requestCode, appItem);
        }
    }

    protected void onAppSelected(int requestCode, HomeApp homeApp) {
    }
}
