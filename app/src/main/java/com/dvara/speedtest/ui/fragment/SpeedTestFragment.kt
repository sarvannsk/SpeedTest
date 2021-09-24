package com.dvara.speedtest.ui.fragment


import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Looper.getMainLooper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dinuscxj.progressbar.CircleProgressBar
import com.dvara.speedtest.R
import com.dvara.speedtest.common.AppPreferences
import com.dvara.speedtest.common.Constants
import com.dvara.speedtest.common.NetWorkInfoUtility
import com.dvara.speedtest.common.Utils
import com.dvara.speedtest.databinding.FragmentSpeedTestBinding
import com.dvara.speedtest.db.entities.SpeedTest
import com.dvara.speedtest.ui.viewmodel.SpeedTestViewModel
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class SpeedTestFragment : Fragment() {

    var TAG = "SpeedTestFragment"

    private var speedTestViewModel: SpeedTestViewModel? = null
    private var _binding: FragmentSpeedTestBinding? = null

    private var mCircleProgressBar: CircleProgressBar? = null
    private var mCircleProgressBarShadowHide: CircleProgressBar? = null
    private var imageViewNeedle: ImageView? = null
    private var textViewCurrentDbCPB: TextView? = null
    private val textViewCPBLabels = arrayOfNulls<TextView>(9)
    private val textViewCPBLabelValues = intArrayOf(0, 20, 30, 50, 60, 70, 90, 100, 120)
    private var mRelativeLayoutGaugeCurrentDb: RelativeLayout? = null
    private var start: Button? = null
    private var submit: Button? = null
    private var textViewSpeed: TextView? = null
    private var editTextMobileNumber: EditText? = null
    private var textViewCurrentTime: TextView? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_speed_test, container, false)
        speedTestViewModel = ViewModelProvider(this).get(SpeedTestViewModel::class.java)
        binding.viewmodel = speedTestViewModel
        bindingView()
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun bindingView(){
        mRelativeLayoutGaugeCurrentDb = binding.relativeLayoutGaugeCurrentDb
        imageViewNeedle = binding.imageViewNeedle
        textViewCurrentDbCPB = binding.textViewCurrentDbCPB
        textViewCPBLabels[0] = binding.textView0CPB
        textViewCPBLabels[1] = binding.textView20CPB
        textViewCPBLabels[2] = binding.textView30CPB
        textViewCPBLabels[3] = binding.textView50CPB
        textViewCPBLabels[4] = binding.textView60CPB
        textViewCPBLabels[5] = binding.textView70CPB
        textViewCPBLabels[6] = binding.textView90CPB
        textViewCPBLabels[7] = binding.textView100CPB
        textViewCPBLabels[8] = binding.textView120CPB

        editTextMobileNumber  = binding.mobileNumber
        textViewCurrentTime  = binding.currentTime
        mCircleProgressBar = binding.myCpb
        mCircleProgressBarShadowHide = binding.myCpbShadowHide
        start = binding.start
        submit = binding.submit
        textViewSpeed = binding.mbps

        mCircleProgressBar?.max = 165
        mCircleProgressBarShadowHide?.max = 165
        mCircleProgressBar?.isDrawBackgroundOutsideProgress = true
        mCircleProgressBar?.setProgressBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorBackground))
        mCircleProgressBar?.setProgressStartColor(ContextCompat.getColor(requireContext(), R.color.colorCircularProgressBarBackground) )
        mCircleProgressBar?.setProgressEndColor(ContextCompat.getColor(requireContext(),R.color.colorCircularProgressBarBackground) )
        mCircleProgressBar?.progress = 0

        startAnimating()

        start?.setOnClickListener{
                var networkUtility = NetWorkInfoUtility()
                if (context?.let { it1 -> networkUtility.isNetWorkAvailableNow(it1) } == true) {
                    if (networkUtility.isMobileNetworkAvailable) {
                        Utils.toast(context,"Testing internet speed for Mobile Internet")
                    } else if (networkUtility.isWifiEnable) {
                        Utils.toast(context,"Testing internet speed for Wifi Internet")
                    }
                    checkInternetSpeed()
                }else{
                    Utils.toast(context,"Kindly check your internet connection and try again!")
                }
        }
        submit?.setOnClickListener{
        var mobileNumber = editTextMobileNumber?.text.toString()
        if(mobileNumber!=null && !mobileNumber.isEmpty()) {
            var speedTest = SpeedTest()
            speedTest.mobileNumber = mobileNumber
            speedTest.speed = textViewSpeed?.text.toString()
            speedTest.timeStamp = textViewCurrentTime?.text.toString()
            speedTestViewModel?.addSpeed(speedTest)
            Utils.toast(context,"Inserted into DB successfully")
            }else{
            Utils.toast(context,"Please enter the mobile number")
            }
        }


        var mobileNumber = AppPreferences.getValueString(Constants.PREF_MOBILE_NUMBER)
        if(mobileNumber!=null && !mobileNumber.isEmpty()){
            editTextMobileNumber?.setText(mobileNumber)
            editTextMobileNumber?.isVisible = false
        }else{
            editTextMobileNumber?.setText("")
            editTextMobileNumber?.isVisible = true
            editTextMobileNumber?.requestFocus()
            Utils.toast(context,"Unable to fetch mobile number over the phone.. Please enter the mobile number")
        }
    }

    override fun onResume() {
        super.onResume()
        updatingTime(textViewCurrentTime!!)
    }
    fun updatingTime(tvClock: TextView){
    val mHandler = Handler(getMainLooper())
        mHandler.postDelayed(object : Runnable {
        override fun run() {
            tvClock.text = Utils.currentTime()
            mHandler.postDelayed(this, 1000)
        }
    }, 10)
    }

     fun startAnimating() {
        /** starting animation  */
         mRelativeLayoutGaugeCurrentDb?.visibility = View.INVISIBLE
         imageViewNeedle?.visibility = View.INVISIBLE
        for (i in 0..8) {
            textViewCPBLabels[i]?.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorBackground))
        }
        val animatorGauge = ValueAnimator.ofInt(0, 121)
        animatorGauge.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            mCircleProgressBar?.progress = progress
        }
        animatorGauge.repeatCount = 0
        animatorGauge.duration = 1200
        animatorGauge.start()
        val animatorText = ValueAnimator.ofInt(0, 9)

        animatorText.addUpdateListener { animation ->
            val i = animation.animatedValue as Int
            if (i >= 0 && i < 9) textViewCPBLabels[i]?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.colorActiveGaugeText))
            if (i > 0) textViewCPBLabels[i - 1]?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.colorNotActiveGaugeText))
            if (i == 9) {
                imageViewNeedle?.visibility = View.VISIBLE
                mRelativeLayoutGaugeCurrentDb?.visibility = View.VISIBLE
                imageViewNeedle?.rotation = 0f
                mCircleProgressBar?.isDrawBackgroundOutsideProgress = true
                mCircleProgressBar?.setProgressBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorCircularProgressBarBackground) )
                mCircleProgressBar?.setProgressStartColor(ContextCompat.getColor( requireContext(), R.color.colorTransparent))
                mCircleProgressBar?.setProgressEndColor(ContextCompat.getColor(requireContext(),R.color.colorTransparent))
                mCircleProgressBar?.progress = 0
            }
        }
        animatorText.repeatCount = 0
        animatorText.duration = 800
        animatorText.startDelay = 1500
        animatorText.start()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun checkInternetSpeed(){
        val startTime: Long
        var endTime: Long
        var fileSize: Long
        val client = OkHttpClient()

        val request: Request = Request.Builder() .url("https://freepngimg.com/thumb/categories/1309.png").build()

        startTime = System.currentTimeMillis()

        Utils.toast(context,"Started")

        client.newCall(request).enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response?) {
                if(response?.message()=="OK") {
                    val responseHeaders: Headers = response!!.headers()
                    var i = 0
                    val size: Int = responseHeaders.size()
                    while (i < size) {
                        Log.d(TAG,  responseHeaders.name(i).toString() + ": " + responseHeaders.value(i))
                        i++
                    }
                    val input: InputStream = response.body().byteStream()
                    fileSize = try {
                        val bos = ByteArrayOutputStream()
                        val buffer = ByteArray(1024)
                        while (input.read(buffer) != -1) {
                            bos.write(buffer)
                        }
                        bos.size().toLong()
                    } finally {
                        input.close()
                    }

                endTime = System.currentTimeMillis()

                // calculate how long it took by subtracting endtime from starttime
                val timeTakenMills =Math.floor((endTime - startTime).toDouble()) // time taken in milliseconds
                val timeTakenSecs = timeTakenMills / 1000 // divide by 1000 to get time in seconds
                val kilobytePerSec = Math.round(1024 / timeTakenSecs).toInt()

                // get the download speed by dividing the file size by time taken to download
                val speed = fileSize / timeTakenMills

                updateAnimatorValueOnGauge(speed)
                }
            }
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
            }
        })
    }
    fun updateAnimatorValueOnGauge(speed: Double) {
        Handler(getMainLooper()).post {
            val downSpeed = Math.round(speed).toInt()
            val animatorValueOnGauge = ValueAnimator.ofInt(0, downSpeed)
            animatorValueOnGauge.addUpdateListener { animation ->
                val progress = animation.animatedValue as Int
                mCircleProgressBar!!.progress = progress
                mCircleProgressBarShadowHide!!.progress = progress
                imageViewNeedle!!.rotation = (2.19166667 * progress).toFloat()
                textViewCurrentDbCPB!!.text = String.format("%d", progress)
                textViewSpeed?.text = Utils.roundOffDecimal(speed).toString() + " Mbps"
                if(speed>0){
                    Handler(Looper.getMainLooper()).postDelayed({ resetAnimatorValueOnGauge() }, Constants.ANIMATION_DELAY)
                }
                for (i in 0..8) {
                    if (textViewCPBLabelValues[i] < progress) textViewCPBLabels[i]!!
                        .setTextColor(ContextCompat.getColor( requireContext(), R.color.colorActiveGaugeText )
                        ) else textViewCPBLabels[i]!!
                        .setTextColor( ContextCompat.getColor(requireContext(),R.color.colorNotActiveGaugeText )
                        )
                }
            }
            animatorValueOnGauge.repeatCount = 0
            animatorValueOnGauge.duration = 2000
            animatorValueOnGauge.startDelay = 1000
            animatorValueOnGauge.start()
        }

    }
    fun resetAnimatorValueOnGauge(){
        Handler(getMainLooper()).post {
            val animatorValueOnGauge = ValueAnimator.ofInt(0, 0)
            animatorValueOnGauge.addUpdateListener { animation ->
                val progress = animation.animatedValue as Int
                mCircleProgressBar!!.progress = progress
                mCircleProgressBarShadowHide!!.progress = progress
                imageViewNeedle!!.rotation = (2.19166667 * progress).toFloat()
                textViewCurrentDbCPB!!.text = String.format("%d", progress)
                              for (i in 0..8) {
                    if (textViewCPBLabelValues[i] < progress) textViewCPBLabels[i]!!
                        .setTextColor(ContextCompat.getColor( requireContext(), R.color.colorActiveGaugeText )
                        ) else textViewCPBLabels[i]!!
                        .setTextColor( ContextCompat.getColor(requireContext(),R.color.colorNotActiveGaugeText )
                        )
                }
            }
            animatorValueOnGauge.repeatCount = 0
            animatorValueOnGauge.duration = 2000
            animatorValueOnGauge.startDelay = 2400
            animatorValueOnGauge.start()
        }
    }
}
