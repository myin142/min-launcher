package myin.phone.apps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppItem {
    private CharSequence name;
    private CharSequence fullName;

    public static Intent getIntentForFullName(PackageManager pm, String fullName) {
        System.out.println("Intent for " + fullName);
        int lastDot = fullName.lastIndexOf(".");
        String packageName = fullName.substring(0, lastDot);
        String className = fullName.substring(lastDot + 1);
        System.out.println("Package: " + packageName);
        System.out.println("Class: " + className);

        Intent appIntent = pm.getLaunchIntentForPackage(packageName);
        appIntent.setClassName(packageName, className);
        return appIntent;
    }

    public AppItem(PackageManager pm, String fullName) {
        this(pm, getIntentForFullName(pm, fullName));
    }

    public AppItem(PackageManager pm, Intent intent) {
        this(pm, pm.resolveActivity(intent, 0));
    }

    public AppItem(PackageManager pm, ResolveInfo info) {
        name = info.loadLabel(pm);
        fullName = info.activityInfo.name;
    }

    public String getFullName() {
        return fullName.toString();
    }

    public String toString() {
        return name.toString();
    }

}
