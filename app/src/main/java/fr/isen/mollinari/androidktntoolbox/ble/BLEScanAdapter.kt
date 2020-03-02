package fr.isen.mollinari.androidktntoolbox.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.mollinari.androidktntoolbox.R
import kotlinx.android.synthetic.main.activity_ble_scan_cell.view.bleAddress
import kotlinx.android.synthetic.main.activity_ble_scan_cell.view.bleLayout
import kotlinx.android.synthetic.main.activity_ble_scan_cell.view.bleName
import kotlinx.android.synthetic.main.activity_ble_scan_cell.view.bleRssi

class BLEScanAdapter(private val scanResults: ArrayList<ScanResult>, val deviceClickListener: (BluetoothDevice) -> Unit) :
    RecyclerView.Adapter<BLEScanAdapter.BLEScanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BLEScanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_ble_scan_cell, parent, false)

        return BLEScanViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = scanResults.size

    override fun onBindViewHolder(holder: BLEScanViewHolder, position: Int) {
        holder.name.text = scanResults[position].device.name ?: "Device Unknown"
        holder.address.text = scanResults[position].device.address
        holder.rssi.text = scanResults[position].rssi.toString()
        holder.rssi.alpha = convertRSSIToAlpha(scanResults[position].rssi)
        holder.layout.setOnClickListener {
            deviceClickListener.invoke(scanResults[position].device)
        }
    }

    fun addDeviceToList(result: ScanResult) {
        val index = scanResults.indexOfFirst { it.device.address == result.device.address }
        if (index != -1) {
            scanResults[index] = result
        } else {
            scanResults.add(result)
        }
    }

    fun clearResults() {
        scanResults.clear()
    }

    private fun convertRSSIToAlpha(rssi: Int): Float =
        when {
            rssi > -40 -> 1f
            rssi > -100 -> (0.01 * rssi + 1.4).toFloat()
            else -> 0.4f
        }


    class BLEScanViewHolder(bleDeviceView: View) : RecyclerView.ViewHolder(bleDeviceView) {
        val layout = bleDeviceView.bleLayout
        val name = bleDeviceView.bleName
        val address = bleDeviceView.bleAddress
        val rssi = bleDeviceView.bleRssi
    }

}
