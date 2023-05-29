package com.constanta.core.presentation.ext.ui

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView

fun NavController.setupBottomNavViewVisibility(destinations: Array<Int>, bottomNavigationView: BottomNavigationView) {
    this.addOnDestinationChangedListener {_, destination, _ ->
        Handler(Looper.getMainLooper()).post {
            when (destination.id in destinations) {
                true -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
                false -> {
                    bottomNavigationView.visibility = View.GONE
                }
            }
        }
    }
}