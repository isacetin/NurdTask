package com.example.nurd_task.adapter

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nurd_task.R
import com.example.nurd_task.models.Device
import com.example.nurd_task.models.DevicesModel
import kotlinx.android.synthetic.main.row_layout.view.*

class RecyclerViewAdapter(
    private val deviceList: DevicesModel,
    private val listener: Listener,
    private val sharedPreferences: SharedPreferences
) :
    RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {

    interface Listener {
        fun onItemClick(device: Device, position: Int)
        fun onItemLongClick(device: Device, position: Int)
        fun onEditItemClick(device: Device, position: Int)
    }

    class RowHolder(view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bind(
            device: Device,
            listener: Listener,
            position: Int,
            sharedPreferences: SharedPreferences
        ) {
            itemView.setOnClickListener {
                listener.onItemClick(device, position)
            }
            itemView.setOnLongClickListener {
                listener.onItemLongClick(device, position)
                true
            }
            itemView.icon_editable.setOnClickListener {
                listener.onEditItemClick(device, position)
            }

            val customTitle = sharedPreferences.getString(device.MacAddress, "")
            if (customTitle != "") {
                itemView.txtHomeNumber.text = customTitle
            } else {
                itemView.txtHomeNumber.text = "Home Number ${position + 1}"
            }

            itemView.txtSN.text = "SN : ${device.PK_Device}"

            if (device.Platform == "Sercomm G450") {
                itemView.imgRow.setImageResource(R.drawable.vera_plus_big)
            } else if (device.Platform == "Sercomm G550") {
                itemView.imgRow.setImageResource(R.drawable.vera_secure_big)
            } else if (device.Platform == "MiCasaVerde VeraLite" || device.Platform == "Sercomm NA900" || device.Platform == "Sercomm NA301" || device.Platform == "Sercomm NA930" || device.Platform == "") {
                itemView.imgRow.setImageResource(R.drawable.vera_edge_big)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return RowHolder(view);
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(deviceList.Devices[position], listener, position, sharedPreferences)
    }

    override fun getItemCount(): Int {
        return deviceList.Devices.size
    }

}