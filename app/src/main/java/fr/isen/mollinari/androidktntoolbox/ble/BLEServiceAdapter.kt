package fr.isen.mollinari.androidktntoolbox.ble

import android.bluetooth.BluetoothGattCharacteristic
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import fr.isen.mollinari.androidktntoolbox.R
import kotlinx.android.synthetic.main.activity_ble_device_characteristic_cell.view.characteristicName
import kotlinx.android.synthetic.main.activity_ble_device_characteristic_cell.view.characteristicProperties
import kotlinx.android.synthetic.main.activity_ble_device_characteristic_cell.view.characteristicUuid
import kotlinx.android.synthetic.main.activity_ble_device_characteristic_cell.view.characteristicValue
import kotlinx.android.synthetic.main.activity_ble_device_characteristic_cell.view.notifyAction
import kotlinx.android.synthetic.main.activity_ble_device_characteristic_cell.view.readAction
import kotlinx.android.synthetic.main.activity_ble_device_characteristic_cell.view.writeAction
import kotlinx.android.synthetic.main.activity_ble_device_service_cell.view.serviceArrow
import kotlinx.android.synthetic.main.activity_ble_device_service_cell.view.serviceName
import kotlinx.android.synthetic.main.activity_ble_device_service_cell.view.serviceUuid

class BLEServiceAdapter(
    private val serviceList: MutableList<BLEService>,
    private val readCharacteristicCallback: (BluetoothGattCharacteristic) -> Unit,
    private val writeCharacteristicCallback: (BluetoothGattCharacteristic) -> Unit,
    private val notifyCharacteristicCallback: (BluetoothGattCharacteristic) -> Unit
) :
    ExpandableRecyclerViewAdapter<BLEServiceAdapter.ServiceViewHolder, BLEServiceAdapter.CharacteristicViewHolder>(
        serviceList
    ) {

    class ServiceViewHolder(itemView: View) : GroupViewHolder(itemView) {
        val serviceName: TextView = itemView.serviceName
        val serviceUuid: TextView = itemView.serviceUuid
        private val serviceArrow: View = itemView.serviceArrow
        override fun expand() {
            serviceArrow.animate().rotation(-180f).setDuration(400L).start()
        }

        override fun collapse() {
            serviceArrow.animate().rotation(0f).setDuration(400L).start()
        }
    }

    class CharacteristicViewHolder(itemView: View) : ChildViewHolder(itemView) {
        val characteristicName: TextView = itemView.characteristicName
        val characteristicUuid: TextView = itemView.characteristicUuid
        val characteristicProperties: TextView = itemView.characteristicProperties
        val characteristicValue: TextView = itemView.characteristicValue

        val characteristicReadAction: Button = itemView.readAction
        val characteristicWriteAction: Button = itemView.writeAction
        val characteristicNotifyAction: Button = itemView.notifyAction
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_ble_device_service_cell, parent, false)
        )

    override fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacteristicViewHolder =
        CharacteristicViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_ble_device_characteristic_cell, parent, false)
        )

    override fun onBindGroupViewHolder(
        holder: ServiceViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>
    ) {
        val title =
            BLEUUIDAttributes.getBLEAttributeFromUUID(group.title).title
        holder.serviceName.text = title
        holder.serviceUuid.text = group.title
    }

    override fun onBindChildViewHolder(
        holder: CharacteristicViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>,
        childIndex: Int
    ) {
        val characteristic = (group.items[childIndex] as BluetoothGattCharacteristic)
        val title =
            BLEUUIDAttributes.getBLEAttributeFromUUID(characteristic.uuid.toString()).title

        val uuidMessage = "UUID : ${characteristic.uuid}"
        holder.characteristicUuid.text = uuidMessage

        holder.characteristicName.text = title
        val properties = arrayListOf<String>()

        addPropertyFromCharacteristic(
            characteristic,
            properties,
            "Lecture",
            BluetoothGattCharacteristic.PROPERTY_READ,
            holder.characteristicReadAction,
            holder.characteristicValue,
            readCharacteristicCallback
        )

        addPropertyFromCharacteristic(
            characteristic,
            properties,
            "Ecrire",
            (BluetoothGattCharacteristic.PROPERTY_WRITE or BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE),
            holder.characteristicWriteAction,
            holder.characteristicValue,
            writeCharacteristicCallback
        )

        addPropertyFromCharacteristic(
            characteristic,
            properties,
            "Notifier",
            BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            holder.characteristicNotifyAction,
            holder.characteristicValue,
            notifyCharacteristicCallback
        )

        val proprietiesMessage = "Proprietés : ${properties.joinToString()}"
        holder.characteristicProperties.text = proprietiesMessage
        Log.w(
            "ServiceActivity",
            "char uuid:${characteristic.uuid}, perm:${characteristic.permissions}, proper:${characteristic.properties}"
        )
    }

    private fun addPropertyFromCharacteristic(
        characteristic: BluetoothGattCharacteristic,
        properties: ArrayList<String>,
        propertyName: String,
        propertyType: Int,
        propertyAction: Button,
        propertyValue: TextView,
        propertyCallBack: (BluetoothGattCharacteristic) -> Unit
    ) {
        if ((characteristic.properties and propertyType) != 0) {
            properties.add(propertyName)
            propertyAction.isEnabled = true
            propertyAction.alpha = 1f
            characteristic.value?.let {
                val value = "Valeur : ${String(it)}"
                propertyValue.visibility = View.VISIBLE
                propertyValue.text = value
            }
            propertyAction.setOnClickListener {
                propertyCallBack.invoke(characteristic)
            }
        }
    }

    fun updateFromChangedCharacteristic(characteristic: BluetoothGattCharacteristic?) {
        serviceList.forEach {
            val index = it.items.indexOf(characteristic)
            if(index != -1) {
                it.items.removeAt(index)
                it.items.add(index, characteristic)
            }
        }
    }
}

