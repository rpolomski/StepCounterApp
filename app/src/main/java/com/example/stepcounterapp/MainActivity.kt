package com.example.stepcounterapp

import ResetStepsWorker
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.stepcounterapp.Permission.PermissionHelper
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var stepSensorManager: StepSensorManager
    private lateinit var permissionHelper: PermissionHelper
    private lateinit var stepsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        stepsTextView = findViewById(R.id.stepsTextView)


        permissionHelper = PermissionHelper(this)
        stepSensorManager = StepSensorManager(this, stepsTextView)


        permissionHelper.checkPermissions { granted ->
            if (granted) {
                stepSensorManager.initStepSensor()
            } else {
                permissionHelper.requestPermissions()
            }
        }


        scheduleResetStepsTask()
    }


    private fun scheduleResetStepsTask() {
        val workRequest = OneTimeWorkRequest.Builder(ResetStepsWorker::class.java)
            .setInitialDelay(stepSensorManager.calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }

    override fun onResume() {
        super.onResume()
        stepSensorManager.registerListener()
    }

    override fun onPause() {
        super.onPause()
        stepSensorManager.unregisterListener()
    }
}
