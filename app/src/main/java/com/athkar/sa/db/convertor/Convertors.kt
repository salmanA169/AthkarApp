package com.athkar.sa.db.convertor

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Convertors {

    @TypeConverter
    fun fromList(list:List<String>?):String?{
        if (list == null) return null
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromString(text :String?):List<String>?{
        if (text == null) return null
        return Gson().fromJson(text,object : TypeToken<List<String>>(){}.type)
    }

}