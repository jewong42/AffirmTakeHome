package com.affirm.takehome

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.affirm.takehome.dagger.ViewModelFactory
import com.affirm.takehome.adapter.RestaurantAdapter
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

private const val LOCATION_PERMISSION_CODE = 101
private const val THUMB_UP = R.drawable.thumb_up
private const val THUMB_DOWN = R.drawable.thumb_down
private const val TAG = "MainActivity"

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mViewModel : MainViewModel

    private var animating = false

    private val restaurantAdapter by lazy {
        RestaurantAdapter()
    }

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        initObservers()
        viewPager.adapter = restaurantAdapter
        // Only allow button input, swiping not allowed
        viewPager.isUserInputEnabled = false

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val leftToConsume = (viewPager?.adapter?.itemCount ?: 0) - position
                if (leftToConsume <= THRESHOLD_SIZE) checkAndRequestPermissionsForLocation()
                super.onPageSelected(position)
            }
        })

        yesButton.setOnClickListener {
            // Make sure the previous animation finishes
            val isLoading = progress_bar.visibility == View.VISIBLE
            val hasMore = viewPager.currentItem + 1 < (viewPager.adapter?.itemCount ?: 0)
            if (!animating && hasMore && !isLoading) {
                mViewModel.incrementYesCount()
                animateIcon(THUMB_UP)
            }
        }

        noButton.setOnClickListener {
            val isLoading = progress_bar.visibility == View.VISIBLE
            val hasMore = viewPager.currentItem + 1 < (viewPager.adapter?.itemCount ?: 0)
            if (!animating && hasMore && !isLoading) {
                mViewModel.incrementNoCount()
                animateIcon(THUMB_DOWN)
            }
        }
        if (savedInstanceState == null) checkAndRequestPermissionsForLocation()
    }

    private fun initObservers() {
        mViewModel.loadingVisibility.observe(this, Observer { visibility ->
            progress_bar.visibility = visibility
        })
        mViewModel.showError.observe(this, Observer { error ->
            if (!error.isNullOrEmpty()) Snackbar.make(main_activity_container, error, Snackbar.LENGTH_SHORT).show()
        })
        mViewModel.restaurants.observe(this, Observer { list ->
            val newRestaurants = list.toList().subList(restaurantAdapter.itemCount, list.size)
            restaurantAdapter.addRestaurants(newRestaurants)
        })
        mViewModel.yesCount.observe(this, Observer { count ->
            viewPager.currentItem = viewPager.currentItem + 1
            yesCounterText.text = count.toString()
        })
        mViewModel.noCount.observe(this, Observer { count ->
            viewPager.currentItem = viewPager.currentItem + 1
            noCounterText.text = count.toString()
        })
    }

    private fun animateIcon(drawable: Int) {
        animating = true
        icon.setImageDrawable(ContextCompat.getDrawable(this, drawable))
        icon.alpha = 0.5f
        icon.visibility = View.VISIBLE
        icon.animate()
            .alpha(1f)
            .setDuration(300)
            .scaleX(2f)
            .scaleY(2f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    icon.visibility = View.GONE
                    animating = false
                }
            })
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                loadLocation()
            } else {
                Toast.makeText(this, getString(R.string.no_permission), Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkAndRequestPermissionsForLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        } else {
            loadLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun loadLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location == null) {
                // request the location
                fusedLocationProviderClient.requestLocationUpdates(
                    LocationRequest.create(),
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)

                            locationResult.locations.lastOrNull().let { location ->
                                if (location == null) {
                                    Log.d(TAG, "Location load fail")
                                    false
                                } else {
                                    mViewModel.getRestaurants(location)
                                    true
                                }
                            }
                            fusedLocationProviderClient.removeLocationUpdates(this)
                        }
                    },
                    null
                )
            } else {
                mViewModel.getRestaurants(location)
            }
        }
    }

    companion object {
        const val THRESHOLD_SIZE = 10
    }
}