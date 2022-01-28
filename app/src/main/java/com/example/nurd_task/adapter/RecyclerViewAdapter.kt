package com.example.nurd_task.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nurd_task.R
import com.example.nurd_task.models.Device
import com.example.nurd_task.models.DevicesModel
import kotlinx.android.synthetic.main.row_layout.view.*

class RecyclerViewAdapter(private val deviceList: DevicesModel, private val listener: Listener) :
    RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {

    interface Listener {
        fun onItemClick(device: Device)
    }

    class RowHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(device: Device, listener: Listener) {
            itemView.setOnClickListener {
                listener.onItemClick(device)
            }
            itemView.txtHomeNumber.text = device.PK_Device.toString()
            itemView.txtSN.text = device.Server_Device
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return RowHolder(view);
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(deviceList.Devices[position],listener)
    }

    override fun getItemCount(): Int {
        return deviceList.Devices.size
    }
}