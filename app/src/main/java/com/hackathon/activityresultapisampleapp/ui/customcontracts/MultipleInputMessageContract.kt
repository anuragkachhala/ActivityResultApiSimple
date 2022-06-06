package com.hackathon.activityresultapisample.ui.customcontracts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.hackathon.activityresultapisampleapp.ui.MultipleDataMessageActivity

/**
 * @Author: Anurag Kumar kachhala
 * @Date: 03/06/22
 */

/**
 * This Contact take multiple Extra in as input in from of pair and return output as string
 */
class MultipleInputMessageContract : ActivityResultContract<Bundle, String>() {

    override fun createIntent(context: Context, input: Bundle) =
        Intent(context, MultipleDataMessageActivity::class.java).apply {
            putExtras(input)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        val message = intent?.getStringExtra(EXTRA_KEY_VALUE)
        return if (message.isNullOrEmpty()) {
            "No Message Received"
        } else {
            message
        }
    }

    companion object {
        const val EXTRA_KEY_TITLE = "key_title"
        const val EXTRA_KEY_HINT = "key_hint"
        const val EXTRA_KEY_VALUE = "key_value"
    }
}