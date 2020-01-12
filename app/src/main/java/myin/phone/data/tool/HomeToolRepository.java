package myin.phone.data.tool;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface HomeToolRepository {

    @Query("SELECT * FROM home_tools ORDER BY `index` ASC")
    LiveData<List<HomeTool>> getHomeToolSorted();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HomeTool... homeTools);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(HomeTool... homeTools);

    @Delete
    void delete(HomeTool... homeTools);
}
