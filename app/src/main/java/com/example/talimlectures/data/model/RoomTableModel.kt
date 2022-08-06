package com.example.talimlectures.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.talimlectures.util.Constant.CATEGORY_TABLE
import com.example.talimlectures.util.Constant.FAVOURITE_LECTURE
import com.example.talimlectures.util.Constant.NEWLY_ADDED
import com.example.talimlectures.util.Constant.RECENTLY_PLAYED
import com.example.talimlectures.util.Constant.TABLE_NAME
import com.example.talimlectures.util.Constant.WORK_REQUEST
import java.util.*


@Entity(tableName = TABLE_NAME )
data class DatabaseLectureModel(
    @PrimaryKey(autoGenerate = true)
    val lectureId:Int = 0,
    val lectureTitle:String,
    val lectureDescription:String,
    val length:Double,
    val date:String,
    val categoryId:Int? = null,
    val uniqueId:String
)
// Tables  for category
@Entity(tableName = CATEGORY_TABLE)
data class DatabaseCategoryModel(
    @PrimaryKey(autoGenerate = true)
    val categoryId:Int = 0,
    val categoryName:String,
    val uniqueId:String

)

// Tables  for Favourite
@Entity(tableName = FAVOURITE_LECTURE)
data class Favourite(
    @PrimaryKey(autoGenerate = true)
    val id:Int =0,
    val lectureTitle: String,
)
//Tables for recently played Lectures
@Entity(tableName = RECENTLY_PLAYED)
data class RecentlyPlayed(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val lectureTitle: String,
)
//Tables for newly added lectures
@Entity(tableName = NEWLY_ADDED)
data class NewlyAdded(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val lectureTitle: String,
)

@Entity(tableName = WORK_REQUEST)
data class WorkRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lectureName:String,
    val workId: UUID
)



