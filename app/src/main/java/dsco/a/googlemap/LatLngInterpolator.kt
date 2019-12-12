package dsco.a.googlemap

import com.google.android.gms.maps.model.LatLng
import java.lang.Math.*
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


interface LatLngInterpolator {
    fun interpolate(fraction: Float, a: LatLng?, b: LatLng?): LatLng?

    class Spherical : LatLngInterpolator {
        override fun interpolate(fraction: Float, a: LatLng?, b: LatLng?): LatLng? {
            val fromLat: Double = toRadians(a!!.latitude)
            val fromLng: Double = toRadians(a.longitude)
            val toLat: Double = toRadians(b!!.latitude)
            val toLng: Double = toRadians(b.longitude)
            val cosFromLat: Double = kotlin.math.cos(fromLat)
            val cosToLat: Double = kotlin.math.cos(toLat)

            // Computes Spherical interpolation coefficients.
            // Computes Spherical interpolation coefficients.
            val angle: Double = computeAngleBetween(fromLat, fromLng, toLat, toLng)
            val sinAngle: Double = sin(angle)
            if (sinAngle < 1E-6) {
                return a
            }
            val aFrom: Double = sin((1 - fraction) * angle) / sinAngle
            val bTo: Double = sin(fraction * angle) / sinAngle

            // Converts from polar to vector and interpolate.
            // Converts from polar to vector and interpolate.
            val x: Double = aFrom * cosFromLat * kotlin.math.cos(fromLng) + bTo * cosToLat * kotlin.math.cos(toLng)
            val y: Double = aFrom * cosFromLat * sin(fromLng) + bTo * cosToLat * sin(toLng)
            val z: Double = aFrom * sin(fromLat) + bTo * sin(toLat)

            // Converts interpolated vector back to polar.
            // Converts interpolated vector back to polar.
            val lat: Double = atan2(z, sqrt(x * x + y * y))
            val lng: Double = atan2(y, x)
            return LatLng(toDegrees(lat), toDegrees(lng))
        }

        private fun computeAngleBetween(
            fromLat: Double,
            fromLng: Double,
            toLat: Double,
            toLng: Double
        ): Double { // Haversine's formula
            val dLat = fromLat - toLat
            val dLng = fromLng - toLng
            return 2 * kotlin.math.asin(
                sqrt(
                    sin(dLat / 2).pow(2) +
                            kotlin.math.cos(fromLat) * kotlin.math.cos(toLat) * sin(dLng / 2).pow(2)
                )
            )
        }
    }
}