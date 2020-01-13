package fr.isen.mollinari.androidktntoolbox.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import androidx.core.app.ActivityCompat
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import fr.isen.mollinari.androidktntoolbox.R
import kotlinx.android.synthetic.main.activity_permission.*
import java.io.FileNotFoundException
import java.util.ArrayList

class PermissionActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var permissionsNotGranted: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        photo.setOnClickListener {
            getPickFromGallery()
        }

        permissionsNotGranted = getAllPermissionNotGranted()

        if(permissionsNotGranted.isEmpty()) {
            showContacts()
            showCurrentPosition()
        }
        else {
            requestPermission()
        }
    }

    public override fun onStop() {
        super.onStop()
        locationManager.removeUpdates(this)
    }

    private fun getAllPermissionNotGranted(): Array<String> {
        val listOfPermission = mutableListOf<String>()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            listOfPermission.add(Manifest.permission.READ_CONTACTS)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            listOfPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        return listOfPermission.toTypedArray()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            permissionsNotGranted,
            PERMISSIONS_REQUEST_READ_AND_LOCATION
        )
    }

    private fun getPickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun showContacts() {
        val contacts = getContactNames()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contacts)
        listContact.adapter = adapter
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
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1f, this)
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                display.text = getString(
                    R.string.permission_location,
                    location.latitude,
                    location.longitude
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION || requestCode == PERMISSIONS_REQUEST_READ_AND_LOCATION) {
                showCurrentPosition()
            }
            if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS || requestCode == PERMISSIONS_REQUEST_READ_AND_LOCATION) {
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
        display.text =
                getString(R.string.permission_location, location.latitude, location.longitude)
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

    companion object {
        private const val REQUEST_CODE = 11
        private const val PERMISSIONS_REQUEST_READ_CONTACTS = 22
        private const val PERMISSIONS_ACCESS_COARSE_LOCATION = 33
        private const val PERMISSIONS_REQUEST_READ_AND_LOCATION = 44
    }
}
