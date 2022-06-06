package com.hackathon.activityresultapisampleapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hackathon.activityresultapisample.ui.customcontracts.SimpleMessageContract.Companion.EXTRA_KEY_MESSAGE
import com.hackathon.activityresultapisample.ui.customcontracts.SimpleMessageContract.Companion.EXTRA_KEY_MESSAGE_INPUT
import com.hackathon.activityresultapisampleapp.databinding.ActivitySimpleMessageBinding

/**
 * @Author: Anurag Kumar kachhala
 * @Date: 03/06/22
 */

class SimpleMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimpleMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySimpleMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val message = intent.getStringExtra(EXTRA_KEY_MESSAGE_INPUT)
        binding.tvMsg.text = message

        binding.btnGoBack.setOnClickListener {
            val intent  = Intent().apply {
                putExtra(EXTRA_KEY_MESSAGE,"Hello simple contract")
            }
            setResult(Activity.RESULT_OK,intent)
            finish()
        }



    }
}