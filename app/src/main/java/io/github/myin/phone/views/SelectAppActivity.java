package io.github.myin.phone.views;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.github.myin.phone.data.app.HomeApp;
import io.github.myin.phone.utils.IntentUtils;
import io.github.myin.phone.views.apps.AppsList;

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

    protected HomeApp resolveToHomeApp(ResolveInfo resolveInfo) {
        ActivityInfo info = resolveInfo.activityInfo;
        return new HomeApp(info.packageName, info.name, resolveInfo.loadLabel(getPackageManager()).toString());
    }

    protected abstract void onAppSelected(int requestCode, ResolveInfo info);
}
