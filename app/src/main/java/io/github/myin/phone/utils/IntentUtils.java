package io.github.myin.phone.utils;

import android.content.ComponentName;
import android.content.Intent;

public class IntentUtils {
    public static Intent fromPackageAndClass(String pkg, String cls) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(pkg, cls);

        intent.setComponent(componentName);

        return intent;
    }
}
