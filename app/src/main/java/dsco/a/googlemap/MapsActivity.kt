package dsco.a.googlemap

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var currentLocation: Location
    private lateinit var currentLocationFirst: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var markers:ArrayList<Marker>? = ArrayList()
    private var currentLocationMarker:Marker?=null

    private var latLng = LatLng(21.028552, 105.787311)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (!PermissionUtils.hasPermission(this, Manifest.permission.CAMERA) ||
            !PermissionUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
            !PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
            !PermissionUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            PermissionUtils.requestPermission(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), REQUEST_CODE_LOCATION
            )
        }

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//
//        fetchLastLocationFirst()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        if (isGooglePlayServicesAvailable()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fetchLastLocationFirst()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        mMap.isMyLocationEnabled = true

        val myLocation = LatLng(currentLocation.latitude, currentLocation.longitude)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16.toFloat()))

        showMarker(myLocation)
//        val circle = CircleOptions()
//            .center(LatLng(lat, lng))
//            .strokeColor(ContextCompat.getColor(this@MapsActivity, R.color.colorAccent))
//            .strokeWidth(0.5.toFloat())
//            .fillColor(ContextCompat.getColor(this@MapsActivity, R.color.colorCircleArea))
//            .radius(this.radius * 1000)
//        mMap.addCircle(circle)
    }

    private fun fetchLastLocationFirst() {
        if (!PermissionUtils.hasPermission(
                this@MapsActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            PermissionUtils.requestPermission(
                this@MapsActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
            return
        }

        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            currentLocation = it
            Log.d("CURRENTLOCATION", "${currentLocation.latitude} - ${currentLocation.longitude}")
            currentLocationFirst = it
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (ConnectionResult.SUCCESS == status) return true else {
            if (googleApiAvailability.isUserResolvableError(status)) Toast.makeText(
                this,
                "Please Install google play services to use this application",
                Toast.LENGTH_LONG
            ).show()
        }
        return false
    }

    private fun showMarker(currentLocation:LatLng){
        if (currentLocationMarker == null){
            currentLocationMarker = mMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(currentLocation))
        }else{
            MarkerAnimation().animateMarkerToGB(currentLocationMarker!!, currentLocation, LatLngInterpolator.Spherical())
        }
    }
}
