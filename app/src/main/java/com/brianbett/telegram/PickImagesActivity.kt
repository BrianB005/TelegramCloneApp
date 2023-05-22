package com.brianbett.telegram

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.brianbett.telegram.databinding.ActivityPickImagesBinding
import com.github.drjacky.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage

class PickImagesActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPickImagesBinding
    @SuppressLint("SetTextI18n")
//    private val galleryLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK) {
//
//                val imagesToSave=ArrayList<String>()
//                if (it.data?.hasExtra(ImagePicker.EXTRA_FILE_PATH)!!) {
//                    val uri = it.data?.data!!
//                    binding.selectedImage.visibility= View.VISIBLE
//                    binding.recyclerView.visibility=View.GONE
//                    binding.selectedImage.setImageURI(uri)
//
//                    binding.send.setOnClickListener {
//                        val title=binding.messageInput.text.toString()
//                        if(title.isEmpty()){
//                            binding.messageInputWrapper.error="Kindly fill this field!"
//                        }else{
//                            val fileName=System.currentTimeMillis()
//                            val storageReference= FirebaseStorage.getInstance().getReference("images/$fileName")
//                            binding.send.text="Sending..."
//                            imagesToSave.add(fileName.toString())
//                            val message=HashMap<String,Any>()
//
//                            val userId=MyPreferences.getItemFromSP(applicationContext,"userId")
//                            message["title"]=title
////                            message["recipie
//                            message["images"]=imagesToSave
//                            storageReference.putFile(uri).addOnCompleteListener {
//
//                            }
//                        }
//                    }
//
//                } else if (it.data?.hasExtra(ImagePicker.MULTIPLE_FILES_PATH)!!) {
//                    val files = ImagePicker.getAllFile(it.data) as ArrayList<Uri>
//                    popupView.selectedImage.visibility=View.GONE
//                    popupView.recyclerView.visibility=View.VISIBLE
//                    if (files.size > 0) {
//                        images.addAll(files)
//                        imagesAdapter.notifyDataSetChanged()
//                        popupView.create.setOnClickListener {
//                            val uploadTasks = mutableListOf<UploadTask>()
//
//
//                            if(title.isEmpty()){
//                                popupView.caption.error="Kindly fill this field!"
//                            }else {
//                                imagesToSave.clear()
//                                for (fileUri in files) {
//                                    val fileName = System.currentTimeMillis()
//                                    val storageReference =
//                                        FirebaseStorage.getInstance()
//                                            .getReference("images/$fileName")
//                                    imagesToSave.add(fileName.toString())
//                                    val uploadTask = storageReference.putFile(fileUri)
//                                    uploadTasks.add(uploadTask)
//                                }
//
//                                Tasks.whenAllComplete(uploadTasks)
//                                    .addOnSuccessListener {
//
//                                        val gallery=HashMap<String,Any>()
//                                        gallery["location"]=location
//                                        gallery["caption"]=title
//                                        gallery["images"]=imagesToSave
//                                        Log.d(TAG, "All files uploaded successfully")
//
//
//                                    }
//                                    .addOnFailureListener { exception ->
//                                        Log.e(TAG, "Error uploading files", exception)
//                                        // handle failure here
//                                    }
//                            }
//                        }
//                    }
//                } else {
//                    parseError(it)
//                }
//
//            } else {
//                parseError(it)
//            }
//        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_images)
    }

    private fun selectImage() {
//        galleryLauncher.launch(
//            ImagePicker.with(this@PickImagesActivity)
//                .crop()
//                .galleryOnly()
//                .setMultipleAllowed(true)
//                .cropFreeStyle()
//                .galleryMimeTypes( // no gif images at all
//                    mimeTypes = arrayOf(
//                        "image/png",
//                        "image/jpg",
//                        "image/jpeg"
//                    )
//                )
//                .createIntent()
//        )

    }


    private fun parseError(activityResult: ActivityResult) {
        if (activityResult.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(applicationContext, ImagePicker.getError(activityResult.data), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(applicationContext, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}