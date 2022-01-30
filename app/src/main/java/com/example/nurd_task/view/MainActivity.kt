package com.example.nurd_task.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.tooling.preview.Devices
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

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {
    private var devicesModel: ArrayList<Device>? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        reacyclerView.layoutManager = layoutManager

        loadData()
    }


    private fun loadData() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(DevicesAPI::class.java)
        val call = service.getDadata()

        call.enqueue(object : Callback<DevicesModel> {
            override fun onResponse(call: Call<DevicesModel>, response: Response<DevicesModel>) {
                if (response.isSuccessful) {
                    progressbar.visibility = View.GONE
                    response.body()?.let {
                        devicesModel = ArrayList(it.Devices)
                        recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
                        reacyclerView.adapter = recyclerViewAdapter
                    }
                }
            }

            override fun onFailure(call: Call<DevicesModel>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    override fun onItemClick(device: Device, position: Int) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra("device", device)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemLongClick(device: Device, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete...")
        builder.setMessage("Are you sure?")
        builder.setPositiveButton("Yes") { _, _ ->
            devicesModel!!.remove(device)
            recyclerViewAdapter!!.notifyItemRemoved(position)
            Toast.makeText(applicationContext, "Yes : ${devicesModel?.size}", Toast.LENGTH_LONG)
                .show()
        }
        builder.setNegativeButton("No") { _, _ ->
            Toast.makeText(applicationContext, "No", Toast.LENGTH_LONG).show()
        }
        builder.show()

    }

    override fun onEditItemClick(device: Device, position: Int) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra("device", device)
        intent.putExtra("editable", true)
        startActivity(intent)
    }
}
