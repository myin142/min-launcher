package myin.phone.apps;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import myin.phone.data.app.HomeApp;

@Data
@AllArgsConstructor
public class AppItem {

    private final String packageName;
    private final String className;
    private final String label;

    public AppItem(PackageManager pm, ResolveInfo info) {
        label = info.loadLabel(pm).toString();
        packageName = info.activityInfo.packageName;
        className = info.activityInfo.name;
    }

    public String toString() {
        return label;
    }

    public HomeApp toHomeApp() {
        return new HomeApp(packageName, className, label);
    }

}
