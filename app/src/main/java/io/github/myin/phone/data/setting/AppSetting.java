package io.github.myin.phone.data.setting;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import io.github.myin.phone.data.BaseApp;

@Entity(tableName = "app_settings")
public class AppSetting extends BaseApp {

    @ColumnInfo(name = "hidden")
    private boolean hidden;

    @ColumnInfo(name = "custom_name")
    private String customName;

    public AppSetting(String packageName, String className) {
        super(packageName, className);
    }

    public void toggleHidden() {
        this.hidden = !this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }
}
