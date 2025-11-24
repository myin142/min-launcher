package io.github.myin.phone.data.todo;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface TodoItemRepository {

    @Query("SELECT * FROM todo_items ORDER BY id ASC")
    LiveData<List<TodoItem>> getAll();

    @Query("SELECT * FROM todo_items WHERE id = :id LIMIT 1")
    TodoItem findById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TodoItem... items);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(TodoItem... items);

    @Delete
    void delete(TodoItem... items);
}

