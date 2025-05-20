package haui.do_an.moive_ticket_booking.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.net.InetSocketAddress
import java.net.Socket


class NetworkErrorDialog : DialogFragment() {
    private lateinit var listener: NetworkErrorListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Không có kết nối internet")
            .setMessage("vui lòng thử lại")
            .setPositiveButton("Thử lại") { _, _ -> listener.onRetry()

            }
            .setNegativeButton("Huỷ") { _, _ ->  listener.onCancel()}
            .create()
    }

    fun setListener(listener: NetworkErrorListener) {
        this.listener = listener
    }

    companion object {
        fun newInstance(): NetworkErrorDialog {
            return NetworkErrorDialog()
        }
    }

    interface NetworkErrorListener{
        fun onRetry()
        fun onCancel()
    }
}