package myin.phone.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import myin.phone.data.app.HomeApp;
import myin.phone.data.app.HomeAppRepository;

@Database(entities = {HomeApp.class}, version = 1, exportSchema = false)
public abstract class MainDatabase extends RoomDatabase {
    abstract HomeAppRepository homeAppRepository();
}
