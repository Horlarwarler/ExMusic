package com.crezent.creator_presentation.upload_dashboard

import android.net.Uri
import com.crezent.common.util.LinkedList
import com.crezent.common.util.MediaAttribute


data class UploadDashboardState(
    val error: LinkedList.Node<String>? = null,
    val selectedAudioUri:Uri? = null,
    val title:String = "",
    val description:String = "",
    val titleBoxError:String? = null,
    val descriptionBoxError:String? = null,
    val audioAttribute: MediaAttribute? = null,
    val imageAttribute: MediaAttribute? = null,
    val imageByteArray:ByteArray? = null
)
