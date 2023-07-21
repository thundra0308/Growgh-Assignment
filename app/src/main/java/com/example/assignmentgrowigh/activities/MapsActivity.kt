package com.example.assignmentgrowigh.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.assignmentgrowigh.R
import com.example.assignmentgrowigh.databinding.ActivityMapsBinding
import com.example.assignmentgrowigh.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if(Constants.isLocationEnabled(this)) {
            getCurrentLocation()
        } else {
            Toast.makeText(this@MapsActivity,"Please Enable the Location Service to Access this Feature",Toast.LENGTH_SHORT).show()
        }
        binding.mapBtnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getCurrentLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),CODE)
            return
        }
        val location = fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if(it!=null) {
                currentLocation = it
                Toast.makeText(applicationContext,currentLocation.latitude.toString()+" "+currentLocation.longitude,Toast.LENGTH_SHORT).show()
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CODE -> if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                if(Constants.isLocationEnabled(this)) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(this@MapsActivity,"Please Enable the Location Service to Access this Feature",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val loc = LatLng(currentLocation.latitude,currentLocation.longitude)
        val markerOptions = MarkerOptions().position(loc).title("Current Location")
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(loc))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,7f))
        googleMap?.addMarker(markerOptions)
    }

    companion object {
        const val CODE = 1
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}