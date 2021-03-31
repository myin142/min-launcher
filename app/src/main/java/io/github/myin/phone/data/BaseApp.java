package io.github.myin.phone.data;

import android.content.Intent;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import io.github.myin.phone.utils.IntentBuilder;

public class BaseApp {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "package_name")
    public String packageName;

    @ColumnInfo(name = "class_name")
    public String className;

    @ColumnInfo(name = "index")
    public int index;

    public BaseApp(String packageName, String className) {
        this.packageName = packageName;
        this.className  = className;
    }

    public Intent getActivityIntent() {
        return new IntentBuilder(this).start();
    }

    public void copyValuesFrom(BaseApp app) {
        packageName = app.packageName;
        className = app.className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseApp app = (BaseApp) o;
        return id == app.id;
    }

}
