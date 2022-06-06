package com.hackathon.activityresultapisampleapp.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackathon.activityresultapisampleapp.databinding.ActivitySimpleBinding

/**
 * @Author: Anurag Kumar kachhala
 * @Date: 03/06/22
 */

class SimpleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimpleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val message = intent.getStringExtra(EXTRA_MESSAGE)
        binding.tvMsg.text = message

        binding.btnGoBack.setOnClickListener {
            val intent = Intent().apply {
                putExtra(EXTRA_TITLE, "hello")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_MESSAGE = "message"

        fun getIntent(context: Context, message: String): Intent {
            return Intent(context, SimpleActivity::class.java).apply {
                putExtra(SimpleActivityTwo.EXTRA_KEY_MESSAGE, message)
            }
        }
    }
}