package haui.do_an.moive_ticket_booking.view.base

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.ActivityWellcomeBinding
import haui.do_an.moive_ticket_booking.view.auth.AuthActivity
import haui.do_an.moive_ticket_booking.viewmodel.WcViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.activity.viewModels
import haui.do_an.moive_ticket_booking.DTO.CityDTO

@AndroidEntryPoint
class WellcomeActivity : AppCompatActivity() {
    private var isAppReady: Boolean = false
    private lateinit var binding: ActivityWellcomeBinding
    private val viewModel: WcViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !isAppReady }

        val preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val isFirstLaunch = preferences.getBoolean("isFirstLaunch", true)

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            if (!isFirstLaunch){
                startActivity(Intent(this@WellcomeActivity, AuthActivity::class.java))
                finish()
            }
            isAppReady = true
        }

        binding = ActivityWellcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.errorMessage.observe(this) {
                errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            println(errorMessage)
        }

        viewModel.cities.observe(this) { cities -> setupSpinnerCity(cities)}


        binding.complete.setOnClickListener {
            saveCity()
            startActivity(Intent(this@WellcomeActivity, AuthActivity::class.java))
        }

    }

    private fun setupSpinnerCity(cities: List<CityDTO>) {
        val cityNames = cities.map { it.name }

        val adapter = ArrayAdapter(this, R.layout.item_spinner, cityNames)
        binding.citys.adapter = adapter

    }


    private fun saveCity(){
        val selectedCinema = binding.citys.selectedItem.toString()
        val preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("selectedCinema", selectedCinema)
        editor.putBoolean("isFirstLaunch", false)
        editor.apply()

    }
}