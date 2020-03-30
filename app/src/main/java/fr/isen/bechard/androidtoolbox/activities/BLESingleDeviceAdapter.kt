package fr.isen.bechard.androidtoolbox.activities

import android.bluetooth.BluetoothGattCharacteristic
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import fr.isen.bechard.androidtoolbox.R
import fr.isen.bechard.androidtoolbox.dataClass.BLEService
import kotlinx.android.synthetic.main.on_click_device_cell.view.*

class BLESingleDeviceAdapter(private val serviceList: MutableList<BLEService>) :
    ExpandableRecyclerViewAdapter<BLESingleDeviceAdapter.ServiceViewHolder, BLESingleDeviceAdapter.CharacteristicViewHolder>(serviceList){

    class ServiceViewHolder(itemView: View): GroupViewHolder(itemView){
        val serviceName: TextView = itemView.ServiceNameTextView
    }

    class CharacteristicViewHolder(itemView: View): ChildViewHolder(itemView){
        val characteristic: TextView = itemView.ServiceUUIDTextView
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder (
            LayoutInflater.from(parent.context).inflate(R.layout.ble_service,parent,false)
        )

    override fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacteristicViewHolder =
        CharacteristicViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.on_click_device_cell,parent,false)
        )

    override fun onBindChildViewHolder(
        holder: CharacteristicViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>?,
        childIndex: Int
    ) {
        val characteristic: BluetoothGattCharacteristic = (group as BLEService).items[childIndex]
        val uuid = characteristic.uuid
        holder.characteristic.text = uuid.toString()
    }

    override fun onBindGroupViewHolder(
        holder: ServiceViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>
    ) {
        val title = group.title
        holder.serviceName.text = title
    }


}
