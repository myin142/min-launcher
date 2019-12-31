package myin.phone.data.app;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface HomeAppRepository {

    @Query("SELECT * FROM home_apps ORDER BY `index` ASC")
    LiveData<List<HomeApp>> getHomeAppsSorted();

    @Insert
    void insert(HomeApp... homeApps);

    @Update
    void update(HomeApp... homeApps);

    @Delete
    void delete(HomeApp... homeApps);

}
