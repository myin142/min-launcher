package myin.phone.apps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppItem implements Comparable<AppItem> {
    private CharSequence name;
    private CharSequence fullName;

    public AppItem(PackageManager pm, String packageName) {
        this(pm, pm.getLaunchIntentForPackage(packageName));
    }

    public AppItem(PackageManager pm, Intent intent) {
        this(pm, pm.resolveActivity(intent, 0));
    }

    public AppItem(PackageManager pm, ResolveInfo info) {
        name = info.loadLabel(pm);
        fullName = info.activityInfo.packageName;
    }

    public String getFullName() {
        return fullName.toString();
    }

    public String toString() {
        return name.toString();
    }

    @Override
    public int compareTo(AppItem o) {
        return name.toString().toLowerCase().compareTo(o.getName().toString().toLowerCase());
    }
}
