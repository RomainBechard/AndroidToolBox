package fr.isen.bechard.androidtoolbox.activities

import android.bluetooth.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.bechard.androidtoolbox.R
import fr.isen.bechard.androidtoolbox.dataClass.BLEService
import kotlinx.android.synthetic.main.activity_ble_device.*


class BLEDeviceActivity : AppCompatActivity() {

    var bluetoothGatt: BluetoothGatt? = null
    private lateinit var adapter: BLESingleDeviceAdapter
    private lateinit var service: BluetoothGattService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_device)

        val device: BluetoothDevice? = intent.getParcelableExtra<BluetoothDevice>("ble_device")

        DeviceNameTextView.text = device?.name
        DeviceAddressTextView.text = device?.address

        //item decoration
        val recyclerView = BLEServiceList
        val layoutManager = LinearLayoutManager(this)
        val mDividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation
        )
        recyclerView.addItemDecoration(mDividerItemDecoration)

        connectToDevice(device)
    }

    private fun connectToDevice(device: BluetoothDevice?) {
        bluetoothGatt = device?.connectGatt(this, false, object : BluetoothGattCallback() {
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
                        Log.i(
                            TAG, "Attempting to start service discovery: " +
                                    bluetoothGatt?.discoverServices()
                        )

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
                runOnUiThread {
                    BLEServiceList.adapter = BLESingleDeviceAdapter(
                        gatt?.services?.map {
                            BLEService(
                                it.uuid.toString(),
                                it.characteristics
                            )
                        }?.toMutableList() ?: arrayListOf(),
                        this@BLEDeviceActivity
                    )
                    BLEServiceList.layoutManager = LinearLayoutManager(this@BLEDeviceActivity)
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

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                when (status) {
                    BluetoothGatt.GATT_SUCCESS -> {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
        bluetoothGatt?.connect()
    }

    companion object {
        private val TAG = "BLEDeviceActivity"
        private val STATE_DISCONNECTED = "disconnected"
        private val STATE_CONNECTING = "connecting"
        private val STATE_CONNECTED = "connected"
        val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
        val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
        private var connectionState = STATE_DISCONNECTED
    }
}


