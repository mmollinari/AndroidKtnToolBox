package fr.isen.mollinari.androidktntoolbox.activity

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.mollinari.androidktntoolbox.R
import kotlinx.android.synthetic.main.activity_blescan.bleDevicesList
import kotlinx.android.synthetic.main.activity_blescan.bleMissing
import kotlinx.android.synthetic.main.activity_blescan.itemsswipetorefresh
import kotlinx.android.synthetic.main.activity_blescan.playPauseAction
import kotlinx.android.synthetic.main.activity_blescan.progressBar
import kotlinx.android.synthetic.main.activity_blescan.scanTitle


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
        setContentView(R.layout.activity_blescan)

        if (isBLEEnabled) {
            initBLEScan()
        } else if (bluetoothAdapter != null) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            bleMissing.visibility = View.VISIBLE
            scanTitle.visibility = View.GONE
            playPauseAction.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        if (isBLEEnabled) {
            scanLeDevice(false)
        }
    }

    private fun initBLEScan() {
        adapter = BLEScanAdapter(arrayListOf())
        bleDevicesList.adapter = adapter
        bleDevicesList.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        itemsswipetorefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        itemsswipetorefresh.setOnRefreshListener {
            scanLeDevice(false)
            scanLeDevice(true)
            itemsswipetorefresh.isRefreshing = false
        }

        handler = Handler()

        scanLeDevice(true)
        playPauseAction.setOnClickListener {
            scanLeDevice(!mScanning)
        }
        scanTitle.setOnClickListener {
            scanLeDevice(!mScanning)
        }
    }

    private fun togglePlayPauseAction() {
        if (mScanning) {
            progressBar.visibility = View.VISIBLE
            scanTitle.text = getString(R.string.ble_scan_title_pause)
            playPauseAction.setImageDrawable(
                ContextCompat.getDrawable(this, android.R.drawable.ic_media_pause)
            )
        } else {
            progressBar.visibility = View.GONE
            scanTitle.text = getString(R.string.ble_scan_title_play)
            playPauseAction.setImageDrawable(
                ContextCompat.getDrawable(this, android.R.drawable.ic_media_play)
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
        private const val SCAN_PERIOD: Long = 20000
    }
}
