package com.hackathon.activityresultapisampleapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hackathon.activityresultapisampleapp.databinding.ActivityOldResultBinding

@RequiresApi(Build.VERSION_CODES.M)
class OldResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOldResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOldResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnOpenCamera.setOnClickListener {
            when {
                checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                    // access to the camera is allowed, open the camera
                    startActivityForResult(
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                        PHOTO_REQUEST_CODE
                    )
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                    // access to the camera is prohibited, explain why the permission is required
                }
                else -> {
                    // access to the camera is denied, requesting permission
                    requestPermissions(
                        arrayOf(android.Manifest.permission.CAMERA),
                        PHOTO_PERMISSIONS_REQUEST_CODE
                    )
                }
            }
        }

        binding.btnStartActivity.setOnClickListener {
            val intent = Intent(this, SimpleActivity::class.java)
                .putExtra("my_input_key", "What is the answer?")

            startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PHOTO_REQUEST_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val bitmap = data.extras?.get("data") as Bitmap
                    // use bitmap
                } else {
                    // failed to take photo
                }
            }
            SECOND_ACTIVITY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    //  val result = data.getIntExtra("my_result_extra")
                    // use result
                } else {
                    // failed to get the result
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PHOTO_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // access to the camera is allowed, open the camera
                    startActivityForResult(
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                        PHOTO_REQUEST_CODE
                    )
                }
                !shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                    // access to the camera is denied, the user has checked the Don't ask again.
                }
                else -> {
                    // access to the camera is denied, the user has rejected the request
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    companion object {
        private const val PHOTO_REQUEST_CODE = 1
        private const val PHOTO_PERMISSIONS_REQUEST_CODE = 2
        private const val SECOND_ACTIVITY_REQUEST_CODE = 3
    }
}