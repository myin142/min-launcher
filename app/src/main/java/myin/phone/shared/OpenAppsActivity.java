package myin.phone.shared;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import myin.phone.apps.AppItem;
import myin.phone.apps.AppsList;

public class OpenAppsActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == RESULT_OK) {
            String packageName = data.getStringExtra(AppsList.SELECTED_PACKAGE_APP);
            String className = data.getStringExtra(AppsList.SELECTED_CLASS_APP);
            String label = data.getStringExtra(AppsList.SELECTED_LABEL_APP);

            AppItem appItem = new AppItem(packageName, className, label);
            onAppSelected(requestCode, appItem);
        }
    }

    protected void onAppSelected(int requestCode, AppItem appItem) {
    }
}
