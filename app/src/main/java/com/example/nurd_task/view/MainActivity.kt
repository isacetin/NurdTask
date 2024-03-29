package com.example.nurd_task.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nurd_task.R
import com.example.nurd_task.adapter.RecyclerViewAdapter
import com.example.nurd_task.models.Device
import com.example.nurd_task.models.DevicesModel
import com.example.nurd_task.services.DevicesAPI
import com.example.nurd_task.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {
    private var devicesModel: ArrayList<Device>? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = this.getSharedPreferences("com.example.nurd_task.view", MODE_PRIVATE)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        reacyclerView.layoutManager = layoutManager
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData() {
        try {
            val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
            val service = retrofit.create(DevicesAPI::class.java)
            val call = service.getDadata()

            call.enqueue(object : Callback<DevicesModel> {
                override fun onResponse(call: Call<DevicesModel>,response: Response<DevicesModel>) {
                    if (response.isSuccessful) {
                        progressbar.visibility = View.GONE
                        response.body()?.let {
                            devicesModel = ArrayList(it.Devices.sortedBy { it.PK_Device })
                            recyclerViewAdapter = RecyclerViewAdapter(
                                devicesModel!!,
                                this@MainActivity,
                                sharedPreferences
                            )
                            reacyclerView.adapter = recyclerViewAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<DevicesModel>, t: Throwable) {
                    t.printStackTrace()
                }

            })
        } catch (e: Throwable) {
            Toast.makeText(
                applicationContext,
                "There was a problem, please try again later..",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    override fun onItemClick(device: Device, position: Int) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra("device", device)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemLongClick(device: Device, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete... ")
        builder.setMessage("Are you sure?")
        builder.setPositiveButton("Yes") { _, _ ->
            removeItem(device, position)
        }
        builder.setNegativeButton("No") { _, _ ->
        }
        builder.show()

    }

    override fun onEditItemClick(device: Device, position: Int) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra("device", device)
        intent.putExtra("position", position)
        intent.putExtra("editable", true)
        startActivity(intent)
    }

    private fun removeItem(device: Device, position: Int) {
        devicesModel!!.remove(device)
        recyclerViewAdapter!!.notifyItemRemoved(position)
        recyclerViewAdapter!!.notifyItemRangeChanged(position, devicesModel!!.size)
    }
}
