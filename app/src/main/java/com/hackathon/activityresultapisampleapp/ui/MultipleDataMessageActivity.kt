package com.hackathon.activityresultapisampleapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hackathon.activityresultapisample.ui.customcontracts.MultipleInputMessageContract.Companion.EXTRA_KEY_HINT
import com.hackathon.activityresultapisample.ui.customcontracts.MultipleInputMessageContract.Companion.EXTRA_KEY_TITLE
import com.hackathon.activityresultapisample.ui.customcontracts.MultipleInputMessageContract.Companion.EXTRA_KEY_VALUE
import com.hackathon.activityresultapisampleapp.databinding.ActivityMultipleDataMessageBinding

/**
 * @Author: Anurag Kumar kachhala
 * @Date: 03/06/22
 */

class MultipleDataMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMultipleDataMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMultipleDataMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        with(binding) {
            tvMessageHeading.text = intent.getStringExtra(EXTRA_KEY_TITLE)
            edtMessage.hint = intent.getStringExtra(EXTRA_KEY_HINT)
            edtMessage.setText(intent.getStringExtra(EXTRA_KEY_VALUE))
            btnSave.setOnClickListener {
                saveValue()
            }
        }
    }

    private fun saveValue() {
        val intent = Intent().apply {
            putExtra(EXTRA_KEY_VALUE, binding.edtMessage.text.toString())
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}