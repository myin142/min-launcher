package myin.phone.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {

    public static void hasPermission(Activity activity, String permission, Runnable runnable) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            runnable.run();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, 0);
        }
    }

}
