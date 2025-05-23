package haui.do_an.moive_ticket_booking.view.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.FragmentPaymentBinding
import haui.do_an.moive_ticket_booking.viewmodel.UserViewModel


class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding
    private val viewModel: UserViewModel by activityViewModels()

    private var isDeepLinkHandled = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_payment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaymentBinding.bind(view)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.url.observe(viewLifecycleOwner) { url ->
            url?.let {
                openWebView(it)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showErrorDialog(it)
            }
        }
    }

    private fun openWebView(url: String){
        binding.webViewPayment.settings.javaScriptEnabled = true
        binding.webViewPayment.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in API 24")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Log.d("WebView", "shouldOverrideUrlLoading (String): $url")
                return handleDeepLinkInWebView(url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                Log.d("WebView", "shouldOverrideUrlLoading (WebResourceRequest): $url")
                return handleDeepLinkInWebView(url)
            }

            private fun handleDeepLinkInWebView(url: String): Boolean {
                if (url.startsWith("myapp://")) {
                    try {
                        val uri = Uri.parse(url)
                        getResult(uri)
                        return true // Ngăn WebView tải URL
                    } catch (e: Exception) {
                        Log.e("WebView", "Lỗi Deep Link: ${e.message}")
                        Toast.makeText(requireContext(), "Lỗi Deep Link: ${e.message}", Toast.LENGTH_SHORT).show()
                        return true
                    }
                }
                return false // Để WebView xử lý các URL khác
            }
        }
        binding.webViewPayment.loadUrl(url)
    }

    private fun getResult(uri: Uri?){
        if (isDeepLinkHandled) {
            Log.d("DeepLink", "Deep Link đã được xử lý, bỏ qua: $uri")
            return
        }
        uri?.let {
            val host = it.host
            Log.d("DeepLink", "Fragment received: $uri")
            when (host) {
                "success" -> {
                    isDeepLinkHandled = true
                    val orderCode = it.getQueryParameter("orderCode")
                    Toast.makeText(requireContext(), "Thanh toán thành công! Order: $orderCode", Toast.LENGTH_LONG).show()
                    requireActivity().intent?.data = null
                    (activity as? UserActivity)?.backToFilmDetail()
                }
                "cancel" -> {
                    isDeepLinkHandled = true
                    Toast.makeText(requireContext(), "Thanh toán bị hủy!", Toast.LENGTH_LONG).show()
                    requireActivity().intent?.data = null
                    (activity as? UserActivity)?.backToFilmDetail()
                }
            }
        }
    }

    private fun showErrorDialog(error: String) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(error)
        builder.setMessage("Vui lòng thử lại sau")
        Log.d("error", error)

        builder.setPositiveButton("Ok") { dialog, which ->
            (activity as UserActivity).backToPaymentComfirm()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
        viewModel.clearErrorMessage()
    }

}