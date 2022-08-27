package com.example.tempo.activity

import android.content.Context
import android.widget.Toast

class ShowToast {
    fun message(message: String, context: Context){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}