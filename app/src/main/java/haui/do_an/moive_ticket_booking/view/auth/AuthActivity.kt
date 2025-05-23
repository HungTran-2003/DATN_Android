package haui.do_an.moive_ticket_booking.view.auth

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.view.base.ChangePasswordFragment
import haui.do_an.moive_ticket_booking.view.base.VerifyOTPFragment
import haui.do_an.moive_ticket_booking.view.dialog.NetworkErrorDialog
import haui.do_an.moive_ticket_booking.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(){
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment())
                .addToBackStack("")
                .commit()
        }


    }

    fun navigateToRegister() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, RegisterFragment())
            .addToBackStack("login")  // Đưa vào back stack để quay lại được
            .commit()
    }

    fun navigateToLogin() {
        supportFragmentManager.popBackStack("login",FragmentManager.POP_BACK_STACK_INCLUSIVE )  // Quay lại LoginFragment
    }

    fun backToFragmentBefore() {
        supportFragmentManager.popBackStack()  // Quay lại RegisterFragment
    }

    fun navigateToForgotPassword() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ForgotPasswordFragment())
            .addToBackStack("login")  // Đưa vào back stack để quay lại được
            .commit()
    }

    fun navigateToChangePassword(bundle: Bundle) {
        val fragment = ChangePasswordFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack("")  // Đưa vào back stack để quay lại được
            .commit()
    }



//    private fun checkConnectInternet(){
//        viewModel.networkError.observe(this) { hasInternet ->
//            if(hasInternet == false){
//                val dialog = NetworkErrorDialog.Companion.newInstance()
//                dialog.setListener(object : NetworkErrorDialog.NetworkErrorListener {
//                    override fun onRetry() {
//                        viewModel.onRetry()
//                    }
//
//                    override fun onCancel() {
//                        finish()
//                    }
//                })
//                dialog.show(supportFragmentManager, "check_internet_dialog")
//            }
//        }
//        viewModel.checkInternetConnection()
//    }

    fun navigateToVerifyOTP(bundle: Bundle?) {
        val fragment = VerifyOTPFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack("")  // Đưa vào back stack để quay lại được
            .commit()
    }

}