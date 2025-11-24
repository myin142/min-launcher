package io.github.myin.phone.data.app;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface HomeAppRepository {

    @Query("SELECT * FROM home_apps ORDER BY `index` ASC")
    LiveData<List<HomeApp>> getHomeAppsSorted();

    @Query("SELECT COUNT(*) FROM home_apps")
    int countHomeApps();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HomeApp... homeApps);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(HomeApp... homeApps);

    @Delete
    void delete(HomeApp... homeApps);

}
