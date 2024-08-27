package com.example.foodiefinder

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog

object Util {
    fun showAlertDialog(
        title : String,
        msg : String,
        negBtnText : String,
        posBtnText : String,
        activity : Activity,
        destination : Class<*>
    ){
        val alertDialog = AlertDialog.Builder(activity , R.style.CustomAlertDialog)

        alertDialog.setTitle(title)
            .setMessage(msg)
            .setCancelable(false)
            .setNegativeButton(
                negBtnText, DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            .setPositiveButton(
                posBtnText, DialogInterface.OnClickListener { dialogInterface, i ->
                    val intent = Intent(activity, destination)
                    //clear the entire activity stack
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    activity.startActivity(intent)
                })
            .create().show()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}