package myin.phone.data.app;

import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "home_apps")
public class HomeApp {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "package_name")
    public String packageName;

    @ColumnInfo(name = "class_name")
    public String className;

    @ColumnInfo(name = "label")
    public String label;

    @ColumnInfo(name = "index")
    public int index;

    public HomeApp(String packageName, String className, String label) {
        this.packageName = packageName;
        this.className = className;
        this.label = label;
    }

    public Intent getActivityIntent(PackageManager pm) {
        Intent intent = pm.getLaunchIntentForPackage(packageName);

        int lastDot = className.lastIndexOf(".");
        String clsPackage = className.substring(0, lastDot);
        String clsName = className.substring(lastDot + 1);

//        intent.setClassName(clsPackage, clsName);
        return intent;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HomeApp homeApp = (HomeApp) o;
        return id == homeApp.id;
    }

}
