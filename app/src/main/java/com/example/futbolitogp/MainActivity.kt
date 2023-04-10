package com.example.futbolitogp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    private val rotationSensorReading = FloatArray(3)
    var ImgPelota: ImageView? = null
    var ImgArco1: ImageView? = null
    var ImgArco2: ImageView? = null
    var goles1: TextView? = null
    var goles2: TextView? = null
    var cancha: ConstraintLayout? = null
    var width = 0
    var height = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cancha = findViewById(R.id.cancha)
        ImgPelota = findViewById(R.id.ImgPelota)
        ImgArco1 = findViewById(R.id.ImgArco1)
        ImgArco2 = findViewById(R.id.ImgArco2)
        goles1 = findViewById(R.id.txtGoles1)
        goles2 = findViewById(R.id.txtGoles2)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        val acelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (acelerometer != null) {
            sensorManager!!.registerListener(
                this, acelerometer,
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(
                event.values, 0, rotationSensorReading,
                0, rotationSensorReading.size
            )
        }
        Log.d("MySensor", rotationSensorReading[1].toString() + "")
        width = cancha!!.width
        height = cancha!!.height
        if (ImgPelota!!.x < 0) {
            ImgPelota!!.x = 0f
        } else if (ImgPelota!!.x + ImgPelota!!.height > width && width != 0) {
            ImgPelota!!.x = (width - ImgPelota!!.height).toFloat()
        } else {
            ImgPelota!!.x = ImgPelota!!.x - rotationSensorReading[0] * 10
        }
        Log.d("Position", (ImgPelota!!.y + ImgPelota!!.width).toString() + "")
        if (ImgPelota!!.y < 0) {
            ImgPelota!!.y = 0f
        } else if (ImgPelota!!.y + ImgPelota!!.width > height && height != 0) {
            ImgPelota!!.y = (height - ImgPelota!!.width).toFloat()
        } else {
            ImgPelota!!.y = ImgPelota!!.y + rotationSensorReading[1] * 10
        }
        if (ImgPelota!!.x + ImgPelota!!.width - 15 >= ImgArco1!!.x && ImgPelota!!.x - 15 <= ImgArco1!!.x + ImgArco1!!.width) {
            if (ImgPelota!!.y + 15 >= ImgArco1!!.y && ImgPelota!!.y + 15 <= ImgArco1!!.y + ImgArco1!!.height) {
                goles2!!.text = (goles2!!.text.toString().toInt() + 1).toString() + ""
                ImgPelota!!.x = (width / 2).toFloat()
                ImgPelota!!.y = (height / 2).toFloat()
                Log.d("Position", "Gol en la porteria 1")
            }
        }
        if (ImgPelota!!.x + ImgPelota!!.width - 15 >= ImgArco2!!.x && ImgPelota!!.x - 15 <= ImgArco2!!.x + ImgArco2!!.width) {
            if (ImgPelota!!.y + ImgPelota!!.height - 15 >= ImgArco2!!.y && ImgPelota!!.y - 15 <= ImgArco2!!.y + ImgArco2!!.height) {
                goles1!!.text = (goles1!!.text.toString().toInt() + 1).toString() + ""
                ImgPelota!!.x = (width / 2).toFloat()
                ImgPelota!!.y = (height / 2).toFloat()
                Log.d("Position", "Gol en la porteria 2")
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
}