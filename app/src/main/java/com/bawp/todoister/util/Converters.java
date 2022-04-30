package com.bawp.todoister.util;

import androidx.room.TypeConverter;

import com.bawp.todoister.model.Priority;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value){
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date){
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromPriority(Priority priority){
        //TODO : အပေါ်က method နဲ့ အတူတူဘဲ
        if (priority == null){
            return null;
        }else {
            return priority.name();
        }
    }

    @TypeConverter
    public static Priority toPriority(String priority){
        //TODO : အပေါ်က method နဲ့ အတူတူဘဲ
        if (priority == null){
            return null;
        }else {
            return Priority.valueOf(priority);
        }
    }


}
