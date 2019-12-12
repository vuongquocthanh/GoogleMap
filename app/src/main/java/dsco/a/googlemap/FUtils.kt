package dsco.a.googlemap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

const val REQUEST_CODE_LOCATION = 123
const val REQUEST_CODE_CAMERA = 121

const val CHECKINBITMAP = "CheckinBitmap"

fun Context.toast(message:CharSequence, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(this, message, duration).show()
}

fun ViewGroup.inflate(layoutRes:Int, attachToRoot:Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes,this, attachToRoot)
}

fun stringCheckNull(obj: String?): String{
    return obj ?: ""
}

fun checkIsPhotoFile(name: String?): Boolean{
    return name?.toLowerCase()!!.contains("jpg") || name.toLowerCase().contains("png") ||
            name.toLowerCase().contains("jpeg") || name.toLowerCase().contains("gif")
}