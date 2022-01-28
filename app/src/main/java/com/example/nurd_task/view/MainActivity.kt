package com.example.nurd_task.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nurd_task.R
import com.example.nurd_task.adapter.RecyclerViewAdapter
import com.example.nurd_task.models.Device
import com.example.nurd_task.models.DevicesModel
import com.example.nurd_task.services.DevicesAPI
import com.example.nurd_task.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {
    private var devicesModel: DevicesModel? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //RecycleView

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        reacyclerView.layoutManager = layoutManager

        loadData()
    }


    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        val service = retrofit.create(DevicesAPI::class.java)
        val call = service.getDadata();

        call.enqueue(object : Callback<DevicesModel> {
            override fun onResponse(call: Call<DevicesModel>, response: Response<DevicesModel>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        devicesModel = it
                        recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
                        reacyclerView.adapter = recyclerViewAdapter
                    }
                }
            }

            override fun onFailure(call: Call<DevicesModel>, t: Throwable) {
                t.printStackTrace()
            }

        });
    }

    override fun onItemClick(device: Device) {
        Toast.makeText(this,"Cliced : ${device.Firmware}",Toast.LENGTH_LONG).show()
    }
}