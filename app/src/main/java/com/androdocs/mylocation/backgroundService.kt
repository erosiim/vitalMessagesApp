package com.androdocs.mylocation


import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.IBinder
import android.os.Vibrator
import android.widget.Toast
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import kotlin.math.abs

class backgroundService: Service() {


    private var sensorManager: SensorManager? = null
    private var lastUpdate: Long = 0


    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private var listen: SensorListen? = null


    var xold = 0.0
    var yold = 0.0
    var zold = 0.0
    var threadShould = 1850.0
    var oldtime: Long = 0

    override fun onBind(intent: Intent): IBinder? {
        return null;
    }

    override fun onCreate() {
        // TODO Auto-generated method stub
        Toast.makeText(applicationContext, "Started", Toast.LENGTH_SHORT).show()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        lastUpdate = System.currentTimeMillis()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var sensor: Sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        listen = SensorListen()
        sensorManager!!.registerListener(listen, sensor, SensorManager.SENSOR_DELAY_UI)
        return Service.START_STICKY

    }

    override fun onDestroy() {
        // TODO Auto-generated method stub
        sensorManager?.unregisterListener(listen)
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }


    inner class SensorListen : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {
           //Toast.makeText(applicationContext,"acelerometro", Toast.LENGTH_SHORT).show()
            var x=event!!.values[0]
            var y=event!!.values[1]
            var z=event!!.values[2]
            var currentTime=System.currentTimeMillis()
            if((currentTime-oldtime)>100){
                var timeDiff=currentTime-oldtime
                oldtime=currentTime
                var speed= abs(x+y+z-xold-yold-zold) /timeDiff*10000
                if(speed>threadShould){
                    val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()

                    val retrofit =
                        Retrofit.Builder().baseUrl("http://192.168.43.87:5000/")
                            .addConverterFactory(GsonConverterFactory.create(gson)).build()

                    val restClient = retrofit.create(RestClient::class.java)

                    val notification = Notification()
                    var call: Call<NotificationResponse>? = null


                    notification.setLatitud("19.309944")
                    notification.setLongitud("-99.177194")
                    call = restClient.post1(notification)


                    if (call == null) {
                        return
                    }

                        val response = call.execute()

                        if (response.isSuccessful) {
                            print("HOLA")
                        } else {

                            print("ADIOS")
                        }


                    var v=getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    v.vibrate(500)
                    Toast.makeText(applicationContext,"Te has caído",Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // TODO Auto-generated method stub

        }

    }

    }

