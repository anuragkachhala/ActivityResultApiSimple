package com.hackathon.activityresultapisampleapp.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hackathon.activityresultapisampleapp.databinding.ActivitySimpleThreeBinding

/**
 * @Author: Anurag Kumar kachhala
 * @Date: 03/06/22
 */

class SimpleActivityThree : AppCompatActivity() {

    private lateinit var binding: ActivitySimpleThreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimpleThreeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setMessage()

        binding.btnGoBack.setOnClickListener {
            val intent = Intent().apply {
                putExtra(EXTRA_RESULT_ONE, "Hey..")
                putExtra(EXTRA_RESULT_TWO, "Done..")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.btnGoBackWithoutResult.setOnClickListener {
            setResult(Activity.RESULT_CANCELED, Intent())
            finish()
        }
    }

    private fun setMessage() {
        val messageOne = intent.getStringExtra(EXTRA_ONE)
        val messageTwo = intent.getStringExtra(EXTRA_TWO)
        val message = StringBuilder().append(messageOne).append(" ").append(messageTwo).toString()
        binding.tvMsg.text = message
    }

    companion object {
        private const val EXTRA_ONE = "message_one"
        private const val EXTRA_TWO = "message_two"
        const val EXTRA_RESULT_ONE = "result_one"
        const val EXTRA_RESULT_TWO = "result_two"

        fun getIntent(context: Context, messageOne: String, messageTwo: String) =
            Intent(context, SimpleActivityThree::class.java).also {
                it.putExtra(EXTRA_ONE, messageOne)
                it.putExtra(EXTRA_TWO, messageTwo)
            }

    }
}