package com.hackathon.activityresultapisample.ui.customcontracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

/**
 * @Author: Anurag Kumar kachhala
 * @Date: 05/06/22
 */
class AppDetailsSettingsContract : ActivityResultContract<Unit, Unit>() {
    override fun createIntent(context: Context, input: Unit) =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }

    override fun parseResult(resultCode: Int, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK){

        }
    }


}