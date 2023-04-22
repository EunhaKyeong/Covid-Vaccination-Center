package com.example.covid_vaccine.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.example.covid_vaccine.R
import com.example.covid_vaccine.databinding.ActivityMapBinding
import com.example.covid_vaccine.entity.CenterEntity
import com.example.covid_vaccine.viewmodel.MapViewModel
import com.google.android.gms.location.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : FragmentActivity(), OnMapReadyCallback {
    private val mapViewModel: MapViewModel by viewModels()

    //위치 권한을 요청하는 런처
    @SuppressLint("MissingPermission")
    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions() ) { permissions ->
        when {
            //권한 허용
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) || permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                binding.isLoading = true    //내 위치를 얻으러 가는 시간이 길어서 로딩 Progressbar를 보여줌.
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCbInSetMap, null)   //현재 위치를 얻기 위해 requestLocationUpdates 호출.
            }
            //권한 허용X
            else -> {
                locationPermissionDeniedAlert.show()
            }
        }
    }
    private val markers: MutableList<Marker> = mutableListOf()

    private lateinit var binding: ActivityMapBinding
    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCbInSetMap: LocationCallback
    private lateinit var locationPermissionDeniedAlert: AlertDialog.Builder
    private lateinit var centers: List<CenterEntity>
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.activity = this@MapActivity
        setContentView(binding.root)

        observe()
        initLocation()
        initNaverMap()
        initLocationPermissionDeniedAlert()
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        naverMap.uiSettings.isLogoClickEnabled = true   //네이버맵 로고 보여주기
        naverMap.moveCamera(CameraUpdate.zoomTo(10.0))  //카메라 줌인
        naverMap.setOnMapClickListener { pointF, latLng ->  //네이버맵 클릭 리스너
            binding.mapInfoLl.visibility = View.GONE    //정보안내창이 보여지고 있으면 GONE으로 상태 변경
        }

        mapViewModel.getCenters()   //Room에서 예방접종센터 리스트 데이터 얻어오기
    }

    //현재 위치를 가져올  때 사용되는 FusedLocationProviderClient 관련 세팅
    private fun initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //위치 데이터를 주기적으로 가져오기 위해 설정하는 코드. 하지만 우리 기능에서는 필요할 때(현재 위치 버튼 클릭 시)만 사용함.
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100).build()

        //위치 데이터를 얻은 후에 발생하는 콜백 객체 생성.
        locationCbInSetMap = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.lastLocation == null) {  //위치가 null이면 에러메시지 보여주기.
                    Toast.makeText(baseContext, getString(R.string.msg_null_current_location), Toast.LENGTH_SHORT).show()
                } else {    //위치가 null이 아니면 전달 받은 위치로 카메라 이동.
                    val cameraUpdate = CameraUpdate
                        .scrollAndZoomTo(LatLng(locationResult.lastLocation!!.latitude, locationResult.lastLocation!!.longitude), 16.0)
                        .animate(CameraAnimation.Fly, 2000)
                        .finishCallback {
                            binding.isLoading = false
                        }

                    naverMap.moveCamera(cameraUpdate)
                }

                //말했듯 필요할 때만 사용하므로 주기적으로 위치를 가져오는 locationRequest를 삭제.
                fusedLocationClient.removeLocationUpdates(this)
            }
        }
    }

    //네이버맵 관련 세팅
    private fun initNaverMap() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    //위치 권한 허용하지 않았을 때 보여줄 Alert 창 세팅
    private fun initLocationPermissionDeniedAlert() {
        locationPermissionDeniedAlert = AlertDialog.Builder(this@MapActivity)
            .setTitle(getString(R.string.title_noti))
            .setMessage(getString(R.string.msg_denied_current_location))
            .setPositiveButton(getString(R.string.title_setting)) { dialog, which ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.action_cancel)) { dialog, which ->

            }
            .setCancelable(false) // 버튼을 눌러야만 창이 사라짐
    }

    //마커 만들기
    private fun setMarkers() {
        centers.forEachIndexed {idx, center ->
            val marker: Marker = Marker().apply {
                position = LatLng(center.lat, center.lng)

                map = naverMap

                icon = MarkerIcons.BLACK
                iconTintColor = if (center.centerType=="중앙/권역") {   //중앙/권역이면 빨간색 마커
                    Color.RED
                } else {    //지역이면 파란색 마커
                    Color.BLUE
                }

                //마커 클릭 리스너
                setOnClickListener {
                    val cameraUpdate = CameraUpdate
                        .scrollAndZoomTo(LatLng(center.lat, center.lng), 16.0)
                        .animate(CameraAnimation.Fly, 1000)
                    naverMap.moveCamera(cameraUpdate)

                    if (binding.mapInfoLl.visibility == View.GONE) {    //정보창이 안보이는 경우 보이게
                        binding.mapInfoLl.visibility = View.VISIBLE
                        binding.center = center
                    } else {    //정보창이 보이고 있을 경우
                        if (binding.center?.id==center.id) {    //현재 클릭한 마커면 정보창 GONE
                            binding.mapInfoLl.visibility = View.GONE
                        } else {    //다른 마커의 정보이면 클릭한 마커 데이터가 보이도록
                            binding.center = center
                        }
                    }

                    true
                }
            }

            markers.add(marker)
        }
    }

    private fun checkGPS(): Boolean {
        val locationManager = baseContext.getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun checkNetwork(): Boolean {
        val connectivityManager: ConnectivityManager = baseContext.getSystemService(ConnectivityManager::class.java)
        val network: Network = connectivityManager.activeNetwork ?: return false
        val actNetwork: NetworkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            else -> false
        }
    }

    private fun observe() {
        mapViewModel.error.observe(this, Observer {
            Toast.makeText(baseContext, it, Toast.LENGTH_SHORT).show()
        })

        mapViewModel.centers.observe(this, Observer {
            centers = it
            setMarkers()
        })
    }

    //현재 위치 버튼 클릭 시 호출되는 함수
    fun setCurrentLocation() {
        binding.mapInfoLl.visibility = View.GONE

        val gpsCheck: Boolean = checkGPS()
        val networkCheck: Boolean = checkNetwork()

        if (gpsCheck && networkCheck) {
            locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        } else if (!gpsCheck) {
            Toast.makeText(baseContext, getString(R.string.msg_gps_error), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(baseContext, getString(R.string.msg_network_error2), Toast.LENGTH_SHORT).show()
        }
    }
}