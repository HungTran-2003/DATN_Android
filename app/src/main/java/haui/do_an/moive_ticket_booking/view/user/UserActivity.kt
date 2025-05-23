package haui.do_an.moive_ticket_booking.view.user

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.ActivityUserBinding
import androidx.core.view.WindowCompat
import com.example.cinemaapp.SelectSeatFragment
import haui.do_an.moive_ticket_booking.view.auth.AuthActivity
import haui.do_an.moive_ticket_booking.view.base.FilmDetailFragment
import haui.do_an.moive_ticket_booking.view.base.VerifyOTPFragment
import javax.inject.Inject

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = false
        userName = intent.getStringExtra("username").toString()
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        }
        setBottomNavigation()
        setUpNavView()
    }

    fun backToLogin(){
        sharedPreferences.edit().putBoolean("saveAccount", false).apply()
        intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun navigateToFilmDetail(bundle: Bundle){
        val filmDetailFragment = FilmDetailFragment()
        filmDetailFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, filmDetailFragment)
            .addToBackStack("home")
            .commit()
    }

    fun backToHome(){
        supportFragmentManager.popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun backToFilmDetail(){
        supportFragmentManager.popBackStack("filmDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun navigateToSelectShowTime(bundle: Bundle){
        val selectShowTimeFragment = SelectShowTimeFragment()
        selectShowTimeFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, selectShowTimeFragment)
            .addToBackStack("filmDetail")
            .commit()
    }

    fun navigateToSelectSeat(bundle: Bundle){
        val selectSeatFragment = SelectSeatFragment()
        selectSeatFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, selectSeatFragment)
            .addToBackStack("selectShowTime")
            .commit()
    }

    fun navigateToPaymentComfirm(bundle: Bundle){
        val paymentComfirmFragment = PaymentConfirmFragment()
        paymentComfirmFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, paymentComfirmFragment)
            .addToBackStack("selectSeat")
            .commit()
    }

    fun backToSelectSeat(){
        supportFragmentManager.popBackStack("selectSeat", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun navigateToChoseCoupon(bundle: Bundle){
        val choseCouponFragment = ChoseCouponFragment()
        choseCouponFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, choseCouponFragment)
            .addToBackStack("paymentComfirm")
            .commit()
    }

    fun backToPaymentComfirm(){
        supportFragmentManager.popBackStack("paymentComfirm", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun setBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_ticket -> BookingFragment()
                else -> SettingFragment()
            }
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, selectedFragment)
                .commit()
            true
        }
    }

    private fun setUpNavView(){
        val toggle = ActionBarDrawerToggle(this, binding.main, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.main.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tvUserName).text = userName

        binding.navView.setNavigationItemSelectedListener { item ->
            clickItemInNavView(item.itemId)
            true
        }
    }

    fun navigateToPayment(){
        val paymentFragment = PaymentFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, paymentFragment)
            .addToBackStack("payment")
            .commit()
    }

    fun navigateToTickerFragment(bundle: Bundle){
        val ticketFragment = TicketFragment()
        ticketFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ticketFragment)
            .addToBackStack("ticket")
            .commit()
    }

    fun navigateToBookingFragment(bundle: Bundle){
        val bookingFragment = BookingFragment()
        bookingFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, bookingFragment)
            .addToBackStack("setting")
            .commit()
    }

    fun backToSetting(){
        supportFragmentManager.popBackStack("setting", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun navigateToReviewFragment(bundle: Bundle){
        val reviewFragment = ReviewFragment()
        reviewFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, reviewFragment)
            .addToBackStack("booking")
            .commit()
    }

    fun navigateToVerifyOTP(bundle: Bundle){
        val sentOTPFragment = VerifyOTPFragment()
        sentOTPFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, sentOTPFragment)
            .addToBackStack("review")
            .commit()
    }

    fun navigateToProfile() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ProfileFragment())
            .addToBackStack("login")  // Đưa vào back stack để quay lại được
            .commit()
    }

    fun backToFragmentBefore(){
        supportFragmentManager.popBackStack()
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

    fun startNav(){
        if (!binding.main.isDrawerOpen(GravityCompat.END)) {
            binding.main.openDrawer(GravityCompat.END)
        }
    }

}