package io.github.myin.phone.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.View
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

class Permission {
    companion object {
        const val READ_CALENDAR = android.Manifest.permission.READ_CALENDAR

        fun hasPermission(activity: Activity, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        }

        fun requestPermission(activity: Activity, permission: String) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), 0);
        }

        fun showPermissionButton(activity: Activity, view: View, permission: String) {
            view.visibility = if (hasPermission(activity, permission)) {
                View.GONE
            } else {
                View.VISIBLE
            }

            view.setOnClickListener {
                requestPermission(activity, permission)
            }
        }
    }

}
