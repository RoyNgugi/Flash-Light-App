package com.example.flashlightapp

import android.content.Context
import android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    //Declares the Camera service
    val camMan by lazy { getSystemService(Context.CAMERA_SERVICE) as CameraManager }

    //Declares a variable that will be used to go through all the cameras in the device
    var camIdWithFlash: String = "0"


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Contains the camera ID list from the camera manager API
        val camList = camMan.cameraIdList

        //looping through the camera list provided and checking fot the camera flash characteristic
        camList.forEach {
            val characteristics = camMan.getCameraCharacteristics(it)
            val doesCamHasFlash: Boolean? = characteristics.get(FLASH_INFO_AVAILABLE)
            if(camIdWithFlash == "0" && doesCamHasFlash == true) {
                camIdWithFlash = it
             }
        }
        //declaring the image button
        val imgBtn = findViewById<ImageButton>(R.id.imageButton)
        var clicked = 0

        //To alternate turning off and on the camera by clicking the button
        imgBtn.setOnClickListener{

            if (clicked == 0 ) {
                camMan.setTorchMode(camIdWithFlash, true)
                imgBtn.setBackgroundResource(R.drawable.torch_on)
                clicked = 1

            } else if (clicked == 1) {
                camMan.setTorchMode(camIdWithFlash, false)
                imgBtn.setBackgroundResource(R.drawable.torch_off)
                clicked = 0
            }


        }


    }

    //closeing the flash once the user exits the app
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPause() {
        super.onPause()
        camMan.setTorchMode(camIdWithFlash, false)
    }
}