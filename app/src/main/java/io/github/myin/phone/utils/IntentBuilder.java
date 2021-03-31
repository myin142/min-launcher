package io.github.myin.phone.utils;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import io.github.myin.phone.data.BaseApp;

public class IntentBuilder {
    private final Intent intent;
    private final String pkg;
    private final String cls;

    public IntentBuilder(BaseApp app) {
        this(app.packageName, app.className);
    }

    public IntentBuilder(ResolveInfo info) {
        this(info.activityInfo.packageName, info.activityInfo.name);
    }

    public IntentBuilder(String pkg, String cls) {
        this.intent = new Intent();
        this.pkg = pkg;
        this.cls = cls;
    }

    public Intent appIntent() {
        ComponentName componentName = new ComponentName(pkg, cls);
        intent.setComponent(componentName);
        return intent;
    }

    public Intent start() {
        appIntent();

        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return intent;
    }

    public Intent uninstall() {
        intent.setAction(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + pkg));
        return intent;
    }
}
