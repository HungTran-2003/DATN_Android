package haui.do_an.moive_ticket_booking.view.admin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.ActivityAdminBinding
import haui.do_an.moive_ticket_booking.view.auth.AuthActivity
import haui.do_an.moive_ticket_booking.view.base.FilmDetailFragment
import javax.inject.Inject

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toggle = ActionBarDrawerToggle(this, binding.main, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.main.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home_film -> HomeFilmFragment()
                R.id.nav_home_showtime -> HomeShowtimeFragment()
                R.id.nav_home_cinema -> HomeCinemasFragment()
                R.id.nav_home_coupon -> HomeCouponsFragment()
                else -> HomeUsersFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, selectedFragment)
                .commit()

            true
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFilmFragment())
                .commit()
        }
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tvUserName).text = intent.getStringExtra("username").toString()
        Log.d("Username", intent.getStringExtra("username").toString())

        binding.navView.setNavigationItemSelectedListener { item ->
            clickItemInNavView(item.itemId)
            true
        }
    }

    fun navigateToAddMovie(bundle: Bundle) {
        val addMovieFragment = AddMovieFragment()
        addMovieFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, addMovieFragment)
            .addToBackStack("homeFirm")
            .commit()
    }

    fun navigateToFilmDetail(bundle: Bundle){
        val filmDetailFragment = FilmDetailFragment()
        filmDetailFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, filmDetailFragment)
            .addToBackStack("homeFirm")
            .commit()
    }

    fun backToHomeFirm(){
        supportFragmentManager.popBackStack("homeFirm", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun navigateToAddShowtime(bundle: Bundle){
        val addShowtimeFragment = AddShowtimeFragment()
        addShowtimeFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, addShowtimeFragment)
            .addToBackStack("homeShowtime")
            .commit()
    }

    fun navigateToAddCoupon(){
        val addCouponFragment = AddCouponFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, addCouponFragment)
            .addToBackStack("homeCoupon")
            .commit()
    }

    fun backToHomeCoupon(){
        supportFragmentManager.popBackStack("homeCoupon", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun backToHomeShowtime(){
        supportFragmentManager.popBackStack("homeShowtime", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun clickItemInNavView(item: Int){
        when(item){
            R.id.nav_profile -> {
                Log.d("TAG", "nav_profile")
            }
            R.id.nav_change_password -> {
                Log.d("TAG", "nav_change_password")
            }
            else -> {
                sharedPreferences.edit()
                    .putBoolean("saveAccount", false)
                    .apply()
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }



}