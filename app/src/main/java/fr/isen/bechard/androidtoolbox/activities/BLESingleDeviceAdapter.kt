package fr.isen.bechard.androidtoolbox.activities

import android.bluetooth.BluetoothGattCharacteristic
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
import kotlinx.android.synthetic.main.ble_service.view.*
import kotlinx.android.synthetic.main.on_click_device_cell.view.*
import org.w3c.dom.Text


class BLESingleDeviceAdapter(private val serviceList: MutableList<BLEService>) :
    ExpandableRecyclerViewAdapter<BLESingleDeviceAdapter.ServiceViewHolder, BLESingleDeviceAdapter.CharacteristicViewHolder>(
        serviceList
    ) {

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
        val characteristicValue: TextView = itemView.CharacteristicValue

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
        /*val characteristic: BluetoothGattCharacteristic = (group as BLEService).items[childIndex]
        val uuid = characteristic.uuid
        holder.characteristic.text = uuid.toString()*/

        val characteristic = (group.items[childIndex] as BluetoothGattCharacteristic)
        val title = BLEUUIDMatching.getBLEAttributeFromUUID(characteristic.uuid.toString()).title

        val uuidMessage = "UUID: ${characteristic.uuid}"
        holder.characteristicUUID.text = uuidMessage

        holder.characteristicName.text = title

        val properties = arrayListOf<String>()

        /*addPropertyFromCharacteristic(
            characteristic,
            properties,
            "Lecture",
            BluetoothGattCharacteristic.PROPERTY_READ,
            holder.characteristicRead,
            readCharacteristicCallback
        )*/
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


}
