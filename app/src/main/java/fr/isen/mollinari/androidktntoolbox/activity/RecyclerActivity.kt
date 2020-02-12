package fr.isen.mollinari.androidktntoolbox.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.mollinari.androidktntoolbox.R
import kotlinx.android.synthetic.main.activity_recycler.*
import java.security.Permission

class RecyclerActivity : AppCompatActivity(), LocationListener {

    override fun onLocationChanged(location: Location?) {
        displayLocation.text =
            getString(R.string.permission_location, location?.latitude, location?.longitude)
    }

    override fun onProviderDisabled(provider: String) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
        Toast.makeText(this, "Gps est désactivé", Toast.LENGTH_SHORT).show()
    }

    override fun onProviderEnabled(provider: String) {
        Toast.makeText(this, "Gps est activé", Toast.LENGTH_SHORT).show()
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Log.d("PermissionActivity", "new status : $status")
    }

    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        val permissionsNotGranted = getAllPermissionNotGranted()

        if (!permissionsNotGranted.contains(Manifest.permission.READ_CONTACTS)) {
            displayContact()
        }
        if (!permissionsNotGranted.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            showCurrentPosition()
        }
        if (permissionsNotGranted.isNotEmpty()) {
            requestPermission(permissionsNotGranted)
        }
    }

    private fun requestPermission(permissionsNotGranted: Array<String>) {
        ActivityCompat.requestPermissions(
            this,
            permissionsNotGranted,
            PERMISSIONS_REQUEST_READ_AND_LOCATION
        )
    }

    private fun getAllPermissionNotGranted(): Array<String> {
        val listOfPermission = arrayListOf<String>()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == PERMISSIONS_REQUEST_READ_CONTACT) {
            displayContact()
        } else {
            Toast.makeText(this, "Permission refusée par l'utilisateur", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        locationManager?.removeUpdates(this)
    }

    private fun displayContact() {
        val contacts = loadContacts()
        permissionRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RecyclerActivity)
            adapter = ContactAdapter(contacts)
            addItemDecoration(
                DividerItemDecoration(
                    this@RecyclerActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
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

    private fun showCurrentPosition() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1f, this)
            val location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            displayLocation.text =
                getString(R.string.permission_location, location?.latitude, location?.longitude)
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_READ_CONTACT = 22
        private const val PERMISSIONS_REQUEST_READ_AND_LOCATION = 33
    }
}
