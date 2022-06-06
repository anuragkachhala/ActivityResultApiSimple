package com.hackathon.activityresultapisample.ui.customcontracts

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.hackathon.activityresultapisampleapp.ui.SimpleMessageActivity

/**
 * @Author: Anurag Kumar kachhala
 * @Date: 03/06/22
 */

/**
 * Contract that extends ActivityResultContract to implement a custom
 * intent creation and handling of the results from that intent.
 */
class SimpleMessageContract : ActivityResultContract<String?, String?>() {

    /**
     * Create an intent to start SimpleMessageActivity. If you are passing any input
     * you can add that input here.
     */
    override fun createIntent(context: Context, input: String?) =
        Intent(context, SimpleMessageActivity::class.java).apply {
            putExtra(EXTRA_KEY_MESSAGE_INPUT, input)
        }

    /**
     * Gets hit when the SimpleMessageActivity is closed or finish the activity . The result can then be parsed and
     * set to the String that our contract is looking for.
     */
    override fun parseResult(resultCode: Int, intent: Intent?): String {
        val message = intent?.getStringExtra(EXTRA_KEY_MESSAGE)
        return if (message.isNullOrEmpty()) {
            "No Message Received"
        } else {
            message
        }
    }

    override fun getSynchronousResult(context: Context, input: String?): SynchronousResult<String?>? {
        return  if (input.isNullOrEmpty()) SynchronousResult("Input not available ") else null
    }

    companion object {
        const val EXTRA_KEY_MESSAGE = "MESSAGE_CONTRACT"
        const val EXTRA_KEY_MESSAGE_INPUT = "MESSAGE_INPUT"
    }

}