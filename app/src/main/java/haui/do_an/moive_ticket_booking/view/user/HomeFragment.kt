package haui.do_an.moive_ticket_booking.view.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.adapter.user.ListMovieAdapter
import haui.do_an.moive_ticket_booking.databinding.FragmentAddShowtimeBinding
import haui.do_an.moive_ticket_booking.databinding.FragmentHomeBinding
import haui.do_an.moive_ticket_booking.viewmodel.UserViewModel

class HomeFragment : Fragment() {

    private val viewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding

    private var isLoading = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        viewModel.getMovieShowing()
        viewModel.getMovieComming()
        viewModel.getMovieSpecial()

        setAdapterMovieShowing()
        setAdapterMovieComing()
        setAdapterMovieSpecial()
        error()
        clickMenu()
    }

    private fun setAdapterMovieShowing(){
        val adapter = ListMovieAdapter(requireContext()) {
            val bundle = Bundle()
            bundle.putInt("movieId", it.id)
            Log.d("movieId", it.id.toString())
            (activity as UserActivity).navigateToFilmDetail(bundle)
            isLoading = true
        }
        binding.rvRecommended.adapter = adapter
        viewModel.movieShowing.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            changeToMainUi()
        }
    }

    private fun setAdapterMovieComing(){
        val adapter = ListMovieAdapter(requireContext()) {
            val bundle = Bundle()
            bundle.putInt("movieId", it.id)
            Log.d("movieId", it.id.toString())
            (activity as UserActivity).navigateToFilmDetail(bundle)
            isLoading = true
        }
        binding.rvNowPlaying.adapter = adapter
        viewModel.movieComing.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setAdapterMovieSpecial(){
        val adapter = ListMovieAdapter(requireContext()) {
            val bundle = Bundle()
            bundle.putInt("movieId", it.id)
            Log.d("movieId", it.id.toString())
            (activity as UserActivity).navigateToFilmDetail(bundle)
            isLoading = true
        }
        binding.rvHotMovies.adapter = adapter
        viewModel.movieSpecial.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun changeToMainUi(){
        if (isLoading){
            binding.viewSwitcher.displayedChild = 1
            isLoading = false
        }
    }

    private fun error(){
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showErrorDialog(it)
            }
        }
    }

    private fun showErrorDialog(error: String) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(error)
        builder.setMessage("Vui lòng thử lại sau")

        builder.setPositiveButton("Ok") { dialog, which ->
            (activity as UserActivity).backToLogin()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
        viewModel.clearErrorMessage()
    }

    private fun clickMenu(){
        binding.btnMenu.setOnClickListener {
            (activity as UserActivity).startNav()
        }
    }
}