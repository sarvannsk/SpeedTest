package com.dvara.speedtest.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView


import androidx.annotation.LayoutRes
import com.dvara.speedtest.R
import com.dvara.speedtest.db.entities.SpeedTest
import java.util.ArrayList

class FilterAdapter(private val c: Context?, @LayoutRes private val layoutResource: Int, private val speedTest: ArrayList<SpeedTest>) :
    ArrayAdapter<SpeedTest>(c!!, layoutResource, speedTest) {

    var filtered: List<SpeedTest> = listOf()

    override fun getCount(): Int = filtered.size

    override fun getItem(position: Int): SpeedTest = filtered[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(c).inflate(layoutResource, parent, false)
        var tvMobileNumber = view.findViewById<TextView>(R.id.tvMobileNumber)
        var tvSpeed = view.findViewById<TextView>(R.id.tvSpeed)
        var tvTimeStamp = view.findViewById<TextView>(R.id.tvTimeStamp)
        tvMobileNumber.text =  filtered[position].mobileNumber.toString()
        tvSpeed.text =filtered[position].speed
        tvTimeStamp.text =filtered[position].timeStamp
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                filtered = filterResults.values as List<SpeedTest>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()
                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    speedTest as ArrayList<SpeedTest>
                else
                    speedTest.filter {
                        it.mobileNumber.toString().contains(queryString)
                    }
                return filterResults
            }
        }
    }
}