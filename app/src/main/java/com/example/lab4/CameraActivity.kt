package com.example.lab4

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lab4.databinding.ActivityCameraBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.File.separator
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraFacing: CameraSelector

    private lateinit var outputDirectory: File

    private lateinit var cameraExecutor: ExecutorService

    private lateinit var storageRef: StorageReference

    private lateinit var actionBar: ActionBar

    private var saveAlbum: String = "album name"
    private var returnToAlbum: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        returnToAlbum = intent.getBooleanExtra("return", false)
        // Intent to change album to save to
        if(intent.extras != null){
            saveAlbum = intent.getStringExtra("album") as String
        }else{
            saveAlbum = FirebaseAuth.getInstance().currentUser?.uid as String
        }

        storageRef = FirebaseStorage.getInstance().reference


        actionBar = supportActionBar!!
        actionBar.title = "Camera"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        outputDirectory = getOutputDirectory()

        cameraFacing = CameraSelector.DEFAULT_FRONT_CAMERA

        cameraExecutor = Executors.newSingleThreadExecutor()
        if (allPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, Constants.REQUIRED_PERMISSIONS,
                Constants.REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnFlipCamera.setOnClickListener()
        {
            flipCamera()
        }
        binding.btnTakePhoto.setOnClickListener()
        {
            takePhoto()
            if(returnToAlbum){
                ContextCompat.startActivity(
                   this@CameraActivity,
                    Intent(this@CameraActivity, AlbumActivity::class.java),
                    null
                )
            }
        }
    }

    private fun flipCamera() {
        cameraFacing = if (cameraFacing == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera()
    }


    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, "${saveAlbum}'s " + resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                Constants.FILE_NAME_FORMAT,
                Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".jpg"
        )
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()


        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val userID = FirebaseAuth.getInstance().currentUser?.uid
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo Saved"

                    val picRef: StorageReference =
                        storageRef.child("users/${userID}/albums/${saveAlbum}/${photoFile.name}")
                    Log.d("Save album Name", "${saveAlbum}")
                    Log.d("Image Save", "${picRef.path}")
                    val uploadTask = picRef.putFile(savedUri)
                    uploadTask.addOnSuccessListener {
                        Log.e("Firebase", "Image Upload success")
                    }.addOnFailureListener {
                        Log.e("Firebase", "Image Upload fail")
                    }

                    Toast.makeText(this@CameraActivity, "$msg $savedUri", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(Constants.TAG, "onError: ${exception.message}", exception)
                }

            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also { mPreview ->

                    mPreview.setSurfaceProvider(
                        binding.viewFinder.surfaceProvider
                    )
                }

            imageCapture = ImageCapture.Builder().build()

            try {

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraFacing, preview, imageCapture
                )

            } catch (e: Exception) {
                Log.d(Constants.TAG, "startCamera Fail:", e)
            }
        }, ContextCompat.getMainExecutor(this))

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {

            if (allPermissionGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permission Denied by the User",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }


        }
    }

    private fun allPermissionGranted() =
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}