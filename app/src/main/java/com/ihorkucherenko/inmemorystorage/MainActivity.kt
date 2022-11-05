package com.ihorkucherenko.inmemorystorage

import android.os.Bundle
import com.ihorkucherenko.storage.Store
import android.widget.Toast
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

/**
* Created by ihor_kucherenko on 6/22/17.
* https://github.com/KucherenkoIhor
*/

class MainActivity : AppCompatActivity() {

    private val etKey by lazy { findViewById<EditText>(R.id.editTextKey) }
    private val etValue by lazy { findViewById<EditText>(R.id.editTextValue) }
    private val spinner by lazy { findViewById<Spinner>(R.id.spinner) }

    private val store = Store()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ArrayAdapter.createFromResource(
                this,
                R.array.types_array,
                android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun onSaveClick(view: View) {
        val key = etKey.text.toString()
        val value = etValue.text.toString()
        when (spinner.selectedItemPosition) {
            0 -> {
                store.setString(key, value)
            }
            1 -> {
                store.setInteger(key, Integer.parseInt(value))
            }
            2 -> {
                store.setFloat(key, java.lang.Float.parseFloat(value))
            }

        }
        etKey.setText("")
        etValue.setText("")
    }

    fun onGetClick(view: View) {
        val key = etKey.text.toString()
        try {
            when (spinner.selectedItemPosition) {
                0 -> {
                    etValue.setText(store.getString(key))
                }
                1 -> {
                    etValue.setText(store.getInteger(key).toString())
                }
                2 -> {
                    etValue.setText(store.getFloat(key).toString())
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

    fun onCheckKeyClick(view: View) {
        val key = etKey.text.toString()
        val isHasEntry = store.hasEntry(key)
        Toast.makeText(this, "has entry: $isHasEntry", Toast.LENGTH_SHORT).show()
    }

}
