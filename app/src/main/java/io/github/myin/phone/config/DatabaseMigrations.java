package io.github.myin.phone.config;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseMigrations {

    // Prevent instantiation
    private DatabaseMigrations() {}

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

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `app_settings` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "`package_name` TEXT," +
                    "`class_name` TEXT," +
                    "`index` INTEGER NOT NULL," +
                    "`hidden` INTEGER NOT NULL" +
                    ")");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("""
                    ALTER TABLE `app_settings`
                    ADD custom_name TEXT;
                    """);
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `todo_items` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "`title` TEXT," +
                    "`completed` INTEGER NOT NULL" +
                    ")");
        }
    };



}
