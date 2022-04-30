package com.bawp.todoister.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bawp.todoister.model.Task;

import java.util.List;

/* SERVICE */
//Dao : Data Access Object
//web မှာဆိုရင် service
@Dao
public interface TaskDao {

    //TODO : Data ထည့်မယ်
    @Insert
    void insertTask(Task task);

    //TODO : Data အကုန် ဖျက်မယ် : DELETE FROM database_name
    @Query("DELETE FROM task_table")
    void deleteAll();

    //TODO : LiveData မသုံးဘဲထားကြည့်ရအောင် ဘာဖြစ်မလဲ LiveData<List<Task>> getTask();
    //TODO : Data အကုန် ပြန်ရှာ မယ်
    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getTasks();


    //TODO : LiveData မသုံးဘဲထားကြည့်ရအောင် ဘာဖြစ်မလဲ LiveData<Task> get(long id);
    //@Query("SELECT * FROM task_table WHERE task_table.task_id == :id")
    @Query("SELECT * FROM task_table task WHERE task.task_id == :id")
    LiveData<Task> get(long id);

    //NO QUERY Need.
    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

}
