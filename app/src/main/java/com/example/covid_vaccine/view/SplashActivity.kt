package com.example.covid_vaccine.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.covid_vaccine.databinding.ActivitySplashBinding
import com.example.covid_vaccine.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    private var progress: Int = 0

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observe()

        /*CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..10) {
                splashViewModel.getCenters(i)
                delay(500)
            }
        }*/

        splashViewModel.deleteAll()

        lifecycleScope.launch(Dispatchers.Main) {
            while (binding.splashPb.progress < 100) {
                delay(20)

                binding.splashPb.progress = ++progress

                when (progress) {
                    80 -> {
                        if (splashViewModel.centerCnt.value?:0 < 100) {
                            Toast.makeText(baseContext, "여기1 ${binding.splashPb.progress}", Toast.LENGTH_SHORT).show()
                            cancel()
                            break
                        } else {
                            Toast.makeText(baseContext, "여기2", Toast.LENGTH_SHORT).show()
                        }
                    }

                    100 -> {
                        moveToMapActivity()
                    }
                }
            }
        }
    }

    private fun moveToMapActivity() {
        val intent: Intent = Intent(baseContext, MapActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun observe() {
        splashViewModel.centerCnt.observe(this, Observer {
            Log.i("SplashActivity", "centerCent: $it, PB progress: ${binding.splashPb.progress}")

            if (it==100 && binding.splashPb.progress<100) {
                progress = binding.splashPb.progress

                lifecycleScope.launch(Dispatchers.Main) {
                    while (progress < 100) {
                        delay(20)
                        binding.splashPb.progress = ++progress
                    }

                    moveToMapActivity()
                }
            }
        })
    }
}