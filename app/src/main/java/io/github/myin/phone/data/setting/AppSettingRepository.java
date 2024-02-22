package io.github.myin.phone.data.setting;

import android.content.pm.ResolveInfo;

import androidx.room.*;

import java.util.List;

@Dao
public interface AppSettingRepository {

    @Query("SELECT * FROM app_settings WHERE package_name = :pkg AND class_name = :cls")
    List<AppSetting> findByPackageAndClass(String pkg, String cls);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AppSetting... apps);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(AppSetting... apps);

    default AppSetting findByResolveInfo(ResolveInfo info) {
        return getOneByPackageAndClass(info.activityInfo.packageName, info.activityInfo.name);
    }

    default AppSetting getOneByPackageAndClass(String pkg, String cls) {
        List<AppSetting> apps = findByPackageAndClass(pkg, cls);

        if (apps.isEmpty()) {
            return new AppSetting(pkg, cls);
        } else {
            return apps.get(0);
        }
    }

}
