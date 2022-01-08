package com.example.talimlectures.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.talimlectures.util.Constant.TABLE_NAME
import java.util.*

@Entity(tableName = TABLE_NAME )
data class Lecture(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val length:String,
    val date:String
)