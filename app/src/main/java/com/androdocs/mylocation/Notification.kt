package com.androdocs.mylocation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

    class Notification {

    @SerializedName("longitud")
    @Expose
    private var longitud: String? = null

    @SerializedName("latitud")
    @Expose
    private var latitud: String? = null


    fun getLongitud(): String? {
        return longitud
    }

    fun setLongitud(longitud: String) {
        this.longitud = longitud
    }

    fun getLatitud(): String? {
        return latitud
    }

    fun setLatitud(latitud: String) {
        this.latitud = latitud
    }
}
