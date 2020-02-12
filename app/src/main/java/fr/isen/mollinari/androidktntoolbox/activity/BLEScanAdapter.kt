package fr.isen.mollinari.androidktntoolbox.activity

import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.mollinari.androidktntoolbox.R
import kotlinx.android.synthetic.main.activity_recycler_cell.view.contactName

class BLEScanAdapter(private val scanResults: ArrayList<ScanResult>) : RecyclerView.Adapter<BLEScanAdapter.BLEScanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BLEScanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_recycler_cell, parent, false)

        return BLEScanViewHolder(view)
    }

    override fun getItemCount(): Int = scanResults.size

    override fun onBindViewHolder(holder: BLEScanViewHolder, position: Int) {
        holder.contactName.text = scanResults[position].device.name ?: "null"
    }

    fun addDeviceToList(result: ScanResult) {
        if (scanResults.all { it.device.address != result.device.address }) {
            scanResults.add(result)
        }
    }

    fun clearResults() {
        scanResults.clear()
    }

    class BLEScanViewHolder(contactView: View) : RecyclerView.ViewHolder(contactView) {
        val contactName = contactView.contactName
    }

}
