package com.example.talimlectures.domain.download



interface DownloadInterface {
    fun selectLecture(lectureName:String)
    suspend fun  download()
    suspend fun stopDownload()
    val downloadState: DownloadInterfaceState

}