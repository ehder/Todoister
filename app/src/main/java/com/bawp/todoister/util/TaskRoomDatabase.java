package com.bawp.todoister.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bawp.todoister.data.TaskDao;
import com.bawp.todoister.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*LOCAL DATABASE */

//TODO : entities သုံးလုံးရှိရင် ဉပမာ @Database(entities = {Task.class, AnotherEntity.class, Other.class}, version = 1)
@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TaskRoomDatabase extends RoomDatabase {

    //TODO ဘာ လို့ THREAD 4 ထားလဲ 1 ဘဲထားကြည့်ပါလား ဘာဖြစ်မလဲမပေါ့/
    public static final int NUMBER_OF_THREADS = 4;

    public static final String DATABASE_NAME = "todoister_database";

    //TODO : [public static volatile TaskRoomDatabaes INSTANCE;] ကိုမထည့်ရင် ဘာပြသနာတက်မလဲမသိ မထည့်ကြည့်ရအောင်
    public static TaskRoomDatabase INSTANCE;

    public static final ExecutorService databaseWriterExecutor  = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriterExecutor.execute(() -> {
                //TODO : Invoke Dao, Write

                TaskDao taskDao = INSTANCE.taskDao();
                taskDao.deleteAll();

                //Writing to our table



            });
        }
    };

    //TODO : (final Context context) ကို ပါရာမီတာထဲထည့်ရန်
    public static TaskRoomDatabase getDatabase(Context context){

        if (INSTANCE == null){
            synchronized (TaskRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract TaskDao taskDao();

}
