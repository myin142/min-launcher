package io.github.myin.phone.data.setting;

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

}
