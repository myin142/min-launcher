package io.github.myin.phone.config;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import io.github.myin.phone.data.app.HomeApp;
import io.github.myin.phone.data.app.HomeAppRepository;
import io.github.myin.phone.data.setting.AppSetting;
import io.github.myin.phone.data.setting.AppSettingRepository;
import io.github.myin.phone.data.tool.HomeTool;
import io.github.myin.phone.data.tool.HomeToolRepository;

@Database(entities = {HomeApp.class, HomeTool.class, AppSetting.class}, version = 3, exportSchema = false)
public abstract class MainDatabase extends RoomDatabase {
    abstract HomeAppRepository homeAppRepository();
    abstract HomeToolRepository homeToolRepository();
    abstract AppSettingRepository appSettingRepository();
}
