package com.crezent.common.util

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.MediaColumns
import androidx.core.net.toUri
import java.io.File

class FileHelper(
    private val context: Context
) {
    fun saveFile(fileName: String, byteArray: ByteArray ): String {
            val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            outputStream.write(byteArray)
            outputStream.close()
            val uri = context.getFileStreamPath(fileName).toUri()
            return uri.toString()

    }

    fun byteFromUri(uri: Uri):ByteArray?{
        val outputStream = context.contentResolver.openInputStream(uri,)

        val byteArray = outputStream?.readBytes()
        outputStream?.close()
        return byteArray


    }

    fun readFile(fileName: String): ByteArray {
            val byteArray = context.openFileInput(fileName).use {
                it.readBytes()
            }
            return byteArray
    }

    fun deleteFile(fileName: String){
        context.deleteFile(fileName)
    }

    fun fileExist(fileName: String): Boolean {
        val file = File(fileName)
        return file.exists()
    }
   fun checkType(uri: Uri):MimeType{
        val contentType = context.contentResolver.getType(uri)?:return MimeType.UNKNOWN
        val startWithImage = contentType.startsWith("image")
        val startWithAudio = contentType.startsWith("audio")
        val startWithVideo = contentType.startsWith("video")
        return  when{
           startWithImage -> {
               MimeType.IMAGE
           }
            startWithAudio ->{
                MimeType.AUDIO
            }
            startWithVideo ->{
                MimeType.VIDEO
            }
            else -> {
                MimeType.UNKNOWN
            }
        }
    }



    fun getFileAttribute(uri: Uri):MediaAttribute{
        val attribute =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val cursor =   context.contentResolver.query(uri, null,null,null)?:return  MediaAttribute(
                null, null,null
            )
            // 750936.0 = 0.71mb
            //     x       =1mb

           // val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val durationIndex =  try {
                cursor.getColumnIndexOrThrow(MediaColumns.DURATION)
            }
            catch (error:Exception){
                null
            }
            val sizeIndex = cursor.getColumnIndexOrThrow(MediaColumns.SIZE)
            val nameIndex = cursor.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            val fileName = cursor.getString(nameIndex)
            val fileSize = cursor.getString(sizeIndex)?.toDouble()?.div(1057656)
            val duration = durationIndex?.let {
                cursor.getString(durationIndex)?.toDouble()
            }
            cursor.close()
            MediaAttribute(
                size = fileSize,
                duration= duration,
                name = fileName
            )

        } else {
            val mediaMetadataRetriever =MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(context, uri)
            val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toDouble()
            val name = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

            val fileSize = uri.path?.let {
                File(it).length().toDouble()
            }
            MediaAttribute(
                size = fileSize,
                duration= duration,
                name = name
            )
        }
        return attribute

    }


}

data class MediaAttribute(
    val  size:Double?,
    val duration:Double?,
    val name:String?
)


enum class  MimeType {
    VIDEO,
    AUDIO,
    IMAGE,
    UNKNOWN
}