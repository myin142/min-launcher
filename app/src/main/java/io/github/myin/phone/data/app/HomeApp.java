package io.github.myin.phone.data.app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import io.github.myin.phone.data.BaseApp;

@Entity(tableName = "home_apps")
public class HomeApp extends BaseApp  {

    @ColumnInfo(name = "label")
    public String label;

    public HomeApp(String packageName, String className, String label) {
        super(packageName, className);
        this.label = label;
    }

    public void copyValuesFrom(HomeApp app) {
        super.copyValuesFrom(app);
        label = app.label;
    }

    @Override
    public String toString() {
        return label;
    }

}
