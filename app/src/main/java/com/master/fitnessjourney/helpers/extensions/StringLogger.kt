package com.master.fitnessjourney.helpers.extensions

import android.util.Log
fun String.logErrorMessage(tag: String = "MyApp"){
    Log.e(tag,this)
}