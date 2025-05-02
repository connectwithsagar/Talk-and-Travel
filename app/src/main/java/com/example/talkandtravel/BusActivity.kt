package com.example.talkandtravel

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BusActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var actvFrom: AutoCompleteTextView
    private lateinit var actvTo: AutoCompleteTextView
    private lateinit var tvDate: TextView
    private lateinit var chipGroupTime: ChipGroup
    private lateinit var chipGroupFilters: ChipGroup
    private lateinit var rvBuses: RecyclerView
    private lateinit var fabMap: FloatingActionButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus) // Link your XML layout

        // Initialize views
        toolbar = findViewById(R.id.toolbar)
        actvFrom = findViewById(R.id.actv_from)
        actvTo = findViewById(R.id.actv_to)
        tvDate = findViewById(R.id.tv_date)
        chipGroupTime = findViewById(R.id.chipgroup_time)
        chipGroupFilters = findViewById(R.id.chipgroup_filters)
        rvBuses = findViewById(R.id.rv_buses)

        fabMap = findViewById(R.id.fab_map)

        setupToolbar()
        setupAutoComplete()
        setupChipGroups()
        setupRecyclerView()
        setupFab()
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_filter -> {
                    Toast.makeText(this, "Filter clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupAutoComplete() {
        val cities = listOf("New York", "Los Angeles", "Chicago", "Houston", "Phoenix")

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cities)
        actvFrom.setAdapter(adapter)
        actvTo.setAdapter(adapter)

        actvFrom.setOnItemClickListener { parent, view, position, id ->
            val selectedCity = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "From: $selectedCity", Toast.LENGTH_SHORT).show()
        }

        actvTo.setOnItemClickListener { parent, view, position, id ->
            val selectedCity = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "To: $selectedCity", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupChipGroups() {
        chipGroupTime.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val chip = findViewById<com.google.android.material.chip.Chip>(checkedId)
                Toast.makeText(this, "Selected Time: ${chip.text}", Toast.LENGTH_SHORT).show()
            }
        }

        chipGroupFilters.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val chip = findViewById<com.google.android.material.chip.Chip>(checkedId)
                Toast.makeText(this, "Filter Applied: ${chip.text}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {

        rvBuses.setBackgroundColor(resources.getColor(R.color.onPrimary))
        // In real project: rvBuses.adapter = YourBusAdapter(busList)
    }

    private fun setupFab() {
        fabMap.setOnClickListener {
            Toast.makeText(this, "Show map clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
