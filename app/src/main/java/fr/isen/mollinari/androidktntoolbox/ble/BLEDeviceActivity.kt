package fr.isen.mollinari.androidktntoolbox.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.mollinari.androidktntoolbox.R
import kotlinx.android.synthetic.main.activity_ble_device.bleServicesList
import kotlinx.android.synthetic.main.activity_ble_device.deviceName
import kotlinx.android.synthetic.main.activity_ble_device.deviceStatus
import kotlinx.android.synthetic.main.activity_ble_device.divider
import kotlinx.android.synthetic.main.activity_ble_device.progressBarService

class BLEDeviceActivity : AppCompatActivity() {

    private var bluetoothGatt: BluetoothGatt? = null
    private lateinit var adapter: BLEServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_device)

        val device = intent.getParcelableExtra<BluetoothDevice>("ble_device")
        deviceName.text = device?.name ?: "Device Unknown"
        deviceStatus.text =
            getString(R.string.ble_device_status, getString(BLEConnexionState.STATE_CONNECTING.text))
        progressBarService.visibility = View.VISIBLE
        divider.visibility = View.INVISIBLE

        bluetoothGatt = device?.connectGatt(this, true, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                progressBarService.visibility = View.INVISIBLE
                divider.visibility = View.VISIBLE
                BLEConnexionState.getBLEConnexionStateFromState(newState)?.let {
                    runOnUiThread {
                        deviceStatus.text =
                            getString(R.string.ble_device_status, getString(it.text))
                    }

                    if (it.state == BLEConnexionState.STATE_CONNECTED.state) {
                        gatt?.discoverServices()
                    }
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                gatt?.services?.let {
                    it.forEach { service ->
                        Log.w("ServiceActivity", "uuid:${service.uuid}, t:${service.type}, iid:${service.instanceId}")
                    }
                    runOnUiThread {
                        adapter = BLEServiceAdapter(
                            it.map { service ->
                                BLEService(service.uuid.toString(), service.characteristics)
                            }.toMutableList()
                        )
                        bleServicesList.adapter = adapter
                        bleServicesList.addItemDecoration(
                            DividerItemDecoration(this@BLEDeviceActivity, LinearLayoutManager.VERTICAL)
                        )
                    }
                }
            }
        })
        bluetoothGatt?.connect()
    }

    override fun onStop() {
        super.onStop()
        closeBluetoothGatt()
    }

    private fun closeBluetoothGatt() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }
}
