package com.highresults.sunset

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var sceneView: View
    private lateinit var seaView: View
    private lateinit var sunView: View
    private lateinit var sunMirrorView: View
    private lateinit var skyView: View
    private var counter: Int = 0
    private lateinit var heightAnimator: ObjectAnimator
    private lateinit var heightMirrorAnimator: ObjectAnimator
    private lateinit var sunsetSkyAnimator: ObjectAnimator
    private lateinit var nightSkyAnimator: ObjectAnimator
    private var isEnded = true

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(
                this,
                R.color.blue_sky
        )
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(
                this,
                R.color.sunset_sky
        )
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(
                this,
                R.color.night_sky
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById(R.id.scene)
        seaView = findViewById(R.id.sea)
        sunView = findViewById(R.id.sun)
        sunMirrorView = findViewById(R.id.sun_mirror)
        skyView = findViewById(R.id.sky)
        sunView.post {
            val sunYStart = sunView.top.toFloat()
            val sunYEnd = skyView.height.toFloat()
            heightAnimator = ObjectAnimator
                    .ofFloat(sunView, "y", sunYStart, sunYEnd)
                    .setDuration(3000)
                    .apply { interpolator = AccelerateInterpolator() }
            heightMirrorAnimator = ObjectAnimator
                    .ofFloat(sunMirrorView, "y", sunMirrorView.top.toFloat(), -sunMirrorView.height.toFloat())
                    .setDuration(3000)
                    .apply { interpolator = AccelerateInterpolator() }

            heightAnimator.addListener(object : Animator.AnimatorListener {

                override fun onAnimationStart(animation: Animator?) {
                    if (!isEnded) {
                        heightAnimator.reverse()
                        heightMirrorAnimator.reverse()
                    }
                    isEnded = false

                }

                override fun onAnimationEnd(animation: Animator?) {
                    isEnded = true
                    if (counter % 2 == 1)
                        nightSkyAnimator.start()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

            })

        }

        sunsetSkyAnimator = ObjectAnimator
                .ofInt(skyView, "backgroundColor", blueSkyColor, sunsetSkyColor)
                .setDuration(3000)
                .apply { setEvaluator(ArgbEvaluator()) }

        nightSkyAnimator = ObjectAnimator
                .ofInt(
                        skyView, "backgroundColor",
                        sunsetSkyColor, nightSkyColor
                )
                .setDuration(1500)
                .apply { setEvaluator(ArgbEvaluator()) }



        sceneView.setOnClickListener {
            if (++counter % 2 == 1)
                startSunsetAnimation(true)
            else
                startSunsetAnimation(false)
        }
    }

    private fun startSunsetAnimation(isSunset: Boolean) {

        if (isSunset && isEnded) {

            heightAnimator.start()
            heightMirrorAnimator.start()
            sunsetSkyAnimator.start()
        } else {
            heightAnimator.reverse()
            heightMirrorAnimator.reverse()
            sunsetSkyAnimator.reverse()
        }

    }


}