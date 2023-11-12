package com.crezent.common.util

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MediaPicker(
    private val activity: ComponentActivity,
) {
    private lateinit var launcher : ActivityResultLauncher<String>

    fun registerPicker(
        onMediaPick : (ByteArray?, Uri?) -> Unit,
    ){

        launcher = activity.registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            uri ->
            uri?.let {

                activity.contentResolver.openInputStream(uri)?.use {
                        onMediaPick(it.readBytes(), uri)
                    }
            }
        }
    }
    fun pickMediaItem(mediaType:String){
        launcher.launch(mediaType)
    }
}