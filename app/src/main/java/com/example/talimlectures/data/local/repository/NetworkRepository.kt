package com.example.talimlectures.data.local.repository

import com.example.talimlectures.domain.network.LecturesInterface
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val lecturesInterface: LecturesInterface
    )
{

    suspend fun  getAllLecture() = lecturesInterface.getAllLecture()

}
