package fr.isen.mollinari.androidktntoolbox

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_permission.*
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.ArrayList

class PermissionActivity : AppCompatActivity(), LocationListener {


    private val REQUEST_CODE = 11
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 22
    private val PERMISSIONS_ACCESS_COARSE_LOCATION = 33
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        showContacts()
        showCurrentPosition()
    }

    public override fun onStop() {
        super.onStop()
        locationManager.removeUpdates(this)
    }

    fun getPickFromGallery(v: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            val contacts = getContactNames()
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contacts)
            listContact.adapter = adapter
        }
    }

    private fun getContactNames(): List<String> {
        val list = ArrayList<String>()
        val phones =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (phones != null && phones.count > 0) {
            while (phones.moveToNext()) {
                val name =
                    phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                list.add("Nom : $name")

            }
            phones.close()
        }
        return list
    }

    private fun showCurrentPosition() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@PermissionActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSIONS_ACCESS_COARSE_LOCATION
            )
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1f, this)
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                display.text = getString(R.string.permission_location, location.latitude, location.longitude)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION) {
                showCurrentPosition()
            } else if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
                showContacts()
            }
        } else {
            Toast.makeText(this, "Permission refusée par l'utilisateur", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            try {
                data?.let {
                    val imageUri = it.data
                    if (imageUri != null) {
                        val imageStream = contentResolver.openInputStream(imageUri)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        photo.setImageBitmap(selectedImage)
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        }
    }

    override fun onLocationChanged(location: Location) {
        display.text = getString(R.string.permission_location, location.latitude, location.longitude)
    }

    override fun onProviderDisabled(provider: String) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
        Toast.makeText(this, "Gps est déactivé", Toast.LENGTH_SHORT).show()
    }

    override fun onProviderEnabled(provider: String) {
        Toast.makeText(this, "Gps est activé", Toast.LENGTH_SHORT).show()
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Log.d("PermissionActivity", "new status : $status")
    }
}
