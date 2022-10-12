package com.example.terveyshelppi.Service

import android.bluetooth.*
import android.util.Log
import com.github.mikephil.charting.data.BarEntry
import java.util.*
import com.github.mikephil.charting.data.Entry

class GattClientCallback(val model: ResultViewModel) : BluetoothGattCallback() {
    val TAG = "terveyshelppi"
    var index = 0
    var index2 = 0

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        if (status == BluetoothGatt.GATT_FAILURE) {
            Log.d(TAG, "GATT connection failure")
            return
        } else if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.d(TAG, "GATT connection success")
        }
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.d(TAG, "Connected GATT service") //...
            gatt.discoverServices();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.d(TAG, "onConnectionStateChange: disconnect")
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return
        }
        Log.d(TAG, "onServicesDiscovered()")
        for (gattService in gatt.services) {
            Log.d(TAG, "Service ${gattService.uuid}")
            if (gattService.uuid == UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb")) {
                Log.d(TAG, "BINGO!!!")
                for (gattCharacteristic in gattService.characteristics)
                    Log.d("DBG",
                        "Characteristic ${gattCharacteristic.uuid} and name is ${gattCharacteristic.describeContents()} and ${gattCharacteristic.properties}")
                /* setup the system for the notification messages */
                val characteristic =
                    gatt.getService(UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb"))
                        .getCharacteristic(UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb"))
                gatt.readCharacteristic(characteristic)
                if (gatt.setCharacteristicNotification(characteristic, true)) {
                    // then enable them on the server
                    Log.d(TAG, "onServicesDiscovered: start to write noti")
                    for (des in characteristic.descriptors)
                        Log.d(TAG,
                            "onServicesDiscovered: description is ${des.uuid} permission${des.permissions} value ${des.value} ")
                    Log.d(TAG, "onServicesDiscovered: finish des")
                    val descriptor =
                        characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    gatt.writeDescriptor(descriptor)
                } else Log.d(TAG, "onServicesDiscovered: fail to write noti")
            }
        }
    }

    override fun onDescriptorWrite(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        status: Int,
    ) {
        super.onDescriptorWrite(gatt, descriptor, status)
        Log.d(TAG,
            "onDescriptorWrite status is $status and descriptor is ${descriptor.value}  ${descriptor.permissions}")
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
    ) {
        val bpm = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1)
        Log.d(TAG, "BPM: $bpm")
        if (bpm != null) {
            model.mBPM.postValue(bpm)
            if (model.highmBPM.value?.compareTo(bpm) ?: -1 < 0) {
                model.highmBPM.postValue(bpm)
                model.testGraphMulti.value?.add(Entry(index2.toFloat(), (bpm/2).toFloat()))
                index2++
            }
            if (model.lowmBPM.value?.compareTo(bpm) ?: 1 > 0) model.lowmBPM.postValue(bpm)
            Log.d(TAG, "heart rate is ${model.mBPM.value}")
            model.graph.value?.add(Entry(index.toFloat(), bpm.toFloat()))
            model.barGraph.postValue(mutableListOf(BarEntry(index.toFloat(), bpm.toFloat()), BarEntry((index + 1).toFloat(), (bpm/2).toFloat())))
            index++
        }
    }
}