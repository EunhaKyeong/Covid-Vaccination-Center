package com.example.covid_vaccine.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.covid_vaccine.MyApplication.Companion.ERROR_NETWORK
import com.example.covid_vaccine.R
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

        //예방접종센터 데이터 조회
        splashViewModel.saveCenters(10)

        lifecycleScope.launch(Dispatchers.Main) {
            while (binding.splashPb.progress < 100) {
                delay(20)   //2초간 프로그래스바가 100%가 되도록

                binding.splashPb.progress = ++progress

                when (progress) {
                    80 -> { //80%가 됐을 때
                        if (splashViewModel.centerCnt.value?:0 < 100) { //데이터 저장이 완료되지 않았으면 잠시 멈추고 대기
                            cancel()
                            break
                        }
                    }

                    100 -> {    //100%가 됐을 때 데이터 저장이 완료 됐으면 MapActivity로 이동
                        moveToMapActivity()
                    }
                }
            }
        }
    }

    //MapActivity로 이동하는 함수
    private fun moveToMapActivity() {
        val intent: Intent = Intent(baseContext, MapActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //MapActivity에서 뒤로 갔을 때 SplashActivity가 보이지 않도록
        startActivity(intent)
    }

    private fun observe() {
        splashViewModel.error.observe(this, Observer {
            when (it) {
                ERROR_NETWORK -> {
                    AlertDialog.Builder(this@SplashActivity)
                        .setTitle(getString(R.string.title_noti))
                        .setMessage(getString(R.string.msg_network_error1))
                        .setPositiveButton(getString(R.string.action_ok)) { dialog, which ->
                            finishAffinity()
                        }
                        .setCancelable(false)
                        .show()
                }
                else -> Toast.makeText(baseContext, it, Toast.LENGTH_SHORT).show()
            }
        })

        splashViewModel.centerCnt.observe(this, Observer {
            if (it==100 && binding.splashPb.progress<100) { //100개 데이터를 가지고 왔고, 프로그래스바가 80%에서 멈춰있을 때
                progress = binding.splashPb.progress

                lifecycleScope.launch(Dispatchers.Main) {
                    while (progress < 100) {
                        delay(20)   //0.4초 안에 나머지 20% 채우기
                        binding.splashPb.progress = ++progress
                    }

                    moveToMapActivity() //100%가 되면 MapActivity로 이동
                }
            }
        })
    }
}