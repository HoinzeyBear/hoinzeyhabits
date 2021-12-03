package com.example.hoinzeyshabits.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Log
import android.view.View

object AnimationUtils {
    fun animateIntoVisibility(view: View) {
        view.animate()
            .alpha(1f)
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    Log.d("AnimationUtils", "Tick in onAnimationStart")
                    super.onAnimationStart(animation)
                }

                override fun onAnimationRepeat(animation: Animator) {
                    Log.d("AnimationUtils", "Tick in onAnimationRepeat")
                    super.onAnimationRepeat(animation)
                }

                override fun onAnimationEnd(animation: Animator) {
                    Log.d("AnimationUtils", "Tick in onAnimationEnd")
                    view.visibility = View.VISIBLE
                }
            })
    }

    fun animateOutOfVisibility(view: View) {
        view.animate()
            .alpha(0f)
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    Log.d("AnimationUtils", "Tick in onAnimationStart")
                    super.onAnimationStart(animation)
                }

                override fun onAnimationRepeat(animation: Animator) {
                    Log.d("AnimationUtils", "Tick in onAnimationRepeat")
                    super.onAnimationRepeat(animation)
                }

                override fun onAnimationEnd(animation: Animator) {
                    Log.d("AnimationUtils", "Tick in onAnimationEnd")
                    view.visibility = View.GONE
                }
            })
    }
}