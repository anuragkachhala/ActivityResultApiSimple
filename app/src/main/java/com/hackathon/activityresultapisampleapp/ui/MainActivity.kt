package com.hackathon.activityresultapisampleapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.documentfile.provider.DocumentFile
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hackathon.activityresultapisampleapp.ui.SimpleActivity.Companion.EXTRA_MESSAGE
import com.hackathon.activityresultapisampleapp.ui.SimpleActivityThree.Companion.EXTRA_RESULT_ONE
import com.hackathon.activityresultapisampleapp.ui.SimpleActivityThree.Companion.EXTRA_RESULT_TWO
import com.hackathon.activityresultapisample.ui.customcontracts.AppDetailsSettingsContract
import com.hackathon.activityresultapisample.ui.customcontracts.MultipleInputMessageContract
import com.hackathon.activityresultapisample.ui.customcontracts.MultipleInputMessageContract.Companion.EXTRA_KEY_HINT
import com.hackathon.activityresultapisample.ui.customcontracts.MultipleInputMessageContract.Companion.EXTRA_KEY_TITLE
import com.hackathon.activityresultapisample.ui.customcontracts.SimpleMessageContract
import com.hackathon.activityresultapisampleapp.utils.extensions.createImageUri
import com.hackathon.activityresultapisampleapp.utils.extensions.showPermissionRequestExplanation
import com.hackathon.activityresultapisampleapp.utils.extensions.showToast
import com.hackathon.activityresultapisampleapp.R
import com.hackathon.activityresultapisampleapp.databinding.ActivityMainBinding

