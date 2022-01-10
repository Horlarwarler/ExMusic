package com.example.talimlectures.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.talimlectures.util.Constant.CATEGORY_TABLE
import com.example.talimlectures.util.Constant.FAVOURITE_LECTURE
import com.example.talimlectures.util.Constant.NEWLY_ADDED
import com.example.talimlectures.util.Constant.RECENTLY_PLAYED
import com.example.talimlectures.util.Constant.TABLE_NAME
import java.time.LocalDateTime


@Entity(tableName = TABLE_NAME )
data class Lecture(
    @PrimaryKey(autoGenerate = true)
    val lectureId:Int = 0,
    val lectureTitle:String,
    val length:String,
    val date:String,
    val path:String,
    val createdAt: LocalDateTime,
    val metaData:String,
    val favourite: Int = 0,
    val categoryId:Int
)
// Tables  for category
@Entity(tableName = CATEGORY_TABLE)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryId:Int = 0,
    val categoryName:String
)

// Tables  for Favourite
@Entity(tableName = FAVOURITE_LECTURE)
data class Favourite(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val lectureId: Int,
    val lectureTitle: String,
    val description:String
)
//Tables for recently played Lectures
@Entity(tableName = RECENTLY_PLAYED)
data class RecentlyPlayed(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val lectureId: Int,
    val lectureTitle: String,
    val description:String
)
//Tables for newly added lectures
@Entity(tableName = NEWLY_ADDED)
data class NewlyAdded(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val lectureId: Int,
    val lectureTitle: String,
    val description:String
)



