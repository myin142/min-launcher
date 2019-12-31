package myin.phone.data.app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "home_apps")
public class HomeApp {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "package_name")
    public String packageName;

    @ColumnInfo(name = "class_name")
    public String className;

    @ColumnInfo(name = "label")
    public String label;

    @ColumnInfo(name = "index")
    public int index;

}
