package fr.isen.bechard.androidtoolbox.activities

import android.app.AlertDialog
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import fr.isen.bechard.androidtoolbox.R
import fr.isen.bechard.androidtoolbox.dataClass.BLEService
import kotlinx.android.synthetic.main.alert_dialog_write.view.*
import kotlinx.android.synthetic.main.ble_service.view.*
import kotlinx.android.synthetic.main.on_click_device_cell.view.*


class BLESingleDeviceAdapter(
    serviceList: MutableList<BLEService>,
    private val context: Context,
    gatt: BluetoothGatt?
) :
    ExpandableRecyclerViewAdapter<BLESingleDeviceAdapter.ServiceViewHolder, BLESingleDeviceAdapter.CharacteristicViewHolder>(
        serviceList
    ) {

    private val bleGATT: BluetoothGatt? = gatt
    private var notify: Boolean = false

    class ServiceViewHolder(itemView: View) : GroupViewHolder(itemView) {
        val serviceName: TextView = itemView.ServiceNameTextView
        val serviceUuid: TextView = itemView.ServiceUUIDTextView
        private val serviceArrow: View = itemView.ArrowDown
        override fun expand() {
            serviceArrow.animate().rotation(-180f).setDuration(400L).start()
        }

        override fun collapse() {
            serviceArrow.animate().rotation(0f).setDuration(400L).start()
        }
    }

    class CharacteristicViewHolder(itemView: View) : ChildViewHolder(itemView) {
        val characteristicName: TextView = itemView.CharacteristicName
        val characteristicUUID: TextView = itemView.CharacteristicUuid
        val characteristicProperties: TextView = itemView.CharacteristicProperties
        var characteristicValue: TextView = itemView.CharacteristicValue

        val characteristicRead: Button = itemView.BoutonLecture
        val characteristicWrite: Button = itemView.BoutonEcriture
        val characteristicNotify: Button = itemView.BoutonNotifier
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.ble_service, parent, false)
        )

    override fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacteristicViewHolder =
        CharacteristicViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.on_click_device_cell, parent, false)
        )

    override fun onBindChildViewHolder(
        holder: CharacteristicViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>,
        childIndex: Int
    ) {
        val characteristic = (group.items[childIndex] as BluetoothGattCharacteristic)
        val title = BLEUUIDMatching.getBLEAttributeFromUUID(characteristic.uuid.toString()).title

        val uuidMessage = "UUID: ${characteristic.uuid}"
        holder.characteristicUUID.text = uuidMessage
        holder.characteristicName.text = title


        if (!proprieties(characteristic.properties).contains("Lire"))
            holder.characteristicRead.visibility = View.GONE
        if (!proprieties(characteristic.properties).contains("Ecrire"))
            holder.characteristicWrite.visibility = View.GONE
        if (!proprieties(characteristic.properties).contains("Notifier"))
            holder.characteristicNotify.visibility = View.GONE

        holder.characteristicProperties.text = "Propriétés = ${proprieties(characteristic.properties)}"

        holder.characteristicRead.setOnClickListener {
            bleGATT?.readCharacteristic(characteristic)
            if (characteristic.value != null) {
                holder.characteristicValue.text =  "Valeur : ${String (characteristic.value)}"
            } else {
                holder.characteristicValue.text =  "Valeur : no value yet"
            }
        }

        holder.characteristicWrite.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            val editView = View.inflate(context, R.layout.alert_dialog_write, null)
            dialog.setView(editView)
            dialog.setNegativeButton("Annuler", DialogInterface.OnClickListener { dialog, which ->  })
            dialog.setPositiveButton("Valider", DialogInterface.OnClickListener {
                    _, _ ->
                val text : String = editView.StringToSendEditText.text.toString()
                characteristic.setValue(text)
                bleGATT?.writeCharacteristic(characteristic)
            })
            dialog.show()
        }

        holder.characteristicNotify.setOnClickListener {
            if (!notify){
                notify = true
                bleGATT?.setCharacteristicNotification(characteristic, true)
                if (characteristic.descriptors.size > 0) {

                    val descriptors = characteristic.descriptors
                    for (descriptor in descriptors) {

                        if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
                            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        } else if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) {
                            descriptor.value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                        }
                        bleGATT?.writeDescriptor(descriptor)
                    }
                }
            } else {
                notify = false
                bleGATT?.setCharacteristicNotification(characteristic, false)
            }
        }

        if (characteristic.uuid.toString() == BLEUUIDMatching.getBLEAttributeFromUUID(characteristic.uuid.toString()).uuid && notify){
            if (characteristic.value == null)
                holder.characteristicValue.text =  "Valeur = 0"
            else
                holder.characteristicValue.text = "Valeur = ${byteArrayToHexString(characteristic.value)}"
        }

    }

    override fun onBindGroupViewHolder(
        holder: ServiceViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>
    ) {
        val title =
            BLEUUIDMatching.getBLEAttributeFromUUID(group.title).title
        holder.serviceName.text = title
        holder.serviceUuid.text = group.title
    }

    fun proprieties(property: Int): StringBuilder {
        val sb = StringBuilder()
        if (property and BluetoothGattCharacteristic.PROPERTY_WRITE != 0) {
            sb.append("Ecrire")
        }
        if (property and BluetoothGattCharacteristic.PROPERTY_READ != 0) {
            sb.append(" Lire")
        }
        if (property and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
            sb.append(" Notifier")
        }
        if (sb.isEmpty()) sb.append("Aucune")
        return sb
    }

    private fun byteArrayToHexString(array: ByteArray): String {
        val result = StringBuilder(array.size * 2)
        for ( byte in array ) {
            val toAppend = String.format("%X", byte) // hexadecimal
            result.append(toAppend).append("-")
        }
        result.setLength(result.length - 1) // remove last '-'
        return result.toString()
    }
}


