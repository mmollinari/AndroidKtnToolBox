package fr.isen.mollinari.androidktntoolbox.ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.mollinari.androidktntoolbox.R
import kotlinx.android.synthetic.main.activity_ble_scan.bleDevicesList
import kotlinx.android.synthetic.main.activity_ble_scan.bleMissing
import kotlinx.android.synthetic.main.activity_ble_scan.divider
import kotlinx.android.synthetic.main.activity_ble_scan.itemsswipetorefresh
import kotlinx.android.synthetic.main.activity_ble_scan.playPauseAction
import kotlinx.android.synthetic.main.activity_ble_scan.progressBar
import kotlinx.android.synthetic.main.activity_ble_scan.scanTitle


class BLEScanActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var mScanning: Boolean = false
    private lateinit var adapter: BLEScanAdapter
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_scan)

        when {
            isBLEEnabled -> {
                initBLEScan()
            }
            bluetoothAdapter != null -> {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent,
                    REQUEST_ENABLE_BT
                )
            }
            else -> {
                bleMissing.visibility = View.VISIBLE
                scanTitle.visibility = View.GONE
                playPauseAction.visibility = View.GONE
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (isBLEEnabled && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            scanLeDeviceWithPermission(false)
        }
    }

    private fun initBLEScan() {
        adapter = BLEScanAdapter(
            arrayListOf(),
            ::onDeviceClicked
        )
        bleDevicesList.adapter = adapter
        bleDevicesList.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        itemsswipetorefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        itemsswipetorefresh.setOnRefreshListener {
            scanLeDeviceWithPermission(false)
            scanLeDeviceWithPermission(true)
            itemsswipetorefresh.isRefreshing = false
        }

        handler = Handler()

        scanLeDeviceWithPermission(true)
        playPauseAction.setOnClickListener {
            scanLeDeviceWithPermission(!mScanning)
        }
        scanTitle.setOnClickListener {
            scanLeDeviceWithPermission(!mScanning)
        }
    }

    private fun onDeviceClicked(device: BluetoothDevice) {
        val intent = Intent(this@BLEScanActivity, BLEDeviceActivity::class.java)
        intent.putExtra("ble_device", device)
        startActivity(intent)
    }

    private fun togglePlayPauseAction() {
        if (mScanning) {
            progressBar.visibility = View.VISIBLE
            divider.visibility = View.INVISIBLE
            scanTitle.text = getString(R.string.ble_scan_title_pause)
            playPauseAction.setImageDrawable(
                ContextCompat.getDrawable(this, android.R.drawable.ic_media_pause)
            )
        } else {
            progressBar.visibility = View.INVISIBLE
            divider.visibility = View.VISIBLE
            scanTitle.text = getString(R.string.ble_scan_title_play)
            playPauseAction.setImageDrawable(
                ContextCompat.getDrawable(this, android.R.drawable.ic_media_play)
            )
        }
    }

    private fun scanLeDeviceWithPermission(enable: Boolean) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            scanLeDevice(enable)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    private fun scanLeDevice(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if (enable) {
                handler.postDelayed({
                    mScanning = false
                    stopScan(leScanCallback)
                }, SCAN_PERIOD)
                mScanning = true
                startScan(leScanCallback)
                adapter.clearResults()
                adapter.notifyDataSetChanged()
            } else {
                mScanning = false
                stopScan(leScanCallback)
            }
            togglePlayPauseAction()
        }
    }

    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.w(this@BLEScanActivity.localClassName, "${result.device}")
            runOnUiThread {
                adapter.addDeviceToList(result)
                adapter.notifyDataSetChanged()
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Toast.makeText(
                this@BLEScanActivity,
                getString(R.string.ble_scan_error),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 44
        private const val PERMISSIONS_REQUEST_LOCATION = 33
        private const val SCAN_PERIOD: Long = 20000
    }
}
