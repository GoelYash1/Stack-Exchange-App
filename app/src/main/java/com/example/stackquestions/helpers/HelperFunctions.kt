package com.example.stackquestions.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object HelperFunctions {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeAgo(unixTimestamp: Long): String {
        val now = LocalDateTime.now()
        val dateTime =
            LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimestamp), ZoneId.systemDefault())
        val duration = Duration.between(dateTime, now)

        return "asked " + when {
            duration.toDays() > 2 -> {
                val pattern =
                    if (dateTime.year == now.year) "MMM d 'at' HH:mm" else "MMM d, yyyy 'at' HH:mm"
                val formatter = DateTimeFormatter.ofPattern(pattern)
                dateTime.format(formatter)
            }

            duration.toDays() >= 1 -> if (duration.toDays() > 1) "${duration.toDays()} days" else "yesterday"
            duration.toHours() >= 1 -> "${duration.toHours()} hour${if (duration.toHours() > 1) "s" else ""} ago"
            duration.toMinutes() >= 1 -> "${duration.toMinutes()} min${if (duration.toMinutes() > 1) "s" else ""} ago"
            else -> "${duration.seconds} sec${if (duration.seconds > 1) "s" else ""} ago"
        }
    }

    @Composable
    fun LoadImageFromUrl(imageUrl: String) {
        val context = LocalContext.current
        var image: ImageBitmap? by remember(imageUrl) {
            mutableStateOf(null)
        }

        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    image = resource.asImageBitmap()
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        image?.let {
            Image(
                bitmap = it,
                contentDescription = "Image loaded from URL",
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxSize()
            )
        }
    }
}