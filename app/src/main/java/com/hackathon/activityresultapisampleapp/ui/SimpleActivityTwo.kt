package com.hackathon.activityresultapisampleapp.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackathon.activityresultapisampleapp.databinding.ActivitySimpleTwoBinding

/**
 * @Author: Anurag Kumar kachhala
 * @Date: 03/06/22
 */

class SimpleActivityTwo : AppCompatActivity() {

    private lateinit var binding: ActivitySimpleTwoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimpleTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val message = intent.getStringExtra(SimpleActivity.EXTRA_MESSAGE)
        binding.tvMsg.text = message

        binding.btnGoBack.setOnClickListener {
            val intent = Intent().apply {
                putExtra(EXTRA_KEY_TITLE, "hello..from two")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        const val EXTRA_KEY_TITLE = "title"
        const val EXTRA_KEY_MESSAGE = "message"

    }
}