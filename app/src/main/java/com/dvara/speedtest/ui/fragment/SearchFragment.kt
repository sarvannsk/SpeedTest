package com.dvara.speedtest.ui.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dvara.speedtest.R
import com.dvara.speedtest.databinding.FragmentSearchBinding
import com.dvara.speedtest.db.entities.SpeedTest
import com.dvara.speedtest.ui.adapter.FilterAdapter
import com.dvara.speedtest.ui.viewmodel.SpeedTestViewModel

class SearchFragment : Fragment() {

    private var speedTestViewModel: SpeedTestViewModel? = null
    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        speedTestViewModel = ViewModelProvider(this).get(SpeedTestViewModel::class.java)
        binding.viewmodel = speedTestViewModel
        binding.search.requestFocus()
        showKeyboard()
        speedTestViewModel?.all?.observe(viewLifecycleOwner, Observer {
            val adapter = FilterAdapter(context, R.layout.item_filter, it as ArrayList<SpeedTest>)
            //start searching for values after typing first character
            binding.search.threshold = 1
            binding.search.setAdapter(adapter)
            binding.search.setOnItemClickListener { adapterView, view, i, l ->
                val speedTest = adapterView.getItemAtPosition(i) as SpeedTest
                var speedTestStr: String = speedTest.mobileNumber.toString() + " - " + speedTest.speed
                binding.search.setText(speedTestStr)
               hideKeyboard()
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun showKeyboard() {
        view?.let { activity?.showKeyboard() }
    }
    fun Context.showKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY )

    }
    fun hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
