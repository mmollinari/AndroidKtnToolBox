package fr.isen.mollinari.androidktntoolbox.activity

import android.Manifest
import android.annotation.SuppressLint
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
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.mollinari.androidktntoolbox.R
import fr.isen.mollinari.androidktntoolbox.adapter.ContactAdapter
import kotlinx.android.synthetic.main.activity_permission.*
import java.io.FileNotFoundException

class PermissionActivity : AppCompatActivity(), LocationListener {

    private var locationManager: LocationManager? = null
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
        locationManager?.removeUpdates(this)
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

    @SuppressLint("Recycle")
    private fun loadContacts(): List<String> {
        val contactNameList = arrayListOf<String>()
        val phoneCursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        phoneCursor?.let { cursor ->
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contactNameList.add(name)
            }
            cursor.close()
        }
        return contactNameList
    }

    private fun showContacts() {
        val contacts = loadContacts()
        permissionRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@PermissionActivity)
            adapter = ContactAdapter(
                contacts
            )
            addItemDecoration(
                DividerItemDecoration(
                    this@PermissionActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun showCurrentPosition() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1f, this)
            val location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
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
            data?.data?.let {
                photo.setImageURI(it)
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
