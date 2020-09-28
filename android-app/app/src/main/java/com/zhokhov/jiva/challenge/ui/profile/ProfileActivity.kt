package com.zhokhov.jiva.challenge.ui.profile

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zhokhov.jiva.challenge.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import com.zhokhov.jiva.challenge.R
import com.zhokhov.jiva.challenge.ui.login.LoginActivity

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 0
        const val CHOOSE_FROM_LIBRARY = 1
        const val REQUEST_PERMISSIONS = 2
    }

    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        bindState()
        bindUiControls()

        // load avatar when profile just opened
        profileViewModel.loadAvatar()
    }

    private fun bindState() {
        profileViewModel.loginCredentials.observe(this, Observer {
            val loginCredentials = it ?: return@Observer

            binding.profileEmailBox.setText(
                loginCredentials.email, TextView.BufferType.NORMAL
            )
            binding.profilePasswordBox.setText(
                loginCredentials.password, TextView.BufferType.NORMAL
            )
        })

        profileViewModel.avatarState.observe(this, { avatarState ->
            when (avatarState) {
                is AvatarError -> {
                    Intent(this, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NEW_TASK

                        startActivity(this)
                    }
                }
                is AvatarSuccess -> {
                    binding.avatar.setImageBitmap(avatarState.avatar)
                }
            }
        })
    }

    private fun bindUiControls() {
        binding.avatar.setOnClickListener {
            Timber.d("Clicked profile photo")

            selectImage(this)
        }
    }

    private fun selectImage(context: Context) {
        val items = arrayOf(
            getString(R.string.take_photo),
            getString(R.string.choose_from_library)
        )

        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.change_profile_photo))
            .setItems(items) { _, which ->
                Timber.d("Selected %s", which)

                if (which == 0) {
                    dispatchTakePictureIntent()
                } else if (which == 1) {
                    requestPermissionsOrOpenAlbum()
                }
            }
            .show()
    }

    private fun requestPermissionsOrOpenAlbum() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSIONS
            )
        } else {
            dispatchOpenAlbumIntent()
        }
    }

    private fun dispatchOpenAlbumIntent() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
            startActivityForResult(it, CHOOSE_FROM_LIBRARY)
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun updateAvatar(bitmap: Bitmap) {
        // Assure that the Image uploaded to the backend does not exceed 1 MB
        val thumbnail = ThumbnailUtils.extractThumbnail(
            bitmap, 300, 300,
            ThumbnailUtils.OPTIONS_RECYCLE_INPUT
        )

        binding.avatar.setImageBitmap(thumbnail)

        profileViewModel.updateAvatar(thumbnail)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSIONS ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchOpenAlbumIntent()
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.insufficient_rights,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data.extras!!.get("data") as Bitmap

                    updateAvatar(imageBitmap)
                }
                CHOOSE_FROM_LIBRARY -> {
                    val selectedImage: Uri? = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        contentResolver.query(
                            selectedImage,
                            filePathColumn, null, null, null
                        )?.use { cursor ->
                            cursor.moveToFirst()

                            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath: String = cursor.getString(columnIndex)

                            updateAvatar(BitmapFactory.decodeFile(picturePath))
                        }
                    }
                }
            }
        }
    }

}