/**
 * @Author: Anurag Kumar kachhala
 * @Date: 03/06/22
 */

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageUri: Uri? = null

    // Simple activity result contract
    private val requestStartActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val message = intent?.getStringExtra(SimpleActivity.EXTRA_TITLE)
                showToast("Result OK from Simple Activity message = $message")
            }
        }

    // Simple Activity with multiple Result
    private val startActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val intent = result.data
                    val messageOne = intent?.getStringExtra(EXTRA_RESULT_ONE)
                    val messageTwo = intent?.getStringExtra(EXTRA_RESULT_TWO)
                    val message = StringBuilder().append(messageOne).append(" ").append(messageTwo)
                    showToast("Result OK from Simple Activity message = $message")
                }
                Activity.RESULT_CANCELED -> {
                    showToast("Result Cancel")
                }

            }

        }

    // Pass Custom Contract for start Activity for Result
    private val requestCustomMessageContract =
        registerForActivityResult(SimpleMessageContract()) { message: String? ->
            message?.let { showToast(it) }
        }

    // Pass Multiple value to contract
    private val requestMultipleInputMessageContact =
        registerForActivityResult(MultipleInputMessageContract()) { message: String ->
            showToast(message)
        }

    /**
     * Bluetooth permission is not dangerous so its granted by system it self
     */
    // Request permission contract
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Do something if permission granted
            if (isGranted) {
                showToast("Permission is granted")
            } else {
                showToast("Permission is denied")
            }
        }

    //we need an instance of ActivityResultLauncher first
    /**
     * Requesting for Storage permission, permission is  dangerous so its will not be  granted by system.
     * It will ask to user
     */
    private val requestDangerousPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->

            when {
                granted -> {
                    // user granted permission
                    showToast(getString(R.string.Camera_permission_granted))
                    //Here you can go with the action that requires permission
                    takePicturePreview()
                }
                !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    // user denied permission and set Don't ask again.
                    showSettingsDialog()
                }
                else -> {
                    showToast(getString(R.string.denied_toast))
                    //Here you need to skip the functionality that requires permission
                    //Because user has denied the permission
                    //you can ask permission again when user will request the feature
                    //that requires this permission
                }
            }
        }


    private val requestMultiplePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            var permissionDetails = ""
            map.entries.forEach { entry ->
                Log.d(
                    this@MainActivity.javaClass.simpleName,
                    "Permission ${entry.key} Granted: ${entry.value}"
                ) //add to details
                if (entry.value) permissionDetails += "\n${entry.key}"
            }

            showToast(
                if (permissionDetails.isEmpty()) "No Permission is granted " else
                    "The following permissions have been granted: $permissionDetails"
            )

        }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                binding.ivPicture.visibility = View.VISIBLE
                binding.ivPicture.setImageURI(imageUri)
            }
        }

    private val takePicturePreviewLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                showToast("show bitmap")
            }
        }


    @SuppressLint("InlinedApi")
    private val PROJECTION: Array<out String> = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.LOOKUP_KEY,
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    )

    /**
     * Contact permission is
     */
    // Request permission contract
    private val readContactPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Do something if permission granted
            if (isGranted) {
                pickContactLauncher.launch(null)
                showToast("Permission is granted")
            } else {
                showToast("Permission is denied")
            }
        }

    @SuppressLint("Range")
    private val pickContactLauncher =
        registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->
            if (uri != null) {
                val cursor = contentResolver.query(
                    uri,
                    arrayOf(ContactsContract.Data.DISPLAY_NAME),
                    null,
                    null,
                    null
                )?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val name =
                            cursor.getString(
                                cursor.getColumnIndex(
                                    ContactsContract.Data.DISPLAY_NAME
                                )
                            )
                        showItems(R.string.selected_content, listOf(name))
                    }
                }
            }

        }

    private val pickContentLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.ivPicture.visibility = View.VISIBLE
                Glide.with(this).load(uri).centerCrop().into(binding.ivPicture)
                showToast("pickContentLauncher $uri")
            }
        }

    private val pickMultipleContentLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            if (uris.isNotEmpty()) {
                showItems(R.string.selected_content, uris.map { it.toString() })
            }
        }

    private val launchAppDetailsSetting =
        registerForActivityResult(AppDetailsSettingsContract()) {

        }

    private val createDocumentLauncher =
        registerForActivityResult(ActivityResultContracts.CreateDocument()) { uri ->
            if (uri != null) {
                contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                    ?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val name =
                                cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                            showToast("create $name")
                        }
                    }
            }

        }

    private val openDocumentLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                    ?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val name =
                                cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                            showToast(name)
                        }
                    }
            }

        }

    private val openMultipleDocumentsLauncher =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            if (uris.isNotEmpty()) {
                val names = uris.map {
                    contentResolver.query(
                        it,
                        arrayOf(OpenableColumns.DISPLAY_NAME),
                        null,
                        null,
                        null
                    )
                        ?.use { cursor ->
                            cursor.moveToFirst()
                            cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                        }.orEmpty()
                }
                showItems(R.string.documents, names)
            }


        }

    private val openDocumentTreeLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            if (uri != null) {
                val documentFile = DocumentFile.fromTreeUri(this, uri)!!
                val names = documentFile.listFiles().map { it.name.orEmpty() }
                showItems(R.string.documents, names)
            }

        }

    private var launcher: ActivityResultLauncher<Uri?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setListeners()

        launcher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            if (uri != null) {
                val documentFile = DocumentFile.fromTreeUri(this, uri)!!
                val names = documentFile.listFiles().map { it.name.orEmpty() }
                showItems(R.string.documents, names)
            }

        }
    }

    private fun setListeners() {
        with(binding) {
            btnStartActivityForResult.setOnClickListener {
                launchSimpleActivity()
            }
            btnStartActivityTwoForResult.setOnClickListener {
                launchSimpleActivityTwo()
            }

            btnStartActivityThreeForResult.setOnClickListener {
                launchSimpleActivityThree()
            }

            btnStartSimpleMessage.setOnClickListener {
                launchSimpleMessageContract()
            }

            btnStartMultipleDataMessage.setOnClickListener {
                launchMultipleInputMessageContact()
            }

            btnRequestSinglePermission.setOnClickListener {
                requestSinglePermission()
            }

            btnRequestSingleDangerousPermission.setOnClickListener {
                requestSingleDangerousPermission1()
            }

            btnRequestMultiplePermission.setOnClickListener {
                requestMultiplePermission()
            }

            btnTakePicture.setOnClickListener {
                takePicture()
            }

            btnTakePicturePreview.setOnClickListener {
                takePicturePreview()
            }

            btnPickContact.setOnClickListener {
                pickContact()
            }

            btnPickContent.setOnClickListener {
                pickContent()
            }

            btnPickMultipleContent.setOnClickListener {
                pickMultipleContent()
            }

            btnLaunchAppSetting.setOnClickListener {
                launchAppDetailsSetting()
            }

            btnCreateDocument.setOnClickListener {
                createDocument()
            }

            btnOpenDocument.setOnClickListener {
                openDocument()
            }

            btnOpenMultipleDocument.setOnClickListener {
                openMultipleDocuments()
            }

            btnOpenDocumentTree.setOnClickListener {
                openDocumentTree()
            }

        }
    }


    /**
     * Launch Simple Activity with some data
     */
    private fun launchSimpleActivity() {
        requestStartActivityForResult.launch(
            SimpleActivity.getIntent(this, "Hey...")
        )
    }

    /**
     * Launch Simple Activity with some data with diff intent
     */
    private fun launchSimpleActivityTwo() {
        requestStartActivityForResult.launch(
            Intent(this, SimpleActivityTwo::class.java).apply {
                putExtra(EXTRA_MESSAGE, "Hey.. two")
            }
        )
    }

    /**
     * Launch Simple Activity three for multiple Input and Output data
     */
    private fun launchSimpleActivityThree() {
        startActivityForResult.launch(
            SimpleActivityThree.getIntent(this, "Hello", "Hey")
        )
    }

    /**
     * Launch Simple Activity with custom Activity Result Contract
     */
    private fun launchSimpleMessageContract() {
        requestCustomMessageContract.launch("Hello Message")
        //requestCustomMessageContract.launch(null)
    }

    /**
     * Launch Simple Activity with custom Activity Result Contract with multiple input
     */
    private fun launchMultipleInputMessageContact() {
        requestMultipleInputMessageContact.launch(
            bundleOf(
                EXTRA_KEY_TITLE to "Full Name",
                EXTRA_KEY_HINT to "Enter Name"
            )
        )
    }

    /**
     * Request Single Permission
     * Bluetooth permission is not dangerous so its granted by system it self
     */
    private fun requestSinglePermission() {
        requestPermission.launch(Manifest.permission.BLUETOOTH)
    }


    /**
     * Request single Dangerous Permission with checking that permission is granted or not
     * and show required info
     */
    private fun requestSingleDangerousPermission1() {
        when {
            /* checkPermission(CAMARA_PERMISSION)
             -> {
                 Log.d("Main Activity", "check Single Permission called")
                 // The permission is granted
                 // you can go with the flow that requires permission here
                 takePicturePreview()
             }*/
            shouldShowRequestPermissionRationale(CAMARA_PERMISSION) -> {
                Log.d("Main Activity", "should show Request Permission Rational called")
                // This case means user previously denied the permission
                // So here we can display an explanation to the user
                // That why exactly we need this permission
                showPermissionRequestExplanation(
                    getString(R.string.permission_camera_rationale_title),
                    getString(R.string.permission_camera_rationale_text),
                    getString(R.string.camera_rationale_button)
                ) { requestDangerousPermission.launch(CAMARA_PERMISSION) }
            }
            else -> {
                Log.d("Main Activity", "Permission is requested")
                // Everything is fine you can simply request the permission
                requestDangerousPermission.launch(CAMARA_PERMISSION)
            }
        }
    }

    private fun showSettingsDialog() {
        showPermissionRequestExplanation(
            getString(R.string.permission_camera_title_denied),
            getString(R.string.permission_camera_message_denied),
            getString(R.string.btn_dialog_setting)
        ) { launchAppDetailsSetting() }
    }

    /**
     * Requesting Multiple Permission
     */
    private fun requestMultiplePermission() {
        requestMultiplePermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO
            )
        )
    }

    // remove camera permission if we have in manifest for this permission else you will get
    //with revoked permission android.permission.CAMERA
    private fun takePicture() {
        imageUri = createImageUri()
        takePictureLauncher.launch(imageUri)
    }

    /**
     * Launch camara for request picture preview
     */
    private fun takePicturePreview() {
        takePicturePreviewLauncher.launch(null)
    }

    /**
     * Launch Contact for pick contact
     */
    private fun pickContact() {
        readContactPermission.launch(Manifest.permission.READ_CONTACTS)

    }

    /**
     * Launch Content launcher to get passed type content
     */
    private fun pickContent() {
        // passing image as input to get image content
        pickContentLauncher.launch("image/*")
    }

    /**
     * Launch Content launcher to get passed type multiple content
     */
    private fun pickMultipleContent() {
        pickMultipleContentLauncher.launch("image/*")
    }


    private fun showItems(@StringRes title: Int, names: List<String>) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setItems(names.toTypedArray()) { _, _ ->
            }
            .show()
    }

    private fun launchAppDetailsSetting() {
        launchAppDetailsSetting.launch(Unit)
    }

    private fun createDocument() {
        createDocumentLauncher.launch("my document")
    }

    private fun openDocumentTree() {

        openDocumentTreeLauncher.launch(null)
        // launcher?.launch(null)
        // Contracts can be registered at any point of the activity or fragment lifecycle,
        // but they cannot be launched before going to the CREATED state.
        // A common approach is to register contracts as class fields.
        /*is attempting to register while current state is RESUMED.
          LifecycleOwners must call register before they are STARTED.*/
        /* registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
             if (uri != null) {
                 val documentFile = DocumentFile.fromTreeUri(this, uri)!!
                 val names = documentFile.listFiles().map { it.name.orEmpty() }
                 showItems(R.string.documents, names)
             }
 
         }.launch(null)*/
    }

    private fun openMultipleDocuments() {
        openMultipleDocumentsLauncher.launch(arrayOf("application/*"))
    }

    private fun openDocument() {
        openDocumentLauncher.launch(arrayOf("application/*"))
    }

    companion object {
        private const val CAMARA_PERMISSION = Manifest.permission.CAMERA
    }


}