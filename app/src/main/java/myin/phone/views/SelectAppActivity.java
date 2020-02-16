package myin.phone.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import myin.phone.data.BaseApp;
import myin.phone.utils.IntentUtils;
import myin.phone.views.apps.AppsList;
import myin.phone.data.app.HomeApp;

public abstract class SelectAppActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == RESULT_OK) {
            String packageName = data.getStringExtra(AppsList.SELECTED_PACKAGE_APP);
            String className = data.getStringExtra(AppsList.SELECTED_CLASS_APP);
//            String label = data.getStringExtra(AppsList.SELECTED_LABEL_APP);

            Intent intent = IntentUtils.fromPackageAndClass(packageName, className);
            ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            onAppSelected(requestCode, resolveInfo);
        }
    }

    protected abstract void onAppSelected(int requestCode, ResolveInfo info);
}
