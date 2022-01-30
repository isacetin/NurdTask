package com.example.nurd_task.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import com.example.nurd_task.R
import com.example.nurd_task.models.Device
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val actionbar = supportActionBar
        actionbar!!.title = "Detail Page"
        actionbar.setDisplayHomeAsUpEnabled(true)
        sharedPreferences = this.getSharedPreferences("com.example.nurd_task.view", MODE_PRIVATE)

        val device: Device = intent.getSerializableExtra("device") as Device
        val editable: Boolean = intent.getBooleanExtra("editable", false)

        setImage(device)

        txtHomeNumber2.isEnabled = editable
        if (editable) {
            txtHomeNumber2.requestFocus()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }

        if (sharedPreferences.getString(device.MacAddress, "") == "")
            txtHomeNumber2.setText("Home Number ${intent.getIntExtra("position", 0) + 1}")
        else
            txtHomeNumber2.setText(sharedPreferences.getString(device.MacAddress, ""))


        txtSN.text = "SN : ${device.PK_Device}"
        txtMac.text = "MAC Address : ${device.MacAddress}"
        txtFrmw.text = "Firmware: ${device.Firmware}"
        txtModel.text = "Model : ${device.Platform}"

        txtHomeNumber2.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sharedPreferences.edit().putString(device.MacAddress, txtHomeNumber2.text.toString()).apply()
            }
            false
        }
    }

    private fun setImage(device: Device) {
        if (device.Platform == "Sercomm G450") {
            img.setImageResource(R.drawable.vera_plus_big)
        } else if (device.Platform == "Sercomm G550") {
            img.setImageResource(R.drawable.vera_secure_big)
        } else if (device.Platform == "MiCasaVerde VeraLite" || device.Platform == "Sercomm NA900" || device.Platform == "Sercomm NA301" || device.Platform == "Sercomm NA930" || device.Platform == "") {
            img.setImageResource(R.drawable.vera_edge_big)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}