package com.example.tempo.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.math.roundToInt


class ConverterPhoto {

    fun converterToByteArray(image: ImageView): ByteArray {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val sizeImage = scaleDown(bitmap)
        val stream = ByteArrayOutputStream()
        sizeImage!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    private fun converterToBitmap(image: ByteArray): Bitmap{
        val photo = ByteArrayInputStream(image)
        return BitmapFactory.decodeStream(photo)
    }

    private fun scaleDown(realImage: Bitmap): Bitmap? {
        val ratio = (250.0 / realImage.width).coerceAtMost(250.0 / realImage.height)
        val width = (ratio * realImage.width).roundToInt()
        val height = (ratio * realImage.height).roundToInt()
        return Bitmap.createScaledBitmap(realImage, width, height, true)
    }

    fun converterStringByBitmap(image: String): Bitmap {
        val str = image.removePrefix("data:image/png;base64,")
        val base = Base64.getDecoder()
        val byte = base.decode(str)
        return converterToBitmap(byte)
    }

}