package com.example.stepcounterapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.TextView
import java.util.concurrent.TimeUnit

class StepSensorManager(private val context: Context, private val stepsTextView: TextView) : SensorEventListener {

    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var stepSensor: Sensor? = null
    private var totalSteps = 0


    fun initStepSensor() {
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        if (stepSensor == null) {
            stepsTextView.text = "Sensor kroków nie jest dostępny."
            Log.e("StepCounter", "Sensor kroków nie jest dostępny.")
        } else {
            registerListener()
        }


        val sharedPreferences = context.getSharedPreferences("StepCounter", Context.MODE_PRIVATE)
        totalSteps = sharedPreferences.getInt("totalSteps", 0)
        stepsTextView.text = "Kroki: $totalSteps"
    }


    fun registerListener() {
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }


    fun unregisterListener() {
        sensorManager.unregisterListener(this)


        val sharedPreferences = context.getSharedPreferences("StepCounter", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("totalSteps", totalSteps)
        editor.apply()
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            totalSteps += event.values[0].toInt()
            stepsTextView.text = "Kroki: $totalSteps"
            Log.d("StepCounter", "Kroki: $totalSteps")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}


    fun calculateInitialDelay(): Long {
        val currentTimeMillis = System.currentTimeMillis()
        val midnightMillis = getMidnightMillis()

        return if (midnightMillis > currentTimeMillis) {
            midnightMillis - currentTimeMillis
        } else {
            midnightMillis + TimeUnit.DAYS.toMillis(1) - currentTimeMillis
        }
    }

    private fun getMidnightMillis(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 24)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
