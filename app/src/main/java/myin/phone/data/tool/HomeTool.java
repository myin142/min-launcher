package myin.phone.data.tool;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import myin.phone.data.BaseApp;

@Entity(tableName = "home_tools")
public class HomeTool extends BaseApp {

    @ColumnInfo(name = "file_name")
    public String fileName;

    public HomeTool(String packageName, String className) {
        super(packageName, className);
    }

    public void copyValuesFrom(HomeTool app) {
        super.copyValuesFrom(app);
        this.fileName = app.fileName;
    }
}
