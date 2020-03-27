package fr.isen.bechard.androidtoolbox.activities

import android.bluetooth.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.isen.bechard.androidtoolbox.R
import fr.isen.bechard.androidtoolbox.dataClass.BLEService
import kotlinx.android.synthetic.main.activity_ble_device.*


class BLEDeviceActivity : AppCompatActivity() {

    var bluetoothGatt: BluetoothGatt? = null
    private lateinit var adapter: BLESingleDeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_device)

        val device: BluetoothDevice? = intent.getParcelableExtra("ble_device")
        bluetoothGatt = device?.connectGatt(this, false, gattCallback)


        val services: MutableList<BluetoothGattCharacteristic>

        val recyclerView = BLECharacteristicsRecyclerView
        val layoutManager = LinearLayoutManager(this)

        //instantiate your adapter with the list of genres
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {
            val intentAction: String
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    intentAction = ACTION_GATT_CONNECTED
                    connectionState = STATE_CONNECTED
                    Log.i(TAG, "Connected to GATT server.")
                    Log.i(TAG, "Attempting to start service discovery: " +
                            bluetoothGatt?.discoverServices())
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    intentAction = ACTION_GATT_DISCONNECTED
                    connectionState = STATE_DISCONNECTED
                    Log.i(TAG, "Disconnected from GATT server.")
                }
            }
        }

        // New services discovered
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            gatt?.services?.forEach{
                Log.i(TAG, "Servcice détécté : ${it.uuid}")
            }
        }

        // Result of a characteristic read operation
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    companion object{
        private val TAG = "BLEDeviceActivity"
        private val STATE_DISCONNECTED = "disconnected"
        private val STATE_CONNECTING = "connecting"
        private val STATE_CONNECTED = "connected"
        val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
        val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
        private var connectionState = STATE_DISCONNECTED
    }
}


