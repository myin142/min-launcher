package io.github.myin.phone.config;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseMigrations {

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `home_tools` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "`package_name` TEXT," +
                    "`class_name` TEXT," +
                    "`index` INTEGER NOT NULL," +
                    "`file_name` TEXT" +
                    ")");
        }
    };

}
