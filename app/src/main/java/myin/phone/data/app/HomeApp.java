package myin.phone.data.app;

import android.content.ComponentName;
import android.content.Intent;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    public Intent getActivityIntent() {
        Intent intent = new Intent();
        ComponentName component = new ComponentName(packageName, className);

        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setComponent(component);

        return intent;
    }

    public void copyValuesFrom(HomeApp app) {
        packageName = app.packageName;
        className = app.className;
        label = app.label;
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
